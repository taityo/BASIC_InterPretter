package newlang5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SubstNode extends Node{

	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.NAME));

	LexicalUnit name, eq;
	Node expr;

	SubstNode(Environment e) {
		super(e);
		type = NodeType.ASSIGN_STMT;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new SubstNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		Node handler = null;

		//変数の解析
		lu = env.getInput().get();
		if(lu.getType() == LexicalType.NAME) {
			name = lu;
		}else {
			System.out.println("SubstNode Error");
			return false;
		}

		//=の解析
		lu = env.getInput().get();
		if(lu.getType() == LexicalType.EQ) {
			eq = lu;
		}else {
			System.out.println("SubstNode Error");
			return false;
		}

		//exprの解析
		lu = env.getInput().get();
		if(ExprNode.isMatch(lu.getType())) {
			handler = ExprNode.getHandler(lu.getType(), env);
			expr = handler;
			env.getInput().unget(lu);
		}else {
			System.out.println("SubstNode Error");
			return false;
		}
		if(handler.Parse()) {
			return true;
		}else {
			System.out.println("SubstNode Error");
			return false;
		}
	}

	public String toString(){
		return name.getValue().getSValue() + "[" + expr.toString() + "]";
	}

	public Value getValue() {
		Variable v = this.env.getVariable(this.name.getValue().getSValue());

		//値を代入
		Value value = this.expr.getValue();
		v.setValue(value);

		return value;
	}
}
