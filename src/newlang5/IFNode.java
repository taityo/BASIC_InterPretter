package newlang5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IFNode extends Node{

	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.IF));
	List<LexicalUnit> unitList = new ArrayList<LexicalUnit>();
	List<Node> nodeList = new ArrayList<Node>();

	/*
	 * 処理の内容ごとに番号を設定した
	 * 0、<if_prefix> <stmt> <NL>
	 * 1、<if_prefix> <stmt> <ELSE> <stmt> <NL>
	 * 2、<if_prefix> <NL> <stmt_list> <else_block> <ENDIF> <NL>
	 */
	int processType = -1;

	private IFNode(Environment e){
		super(e);
	}

	static boolean isMatch(LexicalType type) {
		return first.contains(type);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if(!first.contains(t))return null;
		return new IFNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		Node handler = null;

		//ifの解析
		lu = env.getInput().get();
		unitList.add(lu);
		if(lu.getType() != LexicalType.IF) {
			System.out.println("IFNode Error");
			return false;
		}

		//condの解析
		lu = env.getInput().get();
		if(CondNode.isMatch(lu.getType())) {
			handler = CondNode.getHandler(lu.getType(), env);
			nodeList.add(handler);
			env.getInput().unget(lu);
		}else {
			System.out.println("IFNode Error");
			return false;
		}
		if(!handler.Parse()) {
			System.out.println("IFNode Error");
			return false;
		}

		//thenの解析
		lu = env.getInput().get();
		unitList.add(lu);
		if(lu.getType() != LexicalType.THEN) {
			System.out.println("IFNode Error");
			return false;
		}

		//stmtの解析
		lu = env.getInput().get();
		if(lu.getType() != LexicalType.NL) {

			if(StmtNode.isMatch(lu.getType())) {
				handler = StmtNode.getHandler(lu.getType(), env);
				nodeList.add(handler);
				env.getInput().unget(lu);
			}else {
				System.out.println("IFNode Error");
				return false;
			}
			if(!handler.Parse()) {
				System.out.println("IFNode Error");
				return false;
			}
			nodeList.add(handler);

		}else {
			unitList.add(lu);
			processType = 2;
		}

		lu = env.getInput().get();
		if(processType < 0) {
			unitList.add(lu);
			if(lu.getType() == LexicalType.NL) {
				processType = 0;

			}else if(lu.getType() == LexicalType.ELSE) {
				processType = 1;

				//stmtの解析
				lu = env.getInput().get();
				if(StmtNode.isMatch(lu.getType())) {
					handler = StmtNode.getHandler(lu.getType(), env);
					nodeList.add(handler);
					env.getInput().unget(lu);
				}else {
					System.out.println("IFNode Error");
					return false;
				}
				if(!handler.Parse()) {
					System.out.println("IFNode Error");
					return false;
				}

				//nlの解析
				lu = env.getInput().get();
				unitList.add(lu);
				if(lu.getType() != LexicalType.NL) {
					System.out.println("IFNode Error");
					return false;
				}
			}else {
				System.out.println("IFNode Error");
				return false;
			}

		}else {
			//processType = 2の場合

			//stmt_listの解析
			if(StmtListNode.isMatch(lu.getType())) {
				handler = StmtListNode.getHandler(lu.getType(), env);
				nodeList.add(handler);
				env.getInput().unget(lu);
			}else {
				System.out.println("IFNode Error");
				return false;
			}
			if(!handler.Parse()) {
				System.out.println("IFNode Error");
				return false;
			}

			//else ifを無限に解析
			//end ifが来るまで
			while(true) {

				lu = env.getInput().get();
				unitList.add(lu);
				if(lu.getType() == LexicalType.ELSE) {
					//elseの時の処理

					//nlの解析
					lu = env.getInput().get();
					unitList.add(lu);
					if(lu.getType() != LexicalType.NL) {
						System.out.println("IFNode Error");
						return false;
					}

					//stmt_listの解析
					lu = env.getInput().get();
					if(StmtListNode.isMatch(lu.getType())) {
						handler = StmtListNode.getHandler(lu.getType(), env);
						nodeList.add(handler);
						env.getInput().unget(lu);
					}else {
						System.out.println("IFNode Error");
						return false;
					}
					if(!handler.Parse()) {
						System.out.println("IFNode Error");
						return false;
					}

					//end ifの解析
					//endはstmt listで確認されるため、ifだけを確認
					/*lu = env.getInput().get();
					if(lu.getType() != LexicalType.IF) {
						System.out.println("IFNode Error");
						return false;
					}*/

					break;
				}else if(lu.getType() == LexicalType.ELSEIF) {
					//else ifの時の処理

					//condの解析
					lu = env.getInput().get();
					if(CondNode.isMatch(lu.getType())) {
						handler = CondNode.getHandler(lu.getType(), env);
						nodeList.add(handler);
						env.getInput().unget(lu);
					}else {
						System.out.println("IFNode Error");
						return false;
					}
					if(!handler.Parse()) {
						System.out.println("IFNode Error");
						return false;
					}

					//thenの解析
					lu = env.getInput().get();
					unitList.add(lu);
					if(lu.getType() != LexicalType.THEN) {
						System.out.println("IFNode Error");
						return false;
					}

					//nlの解析
					lu = env.getInput().get();
					unitList.add(lu);
					if(lu.getType() != LexicalType.NL) {
						System.out.println("IFNode Error");
						return false;
					}

					//stmt_listの解析
					lu = env.getInput().get();
					if(StmtListNode.isMatch(lu.getType())) {
						handler = StmtListNode.getHandler(lu.getType(), env);
						nodeList.add(handler);
						env.getInput().unget(lu);
					}else {
						System.out.println("IFNode Error");
						return false;
					}
					if(!handler.Parse()) {
						System.out.println("IFNode Error");
						return false;
					}

					//nlの解析
					/*lu = env.getInput().get();
					if(lu.getType() != LexicalType.NL) {
						System.out.println("IFNode Error");
						return false;
					}*/
				}else {
					System.out.println("IFNode Error");
					return false;
				}

			}

			//endifの解析
			lu = env.getInput().get();
			unitList.add(lu);
			if(lu.getType() != LexicalType.ENDIF) {
				System.out.println("IFNode Error");
				return false;
			}

			//nlの解析
			lu = env.getInput().get();
			unitList.add(lu);
			if(lu.getType() != LexicalType.NL) {
				System.out.println("IFNode Error");
				return false;
			}
		}

		return true;
	}

	public String toString() {
		String res = null;

		if(processType == 0) {
			res = "IF[" + nodeList.get(0).toString()
					+ "[" + nodeList.get(1).toString() + "]]";
		}

		if(processType == 1) {
			res = "IF[" + nodeList.get(0).toString()
					+ "[" + nodeList.get(1).toString() + "]]"
					+ "ELSE [" + nodeList.get(3).toString() + "]";
		}

		if(processType == 2) {
			res = "IF[" + nodeList.get(0).toString()
					+ "[" + nodeList.get(1).toString() + "]]";

			//else ifをとりだしつづける
			LexicalUnit lu;
			int i = 3, j = 2;
			while(true) {
				lu = unitList.get(i);

				if(lu.getType() == LexicalType.ELSE) {
					res += "ELSE[" + nodeList.get(j).toString() + "]";
					break;
				}else {
					res += "ELSEIF[" + nodeList.get(j).toString()
							+ "[" + nodeList.get(j+1).toString() + "]]";
				}
				i += 2;
				j += 2;
			}
		}

		return res;
	}

	public Value getValue() {
		//if, elseif関係なく、条件文があってたらブロック内を実行して終了
		//else が来たら、ブロック内を実行して終了
		String res = null;

		if(processType == 2) {
			//ifブロック
			if(nodeList.get(0).getValue().getBValue()) {
				nodeList.get(1).getValue();
				return null;
			}

			//else ifをとりだしつづける
			LexicalUnit lu;
			int i = 3, j = 2;
			while(true) {
				lu = unitList.get(i);

				if(lu.getType() == LexicalType.ELSE) {
					nodeList.get(j).getValue();
					break;
				}else {
					if(nodeList.get(j).getValue().getBValue()) {
						nodeList.get(j+1).getValue();
						break;
					}
				}
				i += 2;
				j += 2;
			}
		}

		return null;
	}
}
