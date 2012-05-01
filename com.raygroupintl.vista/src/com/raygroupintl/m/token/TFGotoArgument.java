package com.raygroupintl.m.token;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFNull;
import com.raygroupintl.bnf.TFSeq;
import com.raygroupintl.bnf.TFSeqOR;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;

public class TFGotoArgument extends TFSeq {
	protected MVersion version;
	
	private TFGotoArgument(MVersion version) {
		this.version = version;
	}	
	
	private static ITokenFactory getFactory0(IToken[] previousTokens, final MVersion version) {
		TFLabel tfl = TFLabel.getInstance();
		ITokenFactory tfi = MTFSupply.getInstance(version).indirection;
		ITokenFactory tf = ChoiceSupply.get(tfl, tfi);
		return tf; 
	}
	
	private static ITokenFactory getFactory1(IToken[] previousTokens, final MVersion version) {
		return new TFSeqRequired() {								
			@Override
			protected ITokenFactory[] getFactories() {
				TFConstChar tfc = TFConstChar.getInstance('+');
				ITokenFactory tfe = MTFSupply.getInstance(version).expr;
				return new ITokenFactory[]{tfc, tfe};
			}
		};
	}

	private static ITokenFactory getFactory2(IToken[] previousTokens) {
		return TFConstChar.getInstance('^');
	}

	private static ITokenFactory getFactory3(IToken[] previousTokens, MVersion version) {
		if (previousTokens[2] == null) {
			return new TFNull();
		} else {
			ITokenFactory tfEnv = MTFSupply.getInstance(version).environment;
			ITokenFactory tfName = TFName.getInstance();
			ITokenFactory tfEnvName = TFSeqOR.getInstance(tfEnv, tfName);
			ITokenFactory tfInd = MTFSupply.getInstance(version).indirection;
			return ChoiceSupply.get(tfEnvName, tfInd);
		}
	}

	private static ITokenFactorySupply getFactorySupply(final MVersion version, final int count) {
		return new ITokenFactorySupply() {				
			@Override
			public int getCount() {
				return count;
			}
			
			@Override
			public ITokenFactory get(int seqIndex, IToken[] previousTokens) {
				switch (seqIndex) {
					case 0: return TFGotoArgument.getFactory0(previousTokens, version); 
					case 1: return TFGotoArgument.getFactory1(previousTokens, version); 
					case 2: return TFGotoArgument.getFactory2(previousTokens); 
					case 3: return TFGotoArgument.getFactory3(previousTokens, version); 
					case 4: return MTFSupply.getInstance(version).postcondition;
					default:
						assert(false);
						return null;
				}
			}
		};
	}

	protected ITokenFactorySupply getFactorySupply() {
		return  getFactorySupply(this.version, 5);
	}

	@Override
	protected int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 2) {
			if ((foundTokens[0] == null) && (foundTokens[1] == null)) {
				return RETURN_NULL;
			} 
		}
		if (seqIndex == 3) {
			if (foundTokens[2] != null) {
				return this.getErrorCode();
			}
		}
		return CONTINUE;
	}
	
	@Override		
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 2) {
			return this.getErrorCode();
		}
		return 0;
	}
	
	private static class TFGotoArgumentNoPostCondition extends TFGotoArgument {
		private TFGotoArgumentNoPostCondition(MVersion version) {
			super(version);
		}	
		
		@Override
		protected ITokenFactorySupply getFactorySupply() {
			return getFactorySupply(this.version, 4);
		}
	}
		
	public static TFGotoArgument getInstance(MVersion version) {
		return new TFGotoArgument(version);
	}
	
	public static TFGotoArgument getInstance(MVersion version, boolean noPostCondition) {
		if (noPostCondition) {
			return new TFGotoArgumentNoPostCondition(version);
		} else {
			return new TFGotoArgument(version);
		}
	}	
}