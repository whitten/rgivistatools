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

package com.raygroupintl.m.tool.entry.fanin;

import java.util.HashMap;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FanoutType;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.m.tool.entry.fanout.RoutineFanouts;
import com.raygroupintl.struct.HierarchicalMap;
import com.raygroupintl.vista.tools.fnds.ToolUtilities;

public class FanoutFileBasedBlocksSupply extends BlocksSupply<Fanout, FaninMark> {
	private EntryId entryUnderTest;
	private String inputPath;
	private HashMap<String, HierarchicalMap<String, Block<Fanout, FaninMark>>> blocks = new HashMap<String, HierarchicalMap<String, Block<Fanout, FaninMark>>>();
	
	public FanoutFileBasedBlocksSupply(EntryId entryUnderTest, String inputPath) {
		this.entryUnderTest = entryUnderTest;
		this.inputPath = inputPath;
	}
	
	private HierarchicalMap<String, Block<Fanout, FaninMark>> getBlocks(String routineName, RoutineFanouts fanouts) {
		if (fanouts != null) {
			HierarchicalMap<String, Block<Fanout, FaninMark>> result = new HierarchicalMap<String, Block<Fanout, FaninMark>>();
			Set<String> entryTags = fanouts.getRoutineEntryTags();
			EntryId eidUnderTest = this.entryUnderTest;
			if (routineName.equals(this.entryUnderTest.getRoutineName())) {
				eidUnderTest = new EntryId(null, this.entryUnderTest.getLabelOrDefault());
			}
			for (String entryTag : entryTags) {
				EntryId beid = new EntryId(routineName, entryTag);
				Set<EntryId> entryFanouts = fanouts.getFanouts(entryTag);
				FaninMark fim = new FaninMark(beid);
				if (entryFanouts.contains(eidUnderTest)) {
					fim.set(this.entryUnderTest);
				}
				for (EntryId eid : entryFanouts) {
					Fanout ifo = new Fanout(eid, FanoutType.DO_BLOCK);
					fim.addFanout(ifo);
				}
				Block<Fanout, FaninMark> blx = new Block<Fanout, FaninMark>(result, fim);
				result.put(entryTag, blx);
			}
			return result;
		}
		return null;		
	}
	
	@Override
	public HierarchicalMap<String, Block<Fanout, FaninMark>> getBlocks(String routineName) {
		if (! this.blocks.containsKey(routineName)) {			
			RoutineFanouts fanouts = (RoutineFanouts) ToolUtilities.readSerializedRoutineObject(this.inputPath, routineName, ".fo");
			HierarchicalMap<String, Block<Fanout, FaninMark>> result = this.getBlocks(routineName, fanouts);
			this.blocks.put(routineName, result);
			return result;
		}
		return this.blocks.get(routineName);
	}	
}