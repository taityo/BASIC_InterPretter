package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DoubleNode extends Node{
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.DOUBLEVAL));
	LexicalUnit doubleval;

	DoubleNode(Environment e) {
		super(e);
		type = NodeType.DOUBLE_CONSTANT;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new DoubleNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		lu = env.getInput().get();

		if(lu.getType() == LexicalType.DOUBLEVAL) {
			doubleval = lu;
		}else {
			return false;
		}

		return true;
	}

	public String toString(){
		return doubleval.getValue().getSValue();
	}
}
