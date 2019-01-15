package newlang5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BoolNode extends Node{
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.BOOLVAL));
	LexicalUnit boolval;

	BoolNode(Environment e) {
		super(e);
		type = NodeType.BOOL_CONSTANT;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new BoolNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		lu = env.getInput().get();

		if(lu.getType() == LexicalType.BOOLVAL) {
			boolval = lu;
		}else {
			return false;
		}

		return true;
	}

	public String toString(){
		return boolval.getValue().getSValue();
	}

	public Value getValue() {
		return boolval.getValue();
	}
}
