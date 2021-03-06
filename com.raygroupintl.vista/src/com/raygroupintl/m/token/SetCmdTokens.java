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

package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Nodes;
import com.raygroupintl.m.parsetree.SetCmdNodes;
import com.raygroupintl.parser.Token;

public final class SetCmdTokens {
	public static final class MSetCmd extends MCommand {
		public MSetCmd(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "SET";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new SetCmdNodes.SetCmd(postConditionNode, argumentNode);	
		}
	}
	
	public static final class MSingleAtomicSetCmd extends MSequence {
		public MSingleAtomicSetCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			MToken lhs = (MToken) this.get(0);
			Node lhsNode = lhs.getNode();
			MToken rhs = (MToken) this.get(2);
			if (rhs == null) {
				return new SetCmdNodes.IndirectAtomicSet(lhsNode);
			} else {
				Node rhsNode = rhs.getNode();
				return new SetCmdNodes.AtomicSet(lhsNode, rhsNode);
			}
		}		
	}

	public static final class MMultiAtomicSetCmd extends MSequence {
		public MMultiAtomicSetCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			List<Token> tokens = this.get(0).toList();			
			MDelimitedList lhs = (MDelimitedList) tokens.get(1);
			MToken rhs = (MToken) this.get(2);
			Nodes<Node> lhsNodes = NodeUtilities.getNodes(lhs, lhs.size());
			return new SetCmdNodes.MultiAtomicSet(lhsNodes, rhs.getNode());
		}		
	}
}
