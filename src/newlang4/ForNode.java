package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ForNode extends Node{

	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.FOR));
	static Set<LexicalType> end = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.NEXT));
	LexicalUnit forUnit, toUnit, stepUnit, nlUnit, nextUnit, name;
	Node subst, expr, expr2, stmt_list;

	ForNode(Environment e) {
		super(e);
		type = NodeType.FOR_STMT;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static boolean isEnd(LexicalType type) {
		return end.contains(type);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new ForNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		Node handler = null;

		//forの解析
		lu = env.getInput().get();
		if(lu.getType() == LexicalType.FOR) {
			forUnit = lu;
		}else {
			System.out.println("ForNode Error");
			return false;
		}

		//substの解析
		lu = env.getInput().get();
		if(SubstNode.isMatch(lu.getType())) {
			subst = SubstNode.getHandler(lu.getType(), env);
			env.getInput().unget(lu);
		}else {
			System.out.println("ForNode Error");
			return false;
		}
		if(!subst.Parse()) {
			System.out.println("ForNode Error");
			return false;
		}

		//toの解析
		lu = env.getInput().get();
		if(lu.getType() == LexicalType.TO) {
			toUnit = lu;
		}else {
			System.out.println("ForNode Error");
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
		if(!expr.Parse()) {
			System.out.println("ForNode Error");
			return false;
		}

		//stepの解析
		lu = env.getInput().get();
		if(lu.getType() == LexicalType.STEP) {
			stepUnit = lu;
		}else {
			System.out.println("ForNode Error");
			return false;
		}

		//expr2(増量)の解析
		lu = env.getInput().get();
		if(ExprNode.isMatch(lu.getType())) {
			handler = ExprNode.getHandler(lu.getType(), env);
			expr2 = handler;
			env.getInput().unget(lu);
		}else {
			System.out.println("SubstNode Error");
			return false;
		}
		if(!expr2.Parse()) {
			System.out.println("SubstNode Error");
			return false;
		}

		//stmt_listの解析
		lu = env.getInput().get();
		if(StmtListNode.isMatch(lu.getType())) {
			stmt_list = StmtListNode.getHandler(lu.getType(), env);
			env.getInput().unget(lu);
		}else {
			System.out.println("ForNode Error");
			return false;
		}
		if(!stmt_list.Parse()) {
			System.out.println("ForNode Error");
			return false;
		}

		//nextの解析
		lu = env.getInput().get();
		if(lu.getType() == LexicalType.NEXT) {
			nextUnit = lu;
		}else {
			System.out.println("ForNode Error");
			return false;
		}

		//NAMEの解析
		lu = env.getInput().get();
		if(lu.getType() == LexicalType.NAME) {
			name = lu;
		}else {
			System.out.println("ForNode Error");
			return false;
		}

		return true;
	}

	public String toString(){
		String str = "FOR[TO[" +  subst.toString() + ":"
				+ expr.toString() + ":" + expr2.toString() + "][" + stmt_list.toString()
				+ "]]";
		return str;
	}
}
