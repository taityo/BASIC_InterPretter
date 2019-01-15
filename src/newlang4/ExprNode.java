package newlang4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExprNode extends Node {
	List<Node> operand = new ArrayList<Node>();
	List<LexicalUnit> operata = new ArrayList<LexicalUnit>();
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(
					LexicalType.LP, LexicalType.NAME, LexicalType.INTVAL,
					LexicalType.DOUBLEVAL, LexicalType.LITERAL, LexicalType.SUB));
	static Map<LexicalType, String> ope = new HashMap<LexicalType, String>() {
		{
			put(LexicalType.ADD, "+");
			put(LexicalType.SUB, "-");
			put(LexicalType.MUL, "*");
			put(LexicalType.DIV, "/");
		}
	};

	private ExprNode(Environment e) {
		super(e);
		type = NodeType.EXPR;
	}

	private ExprNode(Node expr1, LexicalUnit ope, Node expr2, Environment e) {
		super(e);
		type = NodeType.EXPR;

		operand.add(expr1);
		operata.add(ope);
		operand.add(expr2);
	}

	static boolean isMatch(LexicalType t) {
		return first.contains(t);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new ExprNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		Node handler = null;

		int i = 0;
		while (true) {
			lu = env.getInput().get();

			if (i % 2 == 0) {
				//オペランド

				if (lu.getType() == LexicalType.LP) {
					//カッコ

					lu = env.getInput().get();
					if (ExprNode.isMatch(lu.getType())) {
						handler = ExprNode.getHandler(lu.getType(), env);
						env.getInput().unget(lu);
					} else {
						System.out.println("ExprNode Error");
						return false;
					}
					if (!handler.Parse()) {
						System.out.println("ExprNode Error");
						return false;
					}

				} else if (lu.getType() == LexicalType.NAME) {
					//func, name
					//2つ見て、判断する
					LexicalUnit lu2 = env.getInput().get();
					env.getInput().unget(lu);
					env.getInput().unget(lu2);

					if (lu2.getType() == LexicalType.LP) {
						//func
						if (CallFuncNode.isMatch(lu.getType())) {
							handler = CallFuncNode.getHandler(lu.getType(), env);
						} else {
							System.out.println("ExprNode Error");
							return false;
						}
						if (!handler.Parse()) {
							System.out.println("ExprNode Error");
							return false;
						}

					} else {
						//name
						if (StringNode.isMatch(lu.getType())) {
							handler = StringNode.getHandler(lu.getType(), env);
						} else {
							System.out.println("ExprNode Error");
							return false;
						}
						if (!handler.Parse()) {
							System.out.println("ExprNode Error");
							return false;
						}

					}

				} else if (lu.getType() == LexicalType.LITERAL) {
					//literal
					env.getInput().unget(lu);
					if (StringNode.isMatch(lu.getType())) {
						handler = StringNode.getHandler(lu.getType(), env);
					} else {
						System.out.println("ExprNode Error");
						return false;
					}
					if (!handler.Parse()) {
						System.out.println("ExprNode Error");
						return false;
					}

				} else if (lu.getType() == LexicalType.INTVAL) {
					//intval
					env.getInput().unget(lu);
					if (IntvalNode.isMatch(lu.getType())) {
						handler = IntvalNode.getHandler(lu.getType(), env);
					} else {
						System.out.println("ExprNode Error");
						return false;
					}
					if (!handler.Parse()) {
						System.out.println("ExprNode Error");
						return false;
					}

				} else if (lu.getType() == LexicalType.DOUBLEVAL) {
					//double
					env.getInput().unget(lu);
					if (DoubleNode.isMatch(lu.getType())) {
						handler = DoubleNode.getHandler(lu.getType(), env);
					} else {
						System.out.println("ExprNode Error");
						return false;
					}
					if (!handler.Parse()) {
						System.out.println("ExprNode Error");
						return false;
					}

				} else if (lu.getType() == LexicalType.SUB) {
					//マイナス
					//0-aという計算をしていることにする
					Node expr1;
					Node expr2;

					//値0が入っているexpr1を定義
					expr1 = new IntvalNode(0, env);

					//exprを解析
					LexicalUnit lu2 = env.getInput().get();
					if (ExprNode.isMatch(lu2.getType())) {
						expr2 = ExprNode.getHandler(lu2.getType(), env);
						env.getInput().unget(lu2);
					} else {
						System.out.println("ExprNode Error");
						return false;
					}
					if (!expr2.Parse()) {
						System.out.println("ExprNode Error");
						return false;
					}

					//0-exprが入っているnew exprを作成
					handler = new ExprNode(expr1, lu, expr2, env);
				} else {
					//それ以外が来たら、文法がおかしい
					return false;
				}

				operand.add(handler);
			} else {
				//オペレータ

				if (ope.containsKey(lu.getType())) {
					operata.add(lu);
				} else {
					if (lu.getType() != LexicalType.NL) {
						env.getInput().unget(lu);
					}
					break;
				}
			}

			i++;
		}

		//計算の順番を整える.それぞれの演算をまとめる.
		//オペランド、オペレータの順番
		//コンストラクタ2で作られたexprの事は考えなくていい

		//*と/の演算
		for (i = operata.size() - 1; i >= 0; i--) {
			lu = operata.get(i);
			LexicalType lt = lu.getType();

			if (lt == LexicalType.MUL || lt == LexicalType.DIV) {

				//新しいexprを作成
				Node expr1 = operand.get(i);
				Node expr2 = operand.get(i + 1);
				Node new_expr = new ExprNode(expr1, lu, expr2, env);

				//いらないオペランドとオペレータを削除
				operand.remove(i);
				operand.remove(i);
				operata.remove(i);

				//作ったオペランドを追加
				operand.set(i, new_expr);

				//インデックスを調整
				i--;
			}
		}

		//+と-の演算
		while (true) {
			//オペランドが2つになったら終了
			if (operand.size() <= 2)
				break;

			//新しいexprを作成
			i = operata.size() - 1;
			lu = operata.get(i);
			Node expr1 = operand.get(i);
			Node expr2 = operand.get(i + 1);
			Node new_expr = new ExprNode(expr1, lu, expr2, env);

			//いらないオペランドとオペレータを削除
			operand.remove(i);
			operand.remove(i);
			operata.remove(i);

			//作ったオペランドを追加
			operand.set(i, new_expr);

		}

		return true;

	}

	public String toString() {
		//マイナスのパターンを書いていない
		if (operand.size() > 1) {
			return ope.get(operata.get(0).getType()) + "[" + operand.get(0).toString() + "," + operand.get(1) + "]";
		} else {
			return operand.get(0).toString();
		}
	}
}
