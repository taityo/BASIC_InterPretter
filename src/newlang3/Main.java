package newlang3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

	public static void main(String[] args) {
		String name = "test.bs";
		if (args.length > 0) {
			name = args[0];
		}

		LexicalAnalyzer lexical_analyzer = null;
		try {
			File file = new File(name);
			FileReader fileReader = new FileReader(file);
			lexical_analyzer = new LexicalAnalyzerImple(fileReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				LexicalUnit lexical_unit = lexical_analyzer.get();

				System.out.println(lexical_unit.toString());
				if (lexical_unit.type == LexicalType.EOF) {
					break;
				}

			} catch (Exception e) {
				System.out.println(e);
				break;
			}

		}
	}

}
