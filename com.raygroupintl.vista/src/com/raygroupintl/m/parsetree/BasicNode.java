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

package com.raygroupintl.m.parsetree;

public abstract class BasicNode implements Node {
	@Override
	public void acceptPreAssignment(Visitor visitor) {
		this.accept(visitor);
	}

	@Override
	public void acceptPostAssignment(Visitor visitor) {
	}

	@Override
	public boolean setEntryList(EntryList entryList) {
		return false;
	}

	@Override
	public void acceptExclusiveNew(Visitor visitor) {
		this.accept(visitor);		
	}
	
	@Override
	public void acceptNew(Visitor visitor) {
		this.accept(visitor);
	}
	
	@Override
	public void acceptExclusiveKill(Visitor visitor) {
		this.accept(visitor);		
	}
	
	@Override
	public void acceptKill(Visitor visitor) {
		this.accept(visitor);
	}

	@Override
	public void acceptPreMerge(Visitor visitor) {
		this.accept(visitor);
	}

	@Override
	public void acceptPostMerge(Visitor visitor) {
	}
}