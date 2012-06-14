package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.Do;
import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.Goto;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.Token;

class CmdTokens {
	static class B extends MCommand {
		public B(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "BREAK";
		}			
	}
		
	static class C extends MCommand {
		public C(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "CLOSE";
		}			
	}
		
	static class D extends MCommand {
		public D(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "DO";
		}			

		@Override
		public Node getNode() {
			Node postConditionNode = this.getPostConditionNode();
			Node argumentNode = this.getArgumentNode();
			if (argumentNode == null) {
				return new DoBlock(postConditionNode);
			} else {
				Do result = new Do(postConditionNode, argumentNode);
				return result;
			}
		}
	}

	static class E extends MCommand {
		public E(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "ELSE";
		}			
	}

	static class F extends MCommand {
		public F(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "FOR";
		}			
	}

	static class G extends MCommand {
		public G(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "GOTO";
		}	
		
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new Goto(postConditionNode, argumentNode);	
		}
	}

	static class H extends MCommand {
		public H(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {
			Token argument = this.get(3);
			if (argument == null) {
				return "HALT";
			} else {
				return "HANG";
			}
		}			
	}

	static class I extends MCommand {
		public I(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "IF";
		}			
	}

	static class J extends MCommand {
		public J(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "JOB";
		}			
	}

	static class L extends MCommand {
		public L(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "LOCK";
		}			
	}

	static class M extends MCommand {
		public M(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "MERGE";
		}			
	}

	static class O extends MCommand {
		public O(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "OPEN";
		}			
	}


	static class Q extends MCommand {
		public Q(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "QUIT";
		}			
	}

	static class R extends MCommand {
		public R(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "READ";
		}			
	}

	static class TC extends MCommand {
		public TC(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "TCOMMIT";
		}			
	}

	static class TR extends MCommand {
		public TR(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "TRESTART";
		}			
	}

	static class TRO extends MCommand {
		public TRO(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "TROLLBACK";
		}			
	}

	static class TS extends MCommand {
		public TS(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "TSTART";
		}			
	}

	static class U extends MCommand {
		public U(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "USE";
		}			
	}

	static class W extends MCommand {
		public W(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "WRITE";
		}			
	}

	static class V extends MCommand {
		public V(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "VIEW";
		}			
	}

	static class X extends MCommand {
		public X(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "XECUTE";
		}			
	}

	static class Generic extends MCommand {
		public Generic(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return this.toValue().toString();
		}					
	}
}

