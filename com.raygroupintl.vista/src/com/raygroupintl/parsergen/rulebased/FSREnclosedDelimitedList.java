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

package com.raygroupintl.parsergen.rulebased;

import java.lang.reflect.Constructor;

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.AdapterSpecification;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;

public class FSREnclosedDelimitedList<T extends Token> extends FSRContainer<T> {
	private FactorySupplyRule<T> element;
	private FactorySupplyRule<T> delimiter;
	private FactorySupplyRule<T> left;
	private FactorySupplyRule<T> right;
	private boolean empty;
	private boolean none;
	private TFSequence<T> factory;
	
	public FSREnclosedDelimitedList(String name, RuleSupplyFlag flag) {
		super(flag);
		this.factory = new TFSequence<T>(name, 3);
	}
	
	public void setEmptyAllowed(boolean b) {
		this.empty = b;
	}
	
	
	public void setNoneAllowed(boolean b) {
		this.none = b;
	}
		
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public boolean update(RulesByName<T> symbols) {
		RulesByNameLocal<T> localSymbols = new RulesByNameLocal<T>(symbols, this);
		String name = this.factory.getName();
		this.element.update(localSymbols);
		this.delimiter.update(localSymbols);
		TokenFactory<T> e = this.element.getTheFactory(localSymbols);
		TokenFactory<T> d = this.delimiter.getTheFactory(localSymbols);
		TFDelimitedList<T> dl = new TFDelimitedList<T>(name);		
		dl.set(e, d, this.empty);
		this.left.update(localSymbols);
		this.right.update(localSymbols);
		TokenFactory<T> l = this.left.getTheFactory(localSymbols);
		TokenFactory<T> r = this.right.getTheFactory(localSymbols);
		
		this.factory.reset(4);
		this.factory.add(l, true);
		this.factory.add(dl, ! this.none);
		this.factory.add(r, true);
		return true;		
	}

	@Override
	public TFSequence<T> getShellFactory() {
		return this.factory;
	}

	@Override
	public void setAdapter(AdapterSpecification<T> spec) {
		 Constructor<? extends T> a = spec.getSequenceTokenAdapter();
		 if (a != null) this.factory.setSequenceTargetType(a);
	}	
	
	@Override
	public void set(int index, FactorySupplyRule<T> r) {
		switch(index) {
		case 0: 
			this.element = r; 
			break;
		case 1: 
			this.delimiter = r; 
			break;
		case 2:
			this.left = r;
			break;
		case 3:
			this.right = r;
			break;
		default:
			throw new IndexOutOfBoundsException();				
		}
	}		
}