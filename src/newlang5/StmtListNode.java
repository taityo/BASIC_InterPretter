package newlang5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StmtListNode extends Node {
	List<Node> child = new ArrayList<Node>();
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.IF, LexicalType.DO, LexicalType.WHILE,
					LexicalType.NAME, LexicalType.FOR, LexicalType.END));

	StmtListNode(Environment e) {
		super(e);
		type = NodeType.STMT_LIST;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new StmtListNode(e);
	}

	public boolean Parse() throws Exception {

		while (true) {
			LexicalUnit lu;
			Node handler = null;

			lu = env.getInput().get();
			if (StmtNode.isMatch(lu.getType())) {
				handler = StmtNode.getHandler(lu.getType(), env);
			} else if (BlockNode.isMatch(lu.getType())) {
				handler = BlockNode.getHandler(lu.getType(), env);
			} else if (lu.getType() == LexicalType.NL) {
				continue;
			} else {

				if (BlockNode.isEnd(lu.getType()) || ForNode.isEnd(lu.getType())) {
					env.getInput().unget(lu);
					return true;
				}
				if (lu.getType() == LexicalType.EOF)
					break;

				System.out.println("StmtListNode Error");
				return false;
			}

			child.add(handler);
			env.getInput().unget(lu);

			if (!handler.Parse()) {
				System.out.println("StmtListNode Error");
				return false;
			}

			//ENDだったら終了
			if (lu.getType() == LexicalType.END)
				break;

		}

		return true;
	}

	public String toString() {
		String str = "";

		for (int i = 0; i < child.size(); i++) {
			str += child.get(i).toString();
		}

		return str;
	}

	public Value getValue() {

		for (int i = 0; i < child.size(); i++) {
			child.get(i).getValue();
		}

		return null;
	}
}
