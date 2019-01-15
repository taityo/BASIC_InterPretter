package newlang4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoopNode extends Node {
	List<Node> nodeList = new ArrayList<Node>();
	List<LexicalUnit> unitList = new ArrayList<LexicalUnit>();
	static Set<LexicalType> first = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.DO, LexicalType.WHILE));

	/*
	 * ループ処理毎に割り当てた番号
	 * 0, <WHILE> <cond> <NL> <stmt_list> <WEND> <NL>
	 * 1, <DO> <WHILE> <cond> <NL> <stmt_list> <LOOP> <NL>
	 * 2, <DO> <UNTIL> <cond> <NL> <stmt_list> <LOOP> <NL>
	 * 3, <DO> <NL> <stmt_list> <LOOP> <WHILE> <cond> <NL>
	 * 4, <DO> <NL> <stmt_list> <LOOP> <UNTIL> <cond> <NL>
	 */
	int processType = -1;
	int cond_num[] = {0, 0, 0, 1, 1};
	int stmtList_num[] = {1, 1,1,0,0};

	private LoopNode(Environment e) {
		super(e);
		type = NodeType.LOOP_BLOCK;
	}

	static boolean isMatch(LexicalType type) {
		return first.contains(type);
	}

	static Node getHandler(LexicalType t, Environment e) {
		if (!first.contains(t))
			return null;
		return new LoopNode(e);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu;
		Node handler = null;

		lu = env.getInput().get();
		unitList.add(lu);
		if (lu.getType() == LexicalType.WHILE) {
			//whileの場合の処理

			//condの解析
			lu = env.getInput().get();
			if (CondNode.isMatch(lu.getType())) {
				handler = CondNode.getHandler(lu.getType(), env);
				nodeList.add(handler);
				env.getInput().unget(lu);
			} else {
				System.out.println("LoopNode Error");
				return false;
			}
			if (!handler.Parse()) {
				System.out.println("LoopNode Error");
				return false;
			}

			//nlの解析
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() != LexicalType.NL) {
				System.out.println("LoopNode Error");
				return false;
			}

			//stmt_listの解析
			lu = env.getInput().get();
			if (StmtListNode.isMatch(lu.getType())) {
				handler = StmtListNode.getHandler(lu.getType(), env);
				nodeList.add(handler);
				env.getInput().unget(lu);
			}
			if (!handler.Parse()) {
				System.out.println("LoopNode Error");
				return false;
			}

			//wend の解析
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() != LexicalType.WEND) {
				System.out.println("LoopNode Error");
				return false;
			}

			//nlの解析
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() != LexicalType.NL) {
				System.out.println("LoopNode Error");
				return false;
			}

			processType = 0;
		} else if (lu.getType() != LexicalType.DO) {
			System.out.println("LoopNode Error");
			return false;
		}

		if (processType < 0) {
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() == LexicalType.WHILE) {
				processType = 1;
			} else if (lu.getType() == LexicalType.UNTIL) {
				processType = 2;
			} else if (lu.getType() != LexicalType.NL) {
				System.out.println("LoopNode Error");
				return false;
			}
		}

		if (processType > 0) {
			//processTypeが1,2の場合

			//condの解析
			lu = env.getInput().get();
			if (CondNode.isMatch(lu.getType())) {
				handler = CondNode.getHandler(lu.getType(), env);
				nodeList.add(handler);
				env.getInput().unget(lu);
			} else {
				System.out.println("LoopNode Error");
				return false;
			}
			if (!handler.Parse()) {
				System.out.println("LoopNode Error");
				return false;
			}

			//nlの解析
			/*lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() != LexicalType.NL) {
				System.out.println("LoopNode Error");
				return false;
			}*/

			//stmt_listの解析
			lu = env.getInput().get();
			if (StmtListNode.isMatch(lu.getType())) {
				handler = StmtListNode.getHandler(lu.getType(), env);
				nodeList.add(handler);
				env.getInput().unget(lu);
			} else {
				System.out.println("LoopNode Error");
				return false;
			}
			if (!handler.Parse()) {
				System.out.println("LoopNode Error");
				return false;
			}

			//loopの解析
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() != LexicalType.LOOP) {
				System.out.println("LoopNode Error");
				return false;
			}

			//nlの解析
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() != LexicalType.NL) {
				System.out.println("LoopNode Error");
				return false;
			}

		} else {
			//processTypeがまだ決まってない場合

			//stmt_listの解析
			lu = env.getInput().get();
			if (StmtListNode.isMatch(lu.getType())) {
				handler = StmtListNode.getHandler(lu.getType(), env);
				nodeList.add(handler);
				env.getInput().unget(lu);
			} else {
				System.out.println("LoopNode Error");
				return false;
			}
			if (!handler.Parse()) {
				System.out.println("LoopNode Error");
				return false;
			}

			//loopの解析
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() != LexicalType.LOOP) {
				System.out.println("LoopNode Error");
				return false;
			}

			//whileかuntil
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() == LexicalType.WHILE) {
				processType = 3;
			} else if (lu.getType() == LexicalType.UNTIL) {
				processType = 4;
			} else if (lu.getType() != LexicalType.NL) {
				System.out.println("LoopNode Error");
				return false;
			}

			//condの解析
			lu = env.getInput().get();
			if (CondNode.isMatch(lu.getType())) {
				handler = CondNode.getHandler(lu.getType(), env);
				nodeList.add(handler);
				env.getInput().unget(lu);
			} else {
				System.out.println("LoopNode Error");
				return false;
			}
			if (!handler.Parse()) {
				System.out.println("LoopNode Error");
				return false;
			}

			//nlの解析
			lu = env.getInput().get();
			unitList.add(lu);
			if (lu.getType() != LexicalType.NL) {
				System.out.println("LoopNode Error");
				return false;
			}
		}

		return true;
	}

	public String toString(){
		String res = "LOOP["
				+ nodeList.get(cond_num[processType]).toString()
				+ "[" + nodeList.get(stmtList_num[processType]).toString()
				+ "]";

		return res;
	}
}
