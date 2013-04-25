//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.vista.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.BlocksInSerialRoutine;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.MEntryToolResult;
import com.raygroupintl.m.tool.RecursionSpecification;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.tools.entry.CodeLocations;
import com.raygroupintl.vista.tools.entry.LocalAssignmentAccumulator;
import com.raygroupintl.vista.tools.entry.LocalAssignmentRecorder;
import com.raygroupintl.vista.tools.entryfanin.BlocksInSerialFanouts;
import com.raygroupintl.vista.tools.entryfanin.EntryFaninAccumulator;
import com.raygroupintl.vista.tools.entryfanin.EntryFanins;
import com.raygroupintl.vista.tools.entryfanin.FaninMark;
import com.raygroupintl.vista.tools.entryfanin.MarkedAsFaninBRF;
import com.raygroupintl.vista.tools.entryfanout.EntryFanouts;
import com.raygroupintl.vista.tools.entryinfo.BasicCodeInfoTR;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoAccumulator;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoRecorder;
import com.raygroupintl.vista.tools.entryinfo.FanoutAccumulator;
import com.raygroupintl.vista.tools.entryinfo.VoidBlockRecorder;

public class RepoEntryTools extends Tools {
	private static abstract class EntryInfoToolBase<U extends MEntryToolResult> extends Tool {		
		public EntryInfoToolBase(CLIParams params) {
			super(params);
		}
		
		protected <T> BlocksSupply<Block<T>> getBlocksSupply(RepositoryInfo ri, BlockRecorderFactory<T> f) {
			if ((this.params.parseTreeDirectory == null) || this.params.parseTreeDirectory.isEmpty()) {
				return BlocksInMap.getInstance(f.getRecorder(), ri);
			} else {
				return new BlocksInSerialRoutine<T>(this.params.parseTreeDirectory, f);
			}		
		}

		protected abstract List<U> getResult(RepositoryInfo ri, List<EntryId> entries);
		
		protected void write(EntryId entryId, U result, Terminal t, TerminalFormatter tf, boolean skipEmpty) {			
			if (result.isValid()) {
				if ((! skipEmpty) || (! result.isEmpty())) {
					t.writeEOL(" " + entryId.toString2());		
					this.write(result, t, tf);
				}
			} else {
				t.writeEOL(" " + entryId.toString2());		
				t.writeEOL("  ERROR: Invalid entry point");
				return;
			}
			
		}
				
		protected abstract void write(U result, Terminal t, TerminalFormatter tf);

		private boolean skipEmpty() {
			List<String> outputFlags = this.params.outputFlags;
			for (String outputFlag : outputFlags) {
				if (outputFlag.equals("ignorenodata")) {
					return true;
				}
			}
			return false;			
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {			
					List<EntryId> entries = this.getEntries();
					if (entries != null) {
						List<U> resultList = this.getResult(ri, entries);		
						if (fr.start()) {
							TerminalFormatter tf = new TerminalFormatter();
							tf.setTab(12);
							int n = entries.size();
							for (int i=0; i<n; ++i) {
								this.write(entries.get(i), resultList.get(i), fr, tf, this.skipEmpty());
							}
							fr.stop();
						}
					}
				}
			}
		}
	}

	private static abstract class EntryInfoTool<T, U extends MEntryToolResult> extends EntryInfoToolBase<U> {	
		public EntryInfoTool(CLIParams params) {
			super(params);
		}
		
		protected abstract BlockRecorderFactory<T> getBlockRecorderFactory(final RepositoryInfo ri);
		
		protected abstract List<U> getResult(BlocksSupply<Block<T>> blocksSupply, List<EntryId> entries);
		
		@Override
		public List<U> getResult(final RepositoryInfo ri, List<EntryId> entries) {
			BlockRecorderFactory<T> rf = this.getBlockRecorderFactory(ri);
			BlocksSupply<Block<T>> blocksSupply = this.getBlocksSupply(ri, rf);
			return this.getResult(blocksSupply, entries);
		}
	}

	private static class EntryCodeInfoRecorderFactory implements BlockRecorderFactory<CodeInfo> {
		private RepositoryInfo repositoryInfo;
		
		public EntryCodeInfoRecorderFactory(RepositoryInfo repositoryInfo) {
			this.repositoryInfo = repositoryInfo;
		}
		
		@Override
		public BlockRecorder<CodeInfo> getRecorder() {
			return new EntryCodeInfoRecorder(this.repositoryInfo);
		}
	}

	private static class EntryCodeInfoTool extends EntryInfoToolBase<EntryCodeInfo> {	
		public EntryCodeInfoTool(CLIParams params) {
			super(params);
		}
		
		protected BlockRecorderFactory<CodeInfo> getBlockRecorderFactory(final RepositoryInfo ri) {
			return new EntryCodeInfoRecorderFactory(ri);	
		}

		protected List<EntryCodeInfo> getResult(BlocksSupply<Block<CodeInfo>> blocksSupply, List<EntryId> entries) {
			AssumedVariablesToolParams p = CLIParamsAdapter.toAssumedVariablesToolParams(this.params);
			EntryCodeInfoAccumulator a = new EntryCodeInfoAccumulator(blocksSupply, p);
			return a.getResult(entries);			
		}

		@Override
		public List<EntryCodeInfo> getResult(final RepositoryInfo ri, List<EntryId> entries) {
			BlockRecorderFactory<CodeInfo> rf = this.getBlockRecorderFactory(ri);
			BlocksSupply<Block<CodeInfo>> blocksSupply = this.getBlocksSupply(ri, rf);
			return this.getResult(blocksSupply, entries);
		}

		@Override
		protected void write(EntryCodeInfo result, Terminal t, TerminalFormatter tf) {
			String[] f = result.getFormals();
			AssumedVariables av = result.getAssumedVariablesTR();
			BasicCodeInfoTR ci = result.getBasicCodeInfoTR();
			t.writeFormatted("FORMAL", f, tf);
			t.writeSortedFormatted("ASSUMED", av.toSet(), tf);
			ci.writeInfo(t, tf);
			t.writeEOL();
		}	
	}

	private static class EntryAssumedVarTool extends EntryInfoTool<CodeInfo, AssumedVariables> {	
		public EntryAssumedVarTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected BlockRecorderFactory<CodeInfo> getBlockRecorderFactory(final RepositoryInfo ri) {
			return new EntryCodeInfoRecorderFactory(ri);	
		}
		
		@Override
		protected List<AssumedVariables> getResult(BlocksSupply<Block<CodeInfo>> blocksSupply, List<EntryId> entries) {
			AssumedVariablesToolParams params = CLIParamsAdapter.toAssumedVariablesToolParams(this.params);
			AssumedVariablesTool a = new AssumedVariablesTool(blocksSupply, params);
			return a.getResult(entries);			
		}
		
		@Override
		protected void write(AssumedVariables result, Terminal t, TerminalFormatter tf) {
			t.writeSortedFormatted("ASSUMED", result.toSet(), tf);
		}
	}

	private static class EntryLocalAssignmentRecorderFactory implements BlockRecorderFactory<CodeLocations> {
		private Set<String> localsUnderTest;
		
		public EntryLocalAssignmentRecorderFactory(Set<String> localsUnderTest) {
			this.localsUnderTest = localsUnderTest;
		}
		
		@Override
		public BlockRecorder<CodeLocations> getRecorder() {
			return new LocalAssignmentRecorder(this.localsUnderTest);
		}
	}

	private static class EntryLocalAssignmentTool extends EntryInfoTool<CodeLocations, CodeLocations> {	
		public EntryLocalAssignmentTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected BlockRecorderFactory<CodeLocations> getBlockRecorderFactory(final RepositoryInfo ri) {
			Set<String> locals = new HashSet<String>();
			locals.add("SDCLN");
			return new EntryLocalAssignmentRecorderFactory(locals);	
		}
		
		@Override
		protected List<CodeLocations> getResult(BlocksSupply<Block<CodeLocations>> blocksSupply, List<EntryId> entries) {
			RecursionSpecification rs = CLIParamsAdapter.toRecursionSpecification(this.params);
			FilterFactory<EntryId, EntryId> filterFactory = rs.getFanoutFilterFactory();
			LocalAssignmentAccumulator a = new LocalAssignmentAccumulator(blocksSupply, filterFactory);
			return a.getResult(entries);			
		}

		@Override
		protected void write(CodeLocations result, Terminal t, TerminalFormatter tf) {
			List<CodeLocation> cl = result.getCodeLocations();
			if (cl == null) {
				t.writeEOL("  --");				
			} else {
				for (CodeLocation c : cl) {
					t.writeEOL("  " + c.toString());
				}
			}
		}	
	}

	private static class EntryFanoutRecorderFactory implements BlockRecorderFactory<Void> {
		@Override
		public BlockRecorder<Void> getRecorder() {
			return new VoidBlockRecorder();
		}
	}

	private static class EntryFanoutTool extends EntryInfoTool<Void, EntryFanouts> {	
		public EntryFanoutTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected BlockRecorderFactory<Void> getBlockRecorderFactory(final RepositoryInfo ri) {
			return new EntryFanoutRecorderFactory();	
		}

		@Override
		protected List<EntryFanouts> getResult(BlocksSupply<Block<Void>> blocksSupply, List<EntryId> entries) {
			RecursionSpecification rs = CLIParamsAdapter.toRecursionSpecification(this.params);
			FilterFactory<EntryId, EntryId> filterFactory = rs.getFanoutFilterFactory();
			FanoutAccumulator a = new FanoutAccumulator(blocksSupply, filterFactory);
			return a.getResult(entries);			
		}
		
		@Override
		protected void write(EntryFanouts result, Terminal t, TerminalFormatter tf) {
			Set<EntryId> r = result.getFanouts();
			if (r == null) {
				String em = result.getErrorMsg();
				if (em == null) {
					t.writeEOL("  --");				
				} else {
					t.write("  ");
					t.writeEOL(em);
				}
			} else {
				for (EntryId f : r) {
					t.writeEOL("  " + f.toString2());
				}
			}
		}	
	}

	private static class EntryFanin extends EntryInfoToolBase<EntryFanins> {		
		public EntryFanin(CLIParams params) {
			super(params);
		}
		
		private EntryFaninAccumulator getSupply(EntryId entryId, RepositoryInfo ri) {
			String method = this.params.getMethod("routinefile");
			if (method.equalsIgnoreCase("fanoutfile")) {
				BlocksSupply<Block<FaninMark>> blocksSupply = new BlocksInSerialFanouts(entryId, this.params.parseTreeDirectory);		
				return new EntryFaninAccumulator(blocksSupply, false);
			} else {
				BlocksSupply<Block<FaninMark>> blocksSupply = this.getBlocksSupply(ri, new MarkedAsFaninBRF(entryId));		
				return new EntryFaninAccumulator(blocksSupply, true);
			}
		}
		
		@Override
		public List<EntryFanins> getResult(final RepositoryInfo ri, List<EntryId> entries) {
			List<EntryFanins> resultList = new ArrayList<EntryFanins>();
			for (EntryId entryId : entries) {
				final EntryFaninAccumulator efit = this.getSupply(entryId, ri);
				RecursionSpecification rs = CLIParamsAdapter.toRecursionSpecification(this.params);
				FilterFactory<EntryId, EntryId> filterFactory = rs.getFanoutFilterFactory();
				efit.setFilterFactory(filterFactory);
				RepositoryVisitor rv = new RepositoryVisitor() {
					int packageCount;
					@Override
					protected void visitRoutine(Routine routine) {
						efit.addRoutine(routine);
					}
					@Override
					protected void visitVistaPackage(VistaPackage routinePackage) {
						++this.packageCount;
						MRALogger.logInfo(RepositoryTools.class, String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName() + "...writing");
						super.visitVistaPackage(routinePackage);
						MRALogger.logInfo(RepositoryTools.class, "..done.\n");
					}
				};
				VistaPackages vps = this.getVistaPackages(ri);
				vps.accept(rv);
				EntryFanins result = efit.getResult();
				resultList.add(result);
			}
			return resultList;
		}
		
		@Override
		protected void write(EntryFanins result, Terminal t, TerminalFormatter tf) {
			Set<EntryId> starts = result.getFaninEntries();
			for (EntryId start : starts) {
				Set<EntryId> nextUps = result.getFaninNextEntries(start);
				for (EntryId nextUp : nextUps) {
					t.write("   " + start.toString2() + " thru ");
					t.writeEOL(nextUp.toString2());
				}
			}	
		}
	}
	
	@Override
	protected void updateTools(Map<String, MemberFactory> tools) {
		tools.put("entryinfo", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryCodeInfoTool(params);
			}
		});
		tools.put("entryassumedvar", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryAssumedVarTool(params);
			}
		});
		tools.put("entrylocalassignment", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryLocalAssignmentTool(params);
			}
		});
		tools.put("entryfanout", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryFanoutTool(params);
			}
		});
		tools.put("entryfanin", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryFanin(params);
			}
		});
	}
}
