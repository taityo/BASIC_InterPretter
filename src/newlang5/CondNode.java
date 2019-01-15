package newlang5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CondNode extends Node {

	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.LP, LexicalType.SUB, LexicalType.NAME,
					LexicalType.LITERAL, LexicalType.INTVAL, LexicalType.DOUBLEVAL));
	static Map<LexicalType, String> conds = new HashMap<LexicalType, String>() {
		{
			put(LexicalType.EQ, "=");
			put(LexicalType.GT, ">");
			put(LexicalType.LT, "<");
			put(LexicalType.GE, ">=");
			put(LexicalType.LE, "<=");
			put(LexicalType.NE, "<>");
		}
	};
	Node expr, expr2;
	LexicalUnit cond;

	CondNode(Environment e) {
		super(e);
		type = NodeType.COND;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new CondNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		Node handler = null;

		lu = env.getInput().get();
		if (ExprNode.isMatch(lu.getType())) {
			expr = ExprNode.getHandler(lu.getType(), env);
			env.getInput().unget(lu);
		} else {
			System.out.println("CondNode Error");
			return false;
		}
		if (!expr.Parse()) {
			System.out.println("CondNode Error");
			return false;
		}

		//condの解析
		lu = env.getInput().get();
		if (conds.containsKey(lu.getType())) {
			cond = lu;
		} else {
			System.out.println("CondNode Error");
			return false;
		}

		//exprの解析
		lu = env.getInput().get();
		if (ExprNode.isMatch(lu.getType())) {
			expr2 = ExprNode.getHandler(lu.getType(), env);
			env.getInput().unget(lu);
		} else {
			System.out.println("CondNode Error");
			return false;
		}
		if (!expr2.Parse()) {
			System.out.println("CondNode Error");
			return false;
		}

		return true;
	}

	public String toString() {
		String str = conds.get(cond.getType()) + "[" + expr.toString() + ":" + expr2.toString() + "]";

		return str;
	}

	public Value getValue() {
		Boolean flag = null;

		if(conds.get(cond.getType()) == "=") {
			flag = expr.getValue().getDValue() == expr2.getValue().getDValue();
		}else if(conds.get(cond.getType()) == ">") {
			flag = expr.getValue().getDValue() > expr2.getValue().getDValue();
		}else if(conds.get(cond.getType()) == "<") {
			flag = expr.getValue().getDValue() < expr2.getValue().getDValue();
		}else if(conds.get(cond.getType()) == ">=") {
			flag = expr.getValue().getDValue() >= expr2.getValue().getDValue();
		}else if(conds.get(cond.getType()) == "<=") {
			flag = expr.getValue().getDValue() <= expr2.getValue().getDValue();
		}else if(conds.get(cond.getType()) == "<>") {
			flag = expr.getValue().getDValue() != expr2.getValue().getDValue();
		}

		return new ValueImpl(Boolean.toString(flag), ValueType.BOOL);
	}
}
