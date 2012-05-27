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

package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TString;

public class TSymbol extends TString implements RuleSupply {
	public TSymbol(String value) {
		super(value);
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, String name, Map<String, RuleSupply> existing) {
		if (flag == RuleSupplyFlag.TOP) {
			String value = this.getStringValue();
			RuleSupply referred = existing.get(value);
			if (referred == null) {
				return null;
			} else {
				return new FSRCopy(value, referred.getRule(flag, value, existing));
				//return new FSRSingle(value, flag.toRuleRequiredFlag());
			}
		} else {
			String value = this.getStringValue();
			return new FSRSingle(value, flag.toRuleRequiredFlag());			
		}
	}
}
