package com.raygroupintl.m.tool.entry.fanin;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.MTestCommon;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.AccumulatingBlocksSupply;
import com.raygroupintl.m.tool.entry.fanin.EntryFanins;
import com.raygroupintl.m.tool.entry.fanin.FaninMark;
import com.raygroupintl.m.tool.entry.fanin.FaninTool;
import com.raygroupintl.m.tool.entry.fanin.MarkedAsFaninBRF;

public class FaninToolTest { 
	private void checkResult(EntryFanins fanins, String faninEntry, String[] faninNextEntries) {
		EntryId faninEntryId = EntryId.getInstance(faninEntry); 
		Assert.assertTrue(fanins.hasFaninEntry(faninEntryId));
		Set<EntryId> s = fanins.getFaninNextEntries(faninEntryId);
		Assert.assertNotNull(s);
		Assert.assertEquals(faninNextEntries.length, s.size());
		for (String faninNextEntry : faninNextEntries) {
			EntryId faninNextEntryId = EntryId.getInstance(faninNextEntry);
			Assert.assertTrue(s.contains(faninNextEntryId));
		}
	}
		
	@Test
	public void test() {
		String[] routineNames = {"FINROU00", 
				                 "FINROU01", 
				                 "FINROU02", 
				                 "FINROU03", 
				                 "FINROU04"};
		ParseTreeSupply pts = MTestCommon.getParseTreeSupply(routineNames);
		final EntryId entryId = new EntryId("FINROU00", "ADD");
		MarkedAsFaninBRF brf = new MarkedAsFaninBRF(entryId); 

		AccumulatingBlocksSupply<Fanout, FaninMark> blocksSupply = new AccumulatingBlocksSupply<Fanout, FaninMark>(pts, brf);
		
		FaninTool accumulator = new FaninTool(blocksSupply, false);
		for (String routineName : routineNames) {
			Routine r = pts.getParseTree(routineName);
			accumulator.addRoutine(r);
		}
		EntryFanins fanins = accumulator.getResult();
		Set<EntryId> s = fanins.getFaninEntries();
		Assert.assertEquals(15, s.size());
		this.checkResult(fanins, "FINROU01^FINROU01", new String[]{":4^FINROU01"});
		this.checkResult(fanins, ":4^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "ADDALL^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "CONDADD^FINROU01", new String[]{"CONDADD2^FINROU01"});
		this.checkResult(fanins, "CONDADD2^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "MULTAALL^FINROU01", new String[]{"MULTADD^FINROU00"});
		this.checkResult(fanins, "ADD^FINROU02", new String[]{"ADDALL^FINROU01"});
		this.checkResult(fanins, "ADD2^FINROU02", new String[]{"ADD^FINROU02"});
		this.checkResult(fanins, "MULTADD^FINROU00", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "FINROU03^FINROU03", new String[]{"TESTINDO^FINROU03"});
		this.checkResult(fanins, "TESTINDO^FINROU03", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "FINROU04^FINROU04", new String[]{"TESTINDO^FINROU04"});
		this.checkResult(fanins, "TESTINDO^FINROU04", new String[]{":5^FINROU04"});
		this.checkResult(fanins, ":5^FINROU04", new String[]{"OTHER^FINROU02"});
		this.checkResult(fanins, "OTHER^FINROU02", new String[]{"ADD^FINROU00"});
		
		FaninTool accumulator2 = new FaninTool(blocksSupply, true);
		for (String routineName : routineNames) {
			Routine r = pts.getParseTree(routineName);
			accumulator2.addRoutine(r);
		}
		EntryFanins fanins2 = accumulator2.getResult();
		Set<EntryId> s2 = fanins2.getFaninEntries();
		Assert.assertEquals(13, s2.size());
		this.checkResult(fanins2, "FINROU01^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins2, "ADDALL^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins2, "CONDADD^FINROU01", new String[]{"CONDADD2^FINROU01"});
		this.checkResult(fanins2, "CONDADD2^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins2, "MULTAALL^FINROU01", new String[]{"MULTADD^FINROU00"});
		this.checkResult(fanins2, "ADD^FINROU02", new String[]{"ADDALL^FINROU01"});
		this.checkResult(fanins2, "ADD2^FINROU02", new String[]{"ADD^FINROU02"});
		this.checkResult(fanins2, "MULTADD^FINROU00", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins2, "FINROU03^FINROU03", new String[]{"TESTINDO^FINROU03"});
		this.checkResult(fanins2, "TESTINDO^FINROU03", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins2, "FINROU04^FINROU04", new String[]{"TESTINDO^FINROU04"});
		this.checkResult(fanins2, "TESTINDO^FINROU04", new String[]{"OTHER^FINROU02"});
		this.checkResult(fanins2, "OTHER^FINROU02", new String[]{"ADD^FINROU00"});
	}
}