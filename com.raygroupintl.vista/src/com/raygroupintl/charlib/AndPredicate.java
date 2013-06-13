//---------------------------------------------------------------------------
// Copyright 2013 PwC
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

package com.raygroupintl.charlib;


public class AndPredicate implements Predicate {
	private Predicate predicate0;
	private Predicate predicate1;
	
	public AndPredicate(Predicate predicate0, Predicate predicate1) {
		this.predicate0 = predicate0;
		this.predicate1 = predicate1;
	}
	@Override
	public boolean check(char ch) {
		return this.predicate0.check(ch) && this.predicate1.check(ch);
	}
}
