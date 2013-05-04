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

package com.raygroupintl.m.tool.entry.assumedvariables;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.m.tool.entry.MEntryTool;
import com.raygroupintl.m.tool.entry.RecursiveDataAggregator;
import com.raygroupintl.struct.Filter;

public class AssumedVariablesTool extends MEntryTool<AssumedVariables, IndexedFanout, AVBlockData> {
	private static class AVTRecorderFactory implements BlockRecorderFactory<IndexedFanout, AVBlockData> {
		@Override
		public AVRecorder getRecorder() {
			return new AVRecorder();
		}
	}

	private static class AVTDataAggregator extends RecursiveDataAggregator<Map<String, CodeLocation>, IndexedFanout, AVBlockData> {
		public AVTDataAggregator(Block<IndexedFanout, AVBlockData> block, BlocksSupply<IndexedFanout, AVBlockData> supply) {
			super(block, supply);
		}
		
		@Override
		protected Map<String, CodeLocation> getNewDataInstance(AVBlockData blockData) {
			return new HashMap<>(blockData.getAssumedLocals());		
		}
		
		@Override
		protected int updateData(AVBlockData targetBlockData, Map<String, CodeLocation> targetData, Map<String, CodeLocation> sourceData, IndexedFanout property) {
			int result = 0;
			for (String name : sourceData.keySet()) {
				if (! targetBlockData.isDefined(name, property.getIndex())) {
					if (! targetData.containsKey(name)) {
						targetData.put(name, sourceData.get(name));
						++result;
					}
				}
			}
			return result;
		}		
	}
	
	private DataStore<Map<String, CodeLocation>> store = new DataStore<Map<String, CodeLocation>>();					
	private AssumedVariablesToolParams params;
	
	public AssumedVariablesTool(AssumedVariablesToolParams params) {
		super(params);
		this.params = params;
	}
	
	@Override
	protected BlockRecorderFactory<IndexedFanout, AVBlockData> getBlockRecorderFactory() {
		return new AVTRecorderFactory();
	}

	@Override
	public AssumedVariables getResult(Block<IndexedFanout, AVBlockData> block, Filter<Fanout> filter, Set<EntryId> missingEntryIds) {
		AVTDataAggregator ala = new AVTDataAggregator(block, blocksSupply);
		Map<String, CodeLocation> assumedVariables = ala.get(this.store, filter, missingEntryIds);
		if (assumedVariables != null) {
			for (String name : this.params.getExpected()) {
				assumedVariables.remove(name);
			}
		}
		return new AssumedVariables(assumedVariables);	
	}
}
