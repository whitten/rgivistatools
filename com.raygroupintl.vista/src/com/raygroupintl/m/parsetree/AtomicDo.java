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

public class AtomicDo extends AtomicCommand {
	private static final long serialVersionUID = 1L;

	private boolean postConditional;
	
	public AtomicDo(Node additionalNodes, boolean postConditional) {
		super(additionalNodes);
		this.postConditional = postConditional;
	}

	public boolean getPostConditional() {
		return this.postConditional;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitAtomicDo(this);
	}
}