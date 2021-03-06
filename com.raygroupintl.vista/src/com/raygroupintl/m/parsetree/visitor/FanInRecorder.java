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

package com.raygroupintl.m.parsetree.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.LineLocation;

public class FanInRecorder extends FanoutRecorder {
	private Map<EntryId, Set<String>> fanins = new HashMap<EntryId, Set<String>>();
	private String currentPackageName = "UNCATEGORIZED";
	
	protected void visitRoutine(Routine routine) {
		super.visitRoutine(routine);
		Map<LineLocation, List<EntryId>> fanouts = this.getRoutineFanouts();
		if (fanouts != null) {
			for (List<EntryId> fs : fanouts.values()) {
				for (EntryId f : fs) {
					Set<String> current = this.fanins.get(f);
					if (current == null) {
						current =  new HashSet<String>();
						this.fanins.put(f, current);
					}
					current.add(this.currentPackageName);
				}		
			}
		}
	}
	
	public void setCurrentPackageName(String currentPackageName) {
		this.currentPackageName = currentPackageName;
	}
	
	public Map<EntryId, Set<String>> getFanIns() {
		return this.fanins;
	}
}
