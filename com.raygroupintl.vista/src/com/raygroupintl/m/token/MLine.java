package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.m.parsetree.ParentNode;
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.Tokens;

public class MLine extends MSequence {
	String tagName = "";
	int index = 0;

	public MLine(int length) {
		super(length);
	}

	public MLine(SequenceOfTokens<MToken> tokens) {
		super(tokens);
	}

	public String getTag() {
		Token tag = this.getToken(0);
		if (tag == null) {
			return null;
		} else {
			return tag.toValue().toString();
		}
	}
	
	public String[] getParameters() {
		Tokens<MToken> paramTokens = this.getTokens(1, 1);
		if (paramTokens != null) {
			int length = paramTokens.size();
			if (length > 0) {
				String[] result = new String[paramTokens.size()];
				int i=0;
				for (Token t : paramTokens.toLogicalIterable()) {
					result[i] = t.toValue().toString();
					++i;
				}
				return result;
			}
		}
		return null;
	}
	
	public int getLevel() {
		int level = 0;
		Token levelToken = this.getToken(3);
		if (levelToken != null) {
			TextPiece levelTokenValue = levelToken.toValue();
			return levelTokenValue.count('.');
		}		
		return level;
	}

	public void setIdentifier(String tagName, int index) {
		this.tagName = tagName;
		this.index = index;
	}

	public String getTagName() {
		return this.tagName;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public Line getErrorNode(ErrorNode errorNode) {
		Line result = new Line(this.tagName, this.index, this.getLevel());
		NodeList<Node> nodes = new NodeList<Node>(1);
		nodes.add(errorNode);
		result.setNodes(null);
		return result;
	}
	
	public Line getAsErrorNode(ErrorNode errorNode) {
		Line result = new Line(this.tagName, this.index, this.getLevel());
		NodeList<Node> nodes = new NodeList<Node>(1);
		nodes.add(errorNode);
		result.setNodes(nodes);
		return result;
	}
	
	@Override
	public Line getNode() {
		int level = this.getLevel();
		Line result = new Line(this.tagName, this.index, level);
		ParentNode currentParent = result;
		Tokens<MToken> cmds = this.getTokens(4);
		if (cmds != null) {
			NodeList<Node> nodes = null;
			for (MToken t : cmds.toLogicalIterable()) {
				Node node = t.getNode();
				if (node != null) {
					if (nodes == null) nodes = new NodeList<Node>(cmds.size());
					currentParent = node.addSelf(currentParent, nodes, level);
				}
			}
			if ((nodes != null) && (nodes.size() > 0)) {
				currentParent.setNodes(nodes.copy());
			}
		}
		return result;
	}
}
