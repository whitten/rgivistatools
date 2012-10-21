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

package com.raygroupintl.parser;

import java.lang.reflect.Constructor;

import com.raygroupintl.parsergen.ObjectSupply;

public abstract class TokenFactory {
	private String name;
	private Constructor<? extends Token> ctr;
	
	protected TokenFactory(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSequenceCount() {
		return 1;
	}
	
	public final Token tokenize(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		Token result = this.tokenizeOnly(text, objectSupply);
		return this.convert(result);
	}
	
	protected final Token convert(Token token) {
		if ((this.ctr != null) && (token != null)) {
			try{
				return this.ctr.newInstance(token); 
			} catch (Throwable t) {
				throw new IllegalStateException("Invalid token type had been assigned.");
			}
		} else {
			return token;
		}
	}

	protected abstract Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException;
	
	public <M extends Token> void setTargetType(Constructor<? extends Token> constructor) {
		this.ctr = constructor;
	}
}