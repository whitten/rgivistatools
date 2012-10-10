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

package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.DefaultObjectSupply;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parsergen.ObjectSupply;
import com.raygroupintl.parsergen.ParseErrorException;
import com.raygroupintl.parsergen.ParseException;

public class RuleParser {
	private RuleGrammar grammar;

	public RuleSupply getTopTFRule(String name, String ruleText) {
		if (this.grammar == null) {
			try {
				RuleDefinitionParserGenerator parserGen = new RuleDefinitionParserGenerator();
				this.grammar = parserGen.generate(RuleGrammar.class);
			} catch (ParseException e) {
				throw new ParseErrorException("Error in rule grammar: " + e.getMessage());
			}
		}
		Text text = new Text(ruleText);
		try {
			ObjectSupply objectSupply = new DefaultObjectSupply();
			TRule t = (TRule) this.grammar.rule.tokenize(text, objectSupply);
			int tLength = t.toValue().length();
			if (tLength != ruleText.length()) {
				String msg = "Error in rule " + name + " at position " + String.valueOf(tLength);		
				throw new ParseErrorException(msg);					
			}
			return t;
		} catch (SyntaxErrorException e) {
			int errorLocation = text.getIndex();
			String msg = "Error in rule " + name + " at position " + String.valueOf(errorLocation);		
			throw new ParseErrorException(msg);
		}		
	}	
}