package com.raygroupintl.bnf.annotation;

import java.util.Map;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFDelimitedList;
import com.raygroupintl.bnf.TFList;
import com.raygroupintl.bnf.TFSequenceStatic;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;

public class TSymbolList extends TArray  implements SequencePieceGenerator {
	public TSymbolList(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	private TokenFactory getListFactory(String name, TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		if (delimiter == null) { 
			return new TFList(name, element);
		} else {
			TFDelimitedList r = new TFDelimitedList(name);
			r.set(element, delimiter, emptyAllowed);
			return r;
		}		
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> map) {
		SequencePieceGenerator elementGenerator = (SequencePieceGenerator) this.get(1);
		TokenFactory element = elementGenerator.getFactory(name + ".element", map);
		TArray delimleftrightspec = (TArray) this.get(2);
		if (delimleftrightspec == null) {
			return new TFList(name, element);
		} else {
			SequencePieceGenerator delimGenerator = (SequencePieceGenerator) delimleftrightspec.get(1);
			TokenFactory delimiter = delimGenerator == null ? null : delimGenerator.getFactory(name + ".delimiter", map);
			TArray leftrightSpec = (TArray) delimleftrightspec.get(2);
			TokenFactory dl = this.getListFactory(name, element, delimiter, false);
			if (leftrightSpec == null) {
				return dl;				
			} else {
				TFSequenceStatic result = new TFSequenceStatic(name);
				SequencePieceGenerator leftGenerator = (SequencePieceGenerator) leftrightSpec.get(1);
				SequencePieceGenerator rightGenerator = (SequencePieceGenerator) leftrightSpec.get(3);
				TokenFactory left = leftGenerator.getFactory(name + ".left", map);
				TokenFactory right = rightGenerator.getFactory(name + ".right", map);
				TokenFactory[] factories = {left, dl, right};
				boolean[] required = {true, true, true};
				result.setFactories(factories, required);
				return result;
			}
		}	
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	
}
