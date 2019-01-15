package newlang4;

public class ProgramNode extends Node {

	static Node stmt_list;

	public ProgramNode(NodeType type) {
		super(type);
	}

	public static Node getHandler(LexicalType type, Environment env) throws Exception {
		LexicalUnit lu;
		if (type == LexicalType.NL) {
			while (true) {
				lu = env.getInput().get();
				if(lu.getType() != LexicalType.NL) {
					type = lu.getType();
					env.getInput().unget(lu);
					break;
				}
			}
		}

		stmt_list = StmtListNode.getHandler(type, env);
		return stmt_list;
	}

	public boolean Parse() throws Exception {
		if (stmt_list.Parse()) {
			return true;
		} else {
			System.out.println("ProgramNode Error");
			return false;
		}
	}

	public String toString() {
		//ここで、構文解析木を表示できる必要がある
		return stmt_list.toString();
	}
}
