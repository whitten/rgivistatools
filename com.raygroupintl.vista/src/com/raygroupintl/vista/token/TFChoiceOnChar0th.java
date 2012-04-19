package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.ICharPredicate;
import com.raygroupintl.vista.fnds.ITokenFactory;

class TFChoiceOnChar0th extends TFChoiceOnChar {
	public TFChoiceOnChar0th(ITokenFactory defaultFactory, ICharPredicate[] predicates, ITokenFactory... factories) {
		super(defaultFactory, predicates, factories);
	}

	@Override
	protected ITokenFactory getFactory(String line, int index) {
		if (index < line.length()) {
			char ch = line.charAt(index);
			return this.getFactory(ch);
		} else {
			return null;
		}
	}		
}
