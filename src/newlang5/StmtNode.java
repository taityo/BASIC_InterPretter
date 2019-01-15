package newlang5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StmtNode extends Node {

	Node child;
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.NAME, LexicalType.FOR, LexicalType.END));

	StmtNode(Environment e) {
		super(e);
		type = NodeType.STMT;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new StmtNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu, lu2 = null;
		lu = env.getInput().get();
		lu2 = env.getInput().get();
		Node handler = null;

		//System.out.println(lu);
		if (SubstNode.isMatch(lu.getType()) && lu2.getType() == LexicalType.EQ) {

			handler = SubstNode.getHandler(lu.getType(), env);
		} else if (CallFuncNode.isMatch(lu.getType())) {

			handler = CallFuncNode.getHandler(lu.getType(), env);
		} else if (ForNode.isMatch(lu.getType())) {

			handler = ForNode.getHandler(lu.getType(), env);
		} else if (EndNode.isMatch(lu.getType())) {

			handler = EndNode.getHandler(lu.getType(), env);
		} else {
			System.out.println("StmtNode Error");
			return false;
		}

		child = handler;
		env.getInput().unget(lu);
		env.getInput().unget(lu2);

		if (handler.Parse()) {
			return true;
		} else {
			System.out.println("StmtNode Error");
			return false;
		}
	}

	public String toString() {

		return child.toString() + ";";
	}

	public Value getValue() {
		child.getValue();
		return null;
	}
}
