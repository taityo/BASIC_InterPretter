package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringNode extends Node{
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.LITERAL, LexicalType.NAME));
	LexicalUnit stringval;

	StringNode(Environment e) {
		super(e);
		type = NodeType.STRING_CONSTANT;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new StringNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		lu = env.getInput().get();

		if(lu.getType() == LexicalType.LITERAL || lu.getType() == LexicalType.NAME) {
			stringval = lu;
		}else {
			return false;
		}

		return true;
	}

	public String toString(){
		return stringval.getValue().getSValue();
	}
}
