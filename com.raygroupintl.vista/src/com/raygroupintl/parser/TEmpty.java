package com.raygroupintl.parser;

public class TEmpty implements Token {
	@Override
	public String getStringValue() {
		return "";
	}

	@Override
	public int getStringSize() {
		return 0;
	}

	@Override
	public void beautify() {
	}
}