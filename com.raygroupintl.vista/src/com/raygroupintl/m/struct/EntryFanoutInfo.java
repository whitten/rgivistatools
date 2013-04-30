package com.raygroupintl.m.struct;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.entry.quitype.QuitTypeState;

public class EntryFanoutInfo {
	
	//private int lineNumber; //TODO: not currently set, need to enhance framework to capture this
	//private int startCmdPos;
	//private int endCmdPos;
	
	private boolean valid;
	private LineLocation lineLocation;
	private FanoutTypeENUM fanoutType;
	private EntryId fanoutTo;
	private QuitTypeState returnType;
	private boolean fanoutExists;
	
	public EntryFanoutInfo(LineLocation lineLocation,
			FanoutTypeENUM fanoutType, EntryId fanoutTo) {
		super();
		this.lineLocation = lineLocation;
		this.fanoutType = fanoutType;
		this.fanoutTo = fanoutTo;
	}
	
	public LineLocation getLineLocation() {
		return lineLocation;
	}

	public void setLineLocation(LineLocation lineLocation) {
		this.lineLocation = lineLocation;
	}

	public FanoutTypeENUM getFanoutType() {
		return fanoutType;
	}

	public void setFanoutType(FanoutTypeENUM fanoutType) {
		this.fanoutType = fanoutType;
	}

	public EntryId getFanoutTo() {
		return fanoutTo;
	}

	public void setFanoutTo(EntryId fanoutTo) {
		this.fanoutTo = fanoutTo;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public QuitTypeState getReturnType() {
		return returnType;
	}

	public void setReturnType(QuitTypeState returnType) {
		this.returnType = returnType;
	}

	public boolean isFanoutExists() {
		return fanoutExists;
	}

	public void setFanoutExists(boolean fanoutExists) {
		this.fanoutExists = fanoutExists;
	}

}
