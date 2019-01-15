package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IntvalNode extends Node{

	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.INTVAL));
	LexicalUnit intval;

	IntvalNode(Environment e) {
		super(e);
		type = NodeType.INT_CONSTANT;
	}

	IntvalNode(int num, Environment e){
		this(e);
		intval = new LexicalUnit(LexicalType.INTVAL, new ValueImpl(Integer.toString(num), ValueType.INTEGER));
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new IntvalNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		lu = env.getInput().get();

		if(lu.getType() == LexicalType.INTVAL) {
			intval = lu;
		}else {
			return false;
		}

		return true;
	}

	public String toString(){
		return intval.getValue().getSValue();
	}
}

