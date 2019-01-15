package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExprListNode extends Node{

	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.LP, LexicalType.SUB, LexicalType.NAME,
					LexicalType.INTVAL, LexicalType.DOUBLEVAL, LexicalType.LITERAL));
	Node expr, expr_list;
	LexicalUnit comma = null;

	ExprListNode(Environment e) {
		super(e);
		type = NodeType.EXPR_LIST;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new ExprListNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		Node handler = null;

		//exprの解析
		lu = env.getInput().get();
		if(ExprNode.isMatch(lu.getType())) {
			expr = ExprNode.getHandler(lu.getType(), env);
			env.getInput().unget(lu);
		}else {
			System.out.println("ExprListNode Error");
			return false;
		}
		if(!expr.Parse()) {
			System.out.println("ExprListNode Error");
			return false;
		}

		//コンマの解析
		lu = env.getInput().get();
		if(lu.getType() == LexicalType.COMMA) {
			comma = lu;
		}else {
			env.getInput().unget(lu);
			return true;
		}

		//expr_listの解析
		lu = env.getInput().get();
		if(ExprListNode.isMatch(lu.getType())){
			expr_list = ExprListNode.getHandler(lu.getType(), env);
			env.getInput().unget(lu);
		}else {
			System.out.println("ExprListNode Error");
			return false;
		}
		if(!expr_list.Parse()) {
			System.out.println("ExprListNode Error");
			return false;
		}

		return true;
	}

	public String toString(){
		String res = expr.toString();

		if(comma != null) {
			res += "," + expr_list.toString();
		}

		return res;
	}
}
