package newlang5;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		InputStreamReader fin = null;
		LexicalAnalyzer lex;
		LexicalUnit first;
		Environment env;
		Node prog;

		System.out.println("basic parser");
		try {
			fin = new InputStreamReader(new FileInputStream("test.bs"));
		} catch (Exception e) {
			System.out.println("file not found");
			System.exit(-1);
		}
		lex = new LexicalAnalyzerImpl(fin);
		env = new Environment(lex);
		first = lex.get();
		env.getInput().unget(first);

		prog = ProgramNode.getHandler(first.getType(), env);
		if (prog != null && prog.Parse()) {
			//System.out.println(prog.toString());
			prog.getValue();
		} else {
			System.out.println("syntax error");
		}
	}

}
