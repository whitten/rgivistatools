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

package com.raygroupintl.m.parsetree.data;

public class EntryId implements Comparable<EntryId> {
	public enum StringFormat {
		SF_SINGLE_LABEL,
		SF_SINGLE_ROUTINE;
	}
	
	private String routineName;
	private String label;
	
	public EntryId(String routineName, String label) {
		this.routineName = routineName;
		this.label = label;
	}
	
	public String getRoutineName() {
		return routineName;
	}
	
	public String getTag() {
		return this.label;
	}
	
	public String getLabelOrDefault() {
		if (this.label == null) {
			return this.routineName;
		} else {
			return this.label;
		}
	}
	
	@Override
	public boolean equals(Object rhs) {
		if ((rhs != null) && (rhs instanceof EntryId)) {	
			String lhsString = this.toString();
			String rhsString = rhs.toString();
			return lhsString.equals(rhsString);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = this.toString().hashCode(); 
		return result;
	}
	
	@Override
	public String toString() {
		String lbl = this.getTag();
		String rou = this.getRoutineName();
		if (rou != null) {
			rou = "^" + rou;
		} else {
			rou = "";
		}
		if (lbl == null) {
			lbl = "";
		}					
		return lbl + rou;		
	}

	private int compareLabels(String lhs, String rhs) {
		if (lhs == null) {
			if (rhs == null) return 0;
			return 1;
		}
		if (rhs == null) return -1;
		return lhs.compareTo(rhs);
	}
	
	@Override
	public int compareTo(EntryId rhs) {
		if (rhs.routineName == null) {
			if (this.routineName != null) return -1;
			return compareLabels(this.label, rhs.label);
		}
		if (this.routineName == null) return 1;
		int result = this.routineName.compareTo(rhs.routineName);
		if (result == 0) {
			return compareLabels(this.label, rhs.label);
		} else {
			return result;
		}
	}
	
	public static EntryId getInstance(String tag, StringFormat format) {
		if (tag != null) {
			String[] pieces = tag.split("\\^");
			if ((pieces != null) && (pieces.length > 0) && (pieces.length < 3)) {
				if (pieces.length > 1) {
					String label = pieces[0];
					String routine = pieces[1];
					return new EntryId(routine, label);
				} else {
					switch (format) {
					case SF_SINGLE_ROUTINE:
						return new EntryId(pieces[0], null);
					default:
						return new EntryId(null, pieces[0]);
					}
				}
			}
		}
		return null;
	}

	public static EntryId getInstance(String tag) {
		return getInstance(tag, StringFormat.SF_SINGLE_LABEL);
	}
}
