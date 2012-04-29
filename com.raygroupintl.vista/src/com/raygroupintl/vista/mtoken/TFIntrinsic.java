package com.raygroupintl.vista.mtoken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TChar;
import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFEmpty;
import com.raygroupintl.bnf.TFEmptyVerified;
import com.raygroupintl.bnf.TFSeq;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqROO;
import com.raygroupintl.bnf.TFSeqRRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.bnf.TFSyntaxError;
import com.raygroupintl.bnf.TPair;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public class TFIntrinsic extends TFSeq {	
	private static MNameWithMnemonic.Map INTRINSIC_VARIABLES = new MNameWithMnemonic.Map();

	private MVersion version;
	private static boolean initialized;
	
	private TFIntrinsic(MVersion version) {
		this.version = version;
	}
	
	public static void addVariable(String name) {
		String nameUpperCase = name.toUpperCase();
		INTRINSIC_VARIABLES.put(name, new MNameWithMnemonic(nameUpperCase, nameUpperCase));
	}
	
	private static abstract class TIntrinsicName extends TKeyword {
		public TIntrinsicName(String value) {
			super(value);
		}
		
		@Override
		public String getStringValue() {
			return "$" + super.getStringValue();
		}

		@Override
		public int getStringSize() {
			return 1 + super.getStringSize();
		}	

		@Override
		public List<MError> getErrors() {
			return null;  // All errors during parsing
		}
	}

	private static class TIntrinsicVariable extends TIntrinsicName {
		private TIntrinsicVariable(String value) {
			super(value);
		}
	
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String value = this.getIdentier();
			MNameWithMnemonic name = INTRINSIC_VARIABLES.get(value);
			return name;
		}
		
		public static TIntrinsicVariable getInstance(TIdent name) {
			return new TIntrinsicVariable(name.getStringValue());
		}
	}

	private static MNameWithMnemonic.Map INTRINSIC_FUNCTIONS;

	private static class FunctionInfo {
		private ITokenFactory argumentFactory;
		private int minNumArguments;
		private int maxNumArguments;
		private MVersion version;
		
		public FunctionInfo(MVersion version, int minNumArguments, int maxNumArguments) {
			this.minNumArguments = minNumArguments;
			this.maxNumArguments = maxNumArguments;
			this.version = version;
		}		

		public void setArgumentFactory(ITokenFactory f) {
			this.argumentFactory = f;
		}
		
		public int getMinNumArguments() {
			return this.minNumArguments;
		}
		
		public int getMaxNumArguments() {
			return this.maxNumArguments;
		}
		
		public ITokenFactory getArgumentFactory() {
			if (this.argumentFactory == null) {
				return TFDelimitedList.getInstance(TFExpr.getInstance(this.version), ',');
			} else {
				return this.argumentFactory;
			}
		}
	}

	private static Map<String, FunctionInfo> FUNCTION_INFOS;

	private static FunctionInfo addFunction(MVersion version, String mnemonic, String name, int minNumArguments, int maxNumArguments) {
		FunctionInfo fi = new FunctionInfo(version, minNumArguments, maxNumArguments);
		if (INTRINSIC_FUNCTIONS == null) {
			INTRINSIC_FUNCTIONS = new MNameWithMnemonic.Map();	
		}		
		INTRINSIC_FUNCTIONS.update(mnemonic, name);
		if (FUNCTION_INFOS == null) {
			FUNCTION_INFOS = new HashMap<String, FunctionInfo>();	
		}
		FUNCTION_INFOS.put(mnemonic, fi);
		return fi;
	}
		
	public static FunctionInfo addFunction(MVersion version, String name, int minNumArguments, int maxNumArguments) {
		return addFunction(version, name, name, minNumArguments, maxNumArguments);
	}
		
	public static FunctionInfo addFunction(MVersion version, String name) {
		return addFunction(version, name, 1, Integer.MAX_VALUE);
	}

	private static void initialize(final MVersion version) {
		INTRINSIC_VARIABLES.update("D", "DEVICE"); 	
		INTRINSIC_VARIABLES.update("EC", "ECODE"); 	
		INTRINSIC_VARIABLES.update("ES", "ESTACK"); 	
		INTRINSIC_VARIABLES.update("ET", "ETRAP"); 	
		INTRINSIC_VARIABLES.update("H", "HOROLOG"); 	
		INTRINSIC_VARIABLES.update("I", "IO"); 	
		INTRINSIC_VARIABLES.update("J", "JOB"); 	
		INTRINSIC_VARIABLES.update("K", "KEY"); 	
		INTRINSIC_VARIABLES.update("PD", "PDISPLAY"); 	
		INTRINSIC_VARIABLES.update("P", "PRINCIPAL"); 	
		INTRINSIC_VARIABLES.update("Q", "QUIT"); 	
		INTRINSIC_VARIABLES.update("S", "STORAGE"); 	
		INTRINSIC_VARIABLES.update("ST", "STACK"); 	
		INTRINSIC_VARIABLES.update("SY", "SYSTEM"); 	
		INTRINSIC_VARIABLES.update("T", "TEST"); 	
		INTRINSIC_VARIABLES.update("X", "X"); 	
		INTRINSIC_VARIABLES.update("Y", "Y"); 	

		addFunction(version, "A", "ASCII", 1, 2); 	
		addFunction(version, "C", "CHAR", 1, 999); 	
		addFunction(version, "D", "DATA", 1, 1); 	
		addFunction(version, "E", "EXTRACT", 1, 3); 	
		addFunction(version, "F", "FIND", 2, 3); 	
		addFunction(version, "G", "GET", 1, 2); 	
		addFunction(version, "I", "INCREMENT", 1, 2); 	
		addFunction(version, "J", "JUSTIFY", 2, 3); 	
		addFunction(version, "L", "LENGTH", 1, 2); 		
		FunctionInfo o = addFunction(version, "O", "ORDER", 1, 2); 	
		o.setArgumentFactory(TFSeqRO.getInstance(MTFSupply.getInstance(version).getTFGlvn(),
				TFSeqRequired.getInstance(TFConstChar.getInstance(','), TFExpr.getInstance(version))));
		addFunction(version, "P", "PIECE", 2, 4); 	
		addFunction(version, "Q", "QUERY", 1, 1); 	
		addFunction(version, "R", "RANDOM", 1, 1); 	
		addFunction(version, "RE", "REVERSE", 1, 1);		
		FunctionInfo s = addFunction(version, "S", "SELECT", 1, 999);
		s.setArgumentFactory(new TFCommaDelimitedList() {			
			@Override
			protected ITokenFactory getElementFactory() {
				ITokenFactory expr = TFExpr.getInstance(version);
				return TFSeqRequired.getInstance(expr, TFConstChar.getInstance(':'), expr);
			}
		});
		FunctionInfo t = addFunction(version, "T", "TEXT", 1, 1); 
		t.setArgumentFactory(TFGotoArgument.getInstance(version, true));			
		addFunction(version, "V", "VIEW", 1, 999); 	
		addFunction(version, "FN", "FNUMBER", 2, 3); 	
		addFunction(version, "N", "NEXT", 1, 2); 	
		addFunction(version, "NA", "NAME", 1, 2); 	
		addFunction(version, "Q", "QUERY", 1, 2); 	
		addFunction(version, "QL", "QLENGTH", 1, 2); 	
		addFunction(version, "QS", "QSUBSCRIPT", 1, 3);
		addFunction(version, "ST", "STACK", 1, 2);
		addFunction(version, "TR", "TRANSLATE", 1, 3);
		addFunction(version, "WFONT", 4, 4);
		addFunction(version, "WTFIT", 6, 6);
		addFunction(version, "WTWIDTH", 5, 5);

		if (version == MVersion.CACHE) {
			FunctionInfo c = addFunction(version, "CASE", 1, Integer.MAX_VALUE);
			ITokenFactory expr = TFExpr.getInstance(version);
			ITokenFactory ci = TFSeqRequired.getInstance(ChoiceSupply.get(expr, ':', TFEmpty.getInstance()), TFConstChar.getInstance(':'), expr);
			ITokenFactory cases =  TFSeqRequired.getInstance(TFChar.COMMA, TFDelimitedList.getInstance(ci, ','));
			//ITokenFactory d = TFAllRequired.getInstance(TFChar.COMMA, TFChar.COLON,  expr);
			ITokenFactory arg = TFSeqRequired.getInstance(expr,  cases);
			c.setArgumentFactory(arg);
		}
		
		TFIntrinsic.addVariable("ZA");
		TFIntrinsic.addVariable("ZB");
		TFIntrinsic.addVariable("ZC");
		TFIntrinsic.addVariable("ZE");
		TFIntrinsic.addVariable("ZH");
		TFIntrinsic.addVariable("ZJ");
		TFIntrinsic.addVariable("ZJOB");	
		TFIntrinsic.addVariable("ZR");
		TFIntrinsic.addVariable("ZT");
		TFIntrinsic.addVariable("ZV");
		TFIntrinsic.addVariable("ZIO");	
		TFIntrinsic.addVariable("ZIOS");	
		TFIntrinsic.addVariable("ZVER");
		TFIntrinsic.addVariable("ZEOF");
		TFIntrinsic.addVariable("ZNSPACE");
		TFIntrinsic.addVariable("ZINTERRUPT");
		TFIntrinsic.addVariable("ZRO");
		TFIntrinsic.addVariable("R");
		TFIntrinsic.addVariable("ZS");
		TFIntrinsic.addVariable("ZROUTINES");
		TFIntrinsic.addVariable("ETRAP");
		TFIntrinsic.addVariable("ZTIMESTAMP");
		TFIntrinsic.addVariable("ZERROR");
		TFIntrinsic.addVariable("ZCMDLINE");
		TFIntrinsic.addVariable("ZPOSITION");
		TFIntrinsic.addFunction(version, "ZBITGET");
		TFIntrinsic.addFunction(version, "ZBN");
		TFIntrinsic.addFunction(version, "ZC");
		TFIntrinsic.addFunction(version, "ZF");
		TFIntrinsic.addFunction(version, "ZJ");
		TFIntrinsic.addFunction(version, "ZU");
		TFIntrinsic.addFunction(version, "ZUTIL");
		TFIntrinsic.addFunction(version, "ZTRNLNM");	
		TFIntrinsic.addFunction(version, "ZBOOLEAN");	
		TFIntrinsic.addFunction(version, "ZDEV");	
		TFIntrinsic.addFunction(version, "ZGETDV");
		TFIntrinsic.addFunction(version, "ZSORT");
		TFIntrinsic.addFunction(version, "ZESCAPE");
		TFIntrinsic.addFunction(version, "ZSEARCH");
		TFIntrinsic.addFunction(version, "ZPARSE");
		TFIntrinsic.addFunction(version, "ZCONVERT");
		TFIntrinsic.addFunction(version, "ZDVI");
		TFIntrinsic.addFunction(version, "ZGETDVI");
		TFIntrinsic.addFunction(version, "ZOS");
		TFIntrinsic.addFunction(version, "ZINTERRUPT");
		TFIntrinsic.addFunction(version, "ZJOB");
		TFIntrinsic.addFunction(version, "ZBITSTR");
		TFIntrinsic.addFunction(version, "ZBITXOR");
		TFIntrinsic.addFunction(version, "LISTGET");
		TFIntrinsic.addFunction(version, "ZDEVSPEED");
		TFIntrinsic.addFunction(version, "ZGETJPI");
		TFIntrinsic.addFunction(version, "ZGETSYI");
		TFIntrinsic.addFunction(version, "ZUTIL");	
		TFIntrinsic.addFunction(version, "ZK");	
		TFIntrinsic.addFunction(version, "ZWA");
		TFIntrinsic.addFunction(version, "ZVERSION");
	}

	private static class TIntrinsicFunctionName extends TIntrinsicName {
		public TIntrinsicFunctionName(String name) {
			super(name);
		}
		
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String value = this.getIdentier();
			MNameWithMnemonic name = INTRINSIC_FUNCTIONS.get(value);
			return name;
		}		
	}
	
	private static class TIntrinsicFunction extends TPair {
		private TIntrinsicFunction(TIntrinsicFunctionName name, TInParantheses argument) {
			super(name, argument);
		}

		public static TIntrinsicFunction getInstance(TIdent name, TInParantheses argument) {
			TIntrinsicFunctionName functionName = new TIntrinsicFunctionName(name.getStringValue());
			return new TIntrinsicFunction(functionName, argument);
		}
	}
	
	private static String getFoundIntrinsicName(IToken[] tokens) {
		String name = ((tokens[0] instanceof TArray) ? (TArray) tokens[0] : (TPair) tokens[0]).get(1).getStringValue().toUpperCase();
		return name;
	}
	
	@Override
	protected ITokenFactorySupply getFactorySupply() {
		return new ITokenFactorySupply() {			
			@Override
			public int getCount() {
				return 4;
			}
			
			@Override
			public ITokenFactory get(int seqIndex, IToken[] previousTokens) {
				switch (seqIndex) {
					case 0: {
						if (TFIntrinsic.this.version == MVersion.CACHE) {
							return TFSeqRRO.getInstance(TFConstChar.getInstance('$'), TFIdent.getInstance(), TFList.getInstance(TFSeqRequired.getInstance(TFChar.DOT, TFName.getInstance())));
						} else {
							return TFSeqRequired.getInstance(TFConstChar.getInstance('$'), TFIdent.getInstance());
						}
					}
					case 1:
						return TFConstChar.getInstance('(');
					case 2: {
						String name = getFoundIntrinsicName(previousTokens);
						if (TFIntrinsic.this.version == MVersion.CACHE) {
							if (name.equals("SYSTEM") || name.equals("SY")) {
								return TFDelimitedList.getInstance(MTFSupply.getInstance(TFIntrinsic.this.version).getTFActual(), ',');
							}
						}
						MNameWithMnemonic mName = INTRINSIC_FUNCTIONS.get(name);
						if (mName == null) {
							return TFSyntaxError.getInstance(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION);
						}
						String mnemonic = mName.getMnemonic();
						FunctionInfo info = FUNCTION_INFOS.get(mnemonic);
						ITokenFactory argumentFactory = info.getArgumentFactory();
						return argumentFactory;
					}
					case 3:
						return TFConstChar.getInstance(')');
					default:
						return null;						
				}
			}
		};
	}

	@Override
	protected int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return RETURN_NULL;
		} else if (seqIndex == 1) {
			String name = getFoundIntrinsicName(foundTokens);
			if (INTRINSIC_VARIABLES.containsKey(name)) {
				return RETURN_TOKEN;
			} else {
				return MError.ERR_UNKNOWN_INTRINSIC_VARIABLE;	
			}
		} else if (seqIndex == 2) {
			return MError.ERR_GENERAL_SYNTAX;
		} else {
			return MError.ERR_UNMATCHED_PARANTHESIS;
		}
	}
	
	@Override
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return 0;
		} else {
			return MError.ERR_UNMATCHED_PARANTHESIS;	
		}
	}
	
	@Override
	protected IToken getToken(IToken[] foundTokens) {
		TArray token0 = (TArray) foundTokens[0];
		if (token0.get(2) == null) {		
			TIdent name = (TIdent) token0.get(1);		
			if (foundTokens[1] == null) {
				return TIntrinsicVariable.getInstance(name);			
			} else {
				return TIntrinsicFunction.getInstance(name, new TInParantheses(foundTokens[2]));
			}
		} else {
			return new TArray(foundTokens);
		}
	}
	
	public static TFIntrinsic getInstance(MVersion version) {
		if (! initialized) {
			initialize(version);
			initialized = true;
		}		
		return new TFIntrinsic(version);
	}
}
