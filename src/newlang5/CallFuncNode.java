package newlang5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallFuncNode extends Node {
	List<Node> child = new ArrayList<Node>();
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.NAME));

	LexicalUnit name;
	Node expr_list;
	int functype;

	CallFuncNode(Environment e) {
		super(e);
		type = NodeType.FUNCTION_CALL;
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new CallFuncNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;

		//関数名の解析
		lu = env.getInput().get();
		if (lu.getType() == LexicalType.NAME) {
			name = lu;
		} else {
			System.out.println("CallFuncNode Error");
			return false;
		}

		//LPかexpr listの解析
		lu = env.getInput().get();
		if (lu.getType() == LexicalType.LP) {

			functype = 0;
		} else if (ExprListNode.isMatch(lu.getType())) {

			functype = 1;
			expr_list = ExprListNode.getHandler(lu.getType(), env);
			env.getInput().unget(lu);

			//解析
			if (!expr_list.Parse()) {
				System.out.println("CallFuncNode Error");
				return false;
			}
		} else {
			System.out.println("CallFuncNode Error");
			return false;
		}

		if (functype == 0) {
			//expr_listの解析
			lu = env.getInput().get();
			Node handler = null;
			if (ExprListNode.isMatch(lu.getType())) {
				handler = ExprListNode.getHandler(lu.getType(), env);
				expr_list = handler;
				env.getInput().unget(lu);
			} else {
				System.out.println("CallFuncNode Error");
				return false;
			}
			if (!handler.Parse()) {
				System.out.println("CallFuncNode Error");
				return false;
			}

			//RPの解析
			lu = env.getInput().get();
			if (lu.getType() != LexicalType.RP) {
				System.out.println("CallFuncNode Error");
				return false;
			}
		}

		return true;
	}

	public String toString() {
		String res;

		if (functype == 1) {
			res = name.getValue().getSValue() + "[" + expr_list.toString() + "]";
		} else {
			res = name.getValue().getSValue() + "(" + expr_list.toString() + ")";
		}
		return res;
	}

	public Value getValue() {
		Function func = this.env.getFunction(this.name.getValue().getSValue());

		//value listを取得
		Value value_list = this.expr_list.getValue();

		//関数の実行
		func.invoke((ValueList) value_list);

		return null;
	}
}
