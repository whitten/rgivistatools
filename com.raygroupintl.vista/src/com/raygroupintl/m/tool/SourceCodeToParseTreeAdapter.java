//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.m.tool;

import java.io.InputStream;

import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.parsergen.ParseException;
import com.raygroupintl.vista.tools.MRALogger;

public class SourceCodeToParseTreeAdapter implements ParseTreeSupply {
	private SourceCodeSupply sourceCodeSupply;
	private TFRoutine tokenFactory;
	private boolean inError = false;
	
	public SourceCodeToParseTreeAdapter(SourceCodeSupply sourceCodeSupply) {
		this.sourceCodeSupply = sourceCodeSupply;
	}
	
	private TFRoutine getTokenFactory() {
		try {
			MTFSupply mtf = MTFSupply.getInstance(MVersion.CACHE);
			TFRoutine tf = new TFRoutine(mtf);
			return tf;
		} catch (ParseException e) {
			this.inError = true;
			MRALogger.logError("Unable to load M parser definitions.");
			return null;
		}
		
	}
	
	@Override
	public Routine getParseTree(String routineName) {
		if (this.inError) return null;
		if (this.tokenFactory == null) {
			this.tokenFactory = this.getTokenFactory();
		}
		InputStream is = this.sourceCodeSupply.getStream(routineName);
		if (is != null) {
			MRoutineContent mrc = MRoutineContent.getInstance(routineName, is);
			MRoutine tokenizedSourceCode = this.tokenFactory.tokenize(mrc);
			Node node = tokenizedSourceCode.getNode();
			if (node instanceof Routine) {
				return (Routine) node;
			} else {
				this.inError = true;
				return null;
			}
		}
		return null;
	}
}