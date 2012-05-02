package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFDelimitedList;
import com.raygroupintl.bnf.TFEmpty;
import com.raygroupintl.bnf.TFSeqROO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.ITokenFactory;

public class TFUseArgument extends TFChoice {
	private MVersion version;
	
	private TFUseArgument(MVersion version) {
		this.version = version;
	}
	
	private static class TFUseDeviceParam extends TFChoice {
		private MVersion version;
	
		private TFUseDeviceParam(MVersion version) {
			this.version = version;
		}
		
		protected ITokenFactory getFactory(char ch) {
			switch (ch) {
				case ':':
				case ')':
					return TFEmpty.getInstance();
				default:
					return MTFSupply.getInstance(version).expr;
			}
		}
	}
		
	private static class TFUseDeviceParams extends TFChoice {			
		private MVersion version;
		
		private TFUseDeviceParams(MVersion version) {
			this.version = version;
		}
		
		@Override
		protected ITokenFactory getFactory(char ch) {
			ITokenFactory f = new TFUseDeviceParam(this.version);
			if (ch == '(') {
				return TFInParantheses.getInstance(TFDelimitedList.getInstance(f, ':'));
			} else if (ch == ':') {
				return TFEmpty.getInstance(); 
			} else {
				return f;
			}
		}
		
		public static TFUseDeviceParams getInstance(MVersion version) {
			return new TFUseDeviceParams(version);
		}
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '@') {
			return MTFSupply.getInstance(version).indirection;
		} else {
			return TFSeqROO.getInstance(MTFSupply.getInstance(version).expr, 
					TFSeqRequired.getInstance(TFConstChar.getInstance(':'), TFUseDeviceParams.getInstance(this.version)),
					TFSeqRequired.getInstance(TFConstChar.getInstance(':'), TFUseDeviceParams.getInstance(this.version)));
		}
	}
	
	public static TFUseArgument getInstance(MVersion version) {
		return new TFUseArgument(version);
	}
}