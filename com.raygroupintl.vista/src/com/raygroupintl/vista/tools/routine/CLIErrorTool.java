//---------------------------------------------------------------------------
//Copyright 2013 PwC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.vista.tools.routine;

import java.io.IOException;
import java.util.List;

import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.RoutineToolParams;
import com.raygroupintl.m.tool.routine.error.ErrorTool;
import com.raygroupintl.m.tool.routine.error.ErrorWithLineIndex;
import com.raygroupintl.m.tool.routine.error.ErrorsByRoutine;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;

public class CLIErrorTool extends CLIResultsByRoutineLabelTool<ErrorWithLineIndex, List<ErrorWithLineIndex>> {
	public CLIErrorTool(CLIParams params) {
		super(params);
	}
	
	@Override
	public void run() throws IOException {
		Terminal t = CLIParamsAdapter.getTerminal(this.params);
		if (t != null) {
			RoutineToolParams p = CLIRTParamsAdapter.toMRoutineToolParams(this.params);	
			ErrorTool tool = new ErrorTool(p);
			MRoutineToolInput input = CLIRTParamsAdapter.toMRoutineInput(this.params);
			ErrorsByRoutine result = tool.getResult(input);
			this.write(result, t);
		}
	}
}
