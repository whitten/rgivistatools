package com.raygroupintl.vista.mtoken.test;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.TFLine;
import com.raygroupintl.vista.struct.MError;

public class TLineTest0 {
	private IToken lineTest(String line, boolean errorAsWell) {
		TFLine f = TFLine.getInstance();
		IToken t = f.tokenize(line, 0);
		String r = t.getStringValue();
		Assert.assertEquals(line, r);	
		if (errorAsWell) {
			List<MError> error = t.getErrors();
			int numErrors = error == null ? 0 : error.size();
			Assert.assertEquals(0, numErrors);	
		}
		return t;
	}

	private void noErrorTest(String lineUnderTest) {
		TFLine f = TFLine.getInstance();
		IToken line = f.tokenize(lineUnderTest, 0);
		Assert.assertFalse("Unexpected error", line.hasError());
		Assert.assertFalse("Unexpected fatal error", line.hasFatalError());				
	}
	
	private IToken lineTest(String line) {
		return lineTest(line, true);
	}
	
	@Test
	public void testBasic() {
		lineTest(" S A=A+1  F  S B=$O(^F(B)) Q:B=\"\"   S ^F(B,A)=5");
		lineTest(" S $E(A)=J+1 Q:B=\"\"\"YYY\"\"\"  Q:B=\"\"\"XXX\"\"\"");
		lineTest(" I '$D(USERPRT),(STATUS'=\"c\") Q ;not individual & not complete");
		lineTest("PRCA219P ;ALB/RRG - REPORT LIKELY BILLS TO PRINT;;");
		lineTest(" I $$DEVICE() D ENTER");
		lineTest("DEVICE() ;");
		lineTest(" SET @A=\"S\"  SET @H@(0)=3");
		lineTest(" I Y>0 S DEBT=$P($G(^PRCA(430,Y,0)),\"^\",9) I DEBT S PRCADB=$P($G(^RCD(340,DEBT,0)),\"^\"),^DISV(DUZ,\"^PRCA(430,\")=Y,$P(DEBT,\"^\",2)=$$NAM^RCFN01(DEBT) D COMP,EN1^PRCAATR(Y) G:$D(DTOUT) Q G ASK");
		lineTest(" S ^DISV(DUZ,\"^RCD(340,\")=+Y,PRCADB=$P(Y,\"^\",2),DEBT=+Y_\"^\"_$P(@(\"^\"_$P(PRCADB,\";\",2)_+PRCADB_\",0)\"),\"^\")", false);
		lineTest(" D DIVIS G:IBQUIT EXIT");
		lineTest(" D DQ");
		lineTest(" .F FLD=1:1:$L(LST,\",\") Q:$P(LST,\",\",FLD)']\"\"  D @(+$P(LST,\",\",FLD)) Q:$G(PSODIR(\"DFLG\"))!($G(PSODIR(\"QFLG\")))");
		lineTest(" ESTART", false);
		lineTest(" D ^%ZISC");
		lineTest(" U IO D @PRCARN D ^%ZISC K %ZIS Q");
		lineTest(" S K=7 F L=2:1:4 S K=K-1 D:IOST?1\"C-\".E WAIT^YSUTL:$Y+4>IOSL Q:YSLFT  W !!,^YTT(601,YSTEST,\"G\",L,1,1,0) D CK");	
		lineTest(" D ##class(%XML.TextReader).ParseStream(RESTOBJ.HttpResponse.Data,.AREADER)", false);
		lineTest(" S CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=\"This message is sent to alert you to conditions regarding this \"_RCTYP_\".\",CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=\" \"");
		lineTest(" S CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=\"The following electronic \"_RCTYP_\" was received at your site.\",CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=\"It was received on: \"_$$FMTE^XLFDT($$NOW^XLFDT(),2)_\" in mail msg # \"_RCXMG_\".\"");
		lineTest(" S Z=0 F  S Z=$O(@RCVAR@(Z)) Q:'Z  I $D(@RCVAR@(Z,\"*\")) S CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=@RCVAR@(Z,\"\")");
		lineTest(" L -@TASKNODE@(\"T\",0)");
		lineTest(" . X \"S RC=\"_@RULENODE@(1)");
		lineTest(" Q $D(@HANDLE@(\"Pr\",\"Handle\",CHILD))");
		lineTest(" .S BREAK=0 F  Q:BREAK||READER.EOF||'READER.Read()  D", false);
		lineTest(" F STAT=42,16 F  S BILLN=$O(^PRCA(430,\"AC\",STAT,BILLN)) Q:'BILLN  I $$ACCK(BILLN) D");
		lineTest(" S PRCANODE=.11 S:$P(Y,\";\",2)=\"DIC(4,\" PRCANODE=1 S PRCANODE=\"^\"_$P(Y,\";\",2)_+$P(Y,\"^\",2)_\",\"_PRCANODE_\")\",PRCANODE=$G(@PRCANODE)");
		lineTest(" . D SENDMSG^XMXAPI(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		lineTest("CONT F XT1=1:1 S XT2=$T(ROU+XT1) Q:XT2=\"\"  S X=$P(XT2,\" \",1),XT3=$P(XT2,\";\",3) X XT4 I $T W !,X X ^%ZOSF(\"TEST\") S:'$T XT3=0 X:XT3 ^%ZOSF(\"RSUM\") W ?10,$S('XT3:\"Routine not in UCI\",XT3'=Y:\"Calculated \"_$C(7)_Y_\", off by \"_(Y-XT3),1:\"ok\")");
		lineTest(" S XT4=\"I 1\",X=$T(+3) W !!,\"Checksum routine created on \",$P(X,\";\",4),\" by KERNEL V\",$P(X,\";\",3),!");
		lineTest(" S IOP=IOP_\";255\",%ZIS=\"\" D ^%ZIS G:POP H^XUS U IO X ^%ZOSF(\"TYPE-AHEAD\"),^%ZOSF(\"LABOFF\") S X=0 X ^%ZOSF(\"RM\")");
		lineTest(" I $D(@G)#10 D WRITE(IO,G)");
		lineTest(" .I $Y>(IOSL-9) D UP^DVBCRPR1,NEXT,HDR^DVBCRPR1 W:$O(^DVB(396.4,OLDA,\"RES\",LINE))]\"\"&('+$G(DVBGUI)) !!,\"Exam Results Continued\",!!");
		lineTest(" S Y=$$FPS^RCAMFN01($S($G(LDT)>0:$E(LDT,1,5),1:$E(DT,1,5))_$TR($J($$PST^RCAMFN01(DEB),2),\" \",0),$S(+$E($G(LDT),6,7)>$$STD^RCCPCFN:2,1:1)) D DD^%D");
	}
	
	@Test
	public void testNoErrors() {
		noErrorTest(" G @(\"TAG\"_B):C'>3");
		noErrorTest(" G @A^@B");
		noErrorTest(" G TAG3:A=3,@(\"TAG\"_B):C'>3,@A^@B");
		noErrorTest(" D COMP,EN1^PRCAATR(Y) G:$D(DTOUT) Q G ASK");
		noErrorTest(" D @(+$P(LST,\",\",FLD))");
	}

	@Test
	public void testBeautify() {
		IToken l = lineTest(" S @A=\"S\"  S @H@(0)=3");
		l.beautify();
		String expected = " SET @A=\"S\"  SET @H@(0)=3";
		String actual = l.getStringValue();
		Assert.assertEquals(expected, actual);
	}
}