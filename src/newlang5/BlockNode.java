package newlang5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlockNode extends Node{

	Node child;
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.DO, LexicalType.IF, LexicalType.WHILE));
	static Set<LexicalType> end = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.LOOP, LexicalType.WEND, LexicalType.ELSE,
					LexicalType.ELSEIF, LexicalType.ENDIF));

	private BlockNode(Environment e){
		super(e);
		type = NodeType.BLOCK;
	}

	static boolean isMatch(LexicalType type) {
		return first.contains(type);
	}

	static boolean isEnd(LexicalType type) {
		return end.contains(type);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if(!first.contains(t))return null;
		return new BlockNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		lu = env.getInput().get();
		Node handler = null;

		if (IFNode.isMatch(lu.getType())) {
			handler = IFNode.getHandler(lu.getType(), env);

		}else if(LoopNode.isMatch(lu.getType())) {
			handler = LoopNode.getHandler(lu.getType(), env);

		}else {
			System.out.println("BlockNode Error");
			return false;
		}

		child = handler;
		env.getInput().unget(lu);
		if(handler.Parse()) {
			return true;
		}else {
			System.out.println("BlockNode Error");
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
