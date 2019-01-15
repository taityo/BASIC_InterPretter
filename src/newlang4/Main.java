package newlang4;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	        InputStreamReader fin = null;
	        LexicalAnalyzer lex;
	        LexicalUnit		first;
	        Environment		env;
	        Node			program;

	        System.out.println("basic parser");
	        fin = new InputStreamReader(new FileInputStream("test.bs"));
	        lex = new LexicalAnalyzerImpl(fin);

	        //とりあえず、字句解析を表示
			/*while (true) {
				try {
					LexicalUnit lu = lex.get();

					System.out.println(lu.toString());
					if (lu.type == LexicalType.EOF) {
						break;
					}

				} catch (Exception e) {
					System.out.println(e);
					break;
				}
			}*/

	        env = new Environment(lex);
	        first = lex.get();

	        env.getInput().unget(first);
	        program = ProgramNode.getHandler(first.getType(), env);
	        if (program != null && program.Parse()) {
	        	System.out.println(program.toString());
	        	//System.out.println("value = " + program.getValue());
	        }
	        else {
	        	//System.out.println(program.toString());
	        	System.out.println("syntax error");
	        }
	}

}
