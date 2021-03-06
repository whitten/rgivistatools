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

package com.raygroupintl.m.token;

import com.raygroupintl.parser.Token;

public class StringTokens {
	public static class MName extends MString {
		private static final long serialVersionUID = 1L;
		
		public MName(Token token) {
			super(token);
		}
	}

	public static class MIdent extends MString {
		private static final long serialVersionUID = 1L;
		
		public MIdent(Token token) {
			super(token);
		}
	}

	public static class PatAtoms extends MString {
		private static final long serialVersionUID = 1L;
		
		public PatAtoms(Token token) {
			super(token);
		}
	}
}
