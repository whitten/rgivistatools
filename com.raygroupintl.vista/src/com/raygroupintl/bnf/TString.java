package com.raygroupintl.bnf;

public class TString implements Token {
	private String value;
		
	public TString(String value) {
		this.value = value;
	}
		
	@Override
	public String getStringValue() {
		return this.value;
	}
	
	@Override
	public int getStringSize() {
		return this.value.length();
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void beautify() {		
	}
}
