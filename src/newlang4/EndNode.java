package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EndNode extends Node{
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.END));
	LexicalUnit end;

	EndNode(Environment e) {
		super(e);
		type = NodeType.END;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new EndNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		lu = env.getInput().get();
		end = lu;

		return true;
	}

	public String toString(){
		return "END";
	}
}
