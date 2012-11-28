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

package com.raygroupintl.vista.tools.entryfanin;

import java.util.SortedSet;
import java.util.TreeSet;

import com.raygroupintl.m.parsetree.data.EntryId;

public class PathPieceToEntry {
	private EntryId start;
	private SortedSet<EntryId> nexts;
	
	public PathPieceToEntry(EntryId start) {
		this.start = start;
	}
	
	public EntryId getStartEntry() {
		return this.start;
	}
		
	public SortedSet<EntryId> getNextEntries() {
		return this.nexts;
	}

	public boolean exist() {
		return this.nexts != null;
	}
	
	public boolean add(EntryId next) {
		if (this.nexts == null) {
			this.nexts = new TreeSet<EntryId>();
		}
		if (! this.nexts.contains(next)) {
			this.nexts.add(next);
			return true;
		}
		return false;
	}
}
