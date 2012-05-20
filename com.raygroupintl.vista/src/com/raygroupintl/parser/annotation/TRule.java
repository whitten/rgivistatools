package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TRule extends TDelimitedList implements RuleGenerator {
	public TRule(List<Token> token) {
		super(token);
	}
	
	@Override
	public TokenFactory getTopFactoryShell(String name, Map<String, TokenFactory> symbols) {
		int size = this.size();
		if (size == 0) {
			throw new ParseErrorException("Empty rule.");
		}
		if (size > 1) {
			return new TFSequence(name);
		}
		RulePieceGenerator rpg = (RulePieceGenerator) this.get(0);
		return rpg.getTopFactory(name, symbols, true);
	}
		
	@Override
	public TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols) {
		List<TokenFactory> factories = new ArrayList<TokenFactory>();
		List<Boolean> flags = new ArrayList<Boolean>();
		int index = 0;
		for (Iterator<Token> it=this.iterator(); it.hasNext(); ++index) {
			RulePieceGenerator rpg = (RulePieceGenerator) it.next();
			TokenFactory f = rpg.getFactory(name + "." + String.valueOf(index), symbols);
			boolean b = rpg.getRequired();
			factories.add(f);
			flags.add(b);
		}
		if (factories.size() == 0) return null;
		if (factories.size() == 1) {
			RulePieceGenerator rpg = (RulePieceGenerator) this.get(0);
			return rpg.getTopFactory(name, symbols, false);
		}		
		
		TFSequence result = new TFSequence(name);
		
		int n = factories.size();
		TokenFactory[] fs = new TokenFactory[n];
		boolean[] bs = new boolean[n];
		for (int i=0; i<n; ++i) {
			fs[i] = factories.get(i);
			bs[i] = flags.get(i);
		}		
		result.setFactories(fs, bs);
		return result;
	}
}