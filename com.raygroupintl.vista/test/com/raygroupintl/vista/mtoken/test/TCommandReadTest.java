package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TCommandReadTest {
	public void test(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "R !,\"Select DEBTOR NAME or BILL NUMBER: \",X:DTIME");
	}

	@Test
	public void test() {
		test(MVersion.CACHE);
		test(MVersion.ANSI_STD_95);
	}
}