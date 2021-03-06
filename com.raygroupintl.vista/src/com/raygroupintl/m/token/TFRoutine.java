package com.raygroupintl.m.token;

import java.io.IOException;
import java.nio.file.Path;

import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.ObjectSupply;

public class TFRoutine {
	private TokenFactory tfLine;
	private ObjectSupply mAdapterSupply = new MObjectSupply();
	
	public TFRoutine(MTFSupply supply) {
		this.tfLine = supply.line;
	}
	
	public static MLine recoverFromError(String line, SyntaxErrorException e) {
		Token error = new MSyntaxError(0, new StringPiece(line), 0);
		MSequence result = new MSequence(5);
		result.addToken(error);
		for (int i=0; i<4; ++i) result.addToken(null);
		return new MLine(result);
	}
	
	public MRoutine tokenize(MRoutineContent content) {
		String name = content.getName();
		MRoutine result = new MRoutine(name);
		int index = 0;
		String tagName = "";
		for (String line : content.getLines()) {
			MLine tokens = null;
			try {
				Text text = new Text(line);
				tokens = (MLine) this.tfLine.tokenize(text, this.mAdapterSupply);
			} catch (SyntaxErrorException e) {
				tokens = recoverFromError(line, e);
			}
			String lineTagName = tokens.getTag();
			if (lineTagName != null) {
				tagName = lineTagName;
				index = 0;
			}
			tokens.setIdentifier(tagName, index);
			result.add(tokens);
			++index;
		}		
		return result;
	}
	
	public MRoutine tokenize(Path path) throws IOException,  SyntaxErrorException {
		MRoutineContent content = MRoutineContent.getInstance(path);					
		MRoutine r = this.tokenize(content);
		return r;
	}
}
