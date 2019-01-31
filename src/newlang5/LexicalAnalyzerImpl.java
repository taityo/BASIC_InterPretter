package newlang5;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {

	InputStreamReader fr;
	HashMap<String, LexicalType> lexical_map = new HashMap<String, LexicalType>();
	HashMap<String, LexicalType> sinbol_map = new HashMap<String, LexicalType>();
	PushbackReader pushback;
	Queue<LexicalUnit> queue = new ArrayDeque<LexicalUnit>();

	public LexicalAnalyzerImpl(InputStreamReader fr) {
		this.fr = fr;
		this.pushback = new PushbackReader(this.fr);

		//mapの初期化
		lexical_map_init();
	}

	private void lexical_map_init() {
		//予約語
		lexical_map.put("IF", LexicalType.IF);
		lexical_map.put("THEN", LexicalType.THEN);
		lexical_map.put("ELSE", LexicalType.ELSE);
		lexical_map.put("ELSEIF", LexicalType.ELSEIF);
		lexical_map.put("ENDIF", LexicalType.ENDIF);
		lexical_map.put("FOR", LexicalType.FOR);
		lexical_map.put("FORALL", LexicalType.FORALL);
		lexical_map.put("STEP", LexicalType.STEP);
		lexical_map.put("NEXT", LexicalType.NEXT);
		lexical_map.put("SUB", LexicalType.FUNC);
		lexical_map.put("DIM", LexicalType.DIM);
		lexical_map.put("AS", LexicalType.AS);
		lexical_map.put("END", LexicalType.END);
		lexical_map.put("DOT", LexicalType.DOT);
		lexical_map.put("WHILE", LexicalType.WHILE);
		lexical_map.put("DO", LexicalType.DO);
		lexical_map.put("UNTIL", LexicalType.UNTIL);
		lexical_map.put("LOOP", LexicalType.LOOP);
		lexical_map.put("TO", LexicalType.TO);
		lexical_map.put("WEND", LexicalType.WEND);
		lexical_map.put("EOF", LexicalType.EOF);

		//シンボル
		sinbol_map.put("=", LexicalType.EQ);
		sinbol_map.put("<", LexicalType.LT);
		sinbol_map.put(">", LexicalType.GT);
		sinbol_map.put("<=", LexicalType.LE);
		sinbol_map.put("=<", LexicalType.LE);
		sinbol_map.put(">=", LexicalType.GE);
		sinbol_map.put("=>", LexicalType.GE);
		sinbol_map.put("<>", LexicalType.NE);
		sinbol_map.put("+", LexicalType.ADD);
		sinbol_map.put("-", LexicalType.SUB);
		sinbol_map.put("*", LexicalType.MUL);
		sinbol_map.put("/", LexicalType.DIV);
		sinbol_map.put("(", LexicalType.LP);
		sinbol_map.put(")", LexicalType.RP);
		sinbol_map.put(",", LexicalType.COMMA);
		sinbol_map.put("\r\n", LexicalType.NL);

		//それぞれ、getメソッド内でreturn new LexicalUnitと統一するためにLexicalTypeを代入
	}

	@Override
	public LexicalUnit get() throws Exception {
		// NewLexicalUnitを1つ帰す関数

		if (!(queue.isEmpty())) {
			return queue.poll();
		}

		//1文字だけ取り出して、取り出した文字に対応する字句のメソッドを実行する

		while (true) {
			int ci;
			if (this.pushback.ready()) {
				ci = this.pushback.read();
			} else {
				ci = this.fr.read();
			}

			if ('\"' == ci || '\'' == ci) {
				//文字列
				return getString();
			} else if ('0' <= ci && ci <= '9') {
				//数値
				this.pushback.unread((char) ci);
				return getNumber();
			} else if ('a' <= ci && ci <= 'z' || 'A' <= ci && ci <= 'Z') {
				//変数と予約語
				this.pushback.unread((char) ci);
				return getReservedORVariable();
			} else if ('(' <= ci && ci <= '/' || '<' <= ci && ci <= '>' || ci == '\r') {
				//シンボル
				this.pushback.unread((char) ci);
				return getSymbol();
			} else if (-1 == ci) {
				//EOF
				return new LexicalUnit(LexicalType.EOF);
			}

		}

	}

	@Override
	public boolean expect(LexicalType type) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void unget(LexicalUnit token) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		queue.add(token);
	}

	private LexicalUnit getString() throws Exception {
		//'か"が出るまで探す

		String target = "";
		LexicalType this_type = LexicalType.LITERAL;
		ValueType this_value_type = ValueType.STRING;

		while (true) {
			int ci;
			try {
				//一文字取り出す
				ci = this.fr.read();
			} catch (IOException e) {
				System.out.println("io error");
				break;
			}

			if (ci == '\'' || ci == '\"') {
				//"か'が来たら、処理を終了させる
				break;
			}
			//文字列を形成
			target += (char) ci;
		}

		return new LexicalUnit(this_type, new ValueImpl(target, this_value_type));
	}

	private LexicalUnit getNumber() throws Exception {
		//「数値以外」が出るまで探す
		String target = "";
		boolean point_flag = false;
		LexicalType lexical_type;
		ValueType value_type;

		while (true) {
			int ci;
			try {
				//一文字取り出す
				if (this.pushback.ready()) {
					ci = (char) this.pushback.read();
				} else {
					ci = this.fr.read();
				}

			} catch (IOException e) {
				System.out.println("io error");
				break;
			}

			if (!('0' <= ci && ci <= '9')) {
				//数字以外が来たら、処理を終了させる
				this.pushback.unread(ci);
				break;
			}

			if (ci == '.') {
				//小数点のフラグ
				point_flag = true;
			}
			//文字列を形成
			target += (char) ci;
		}

		if (point_flag) {
			//小数だったら
			lexical_type = LexicalType.DOUBLEVAL;
			value_type = ValueType.DOUBLE;
		} else {
			//整数だったら
			lexical_type = LexicalType.INTVAL;
			value_type = ValueType.INTEGER;
		}

		return new LexicalUnit(lexical_type, new ValueImpl(target, value_type));
	}

	private LexicalUnit getReservedORVariable() throws Exception {
		//「空白」か「;」か「改行」か「EOF」が出るまで探す
		//「空白」か「;」か「改行」か「EOF」が出た後にmapで検索し、引っかかったら予約語、そうでなければ変数

		String target = "";
		LexicalType this_type;

		while (true) {

			int ci;
			try {
				//一文字取り出す
				if (this.pushback.ready()) {
					ci = this.pushback.read();
				} else {
					ci = this.fr.read();
				}

			} catch (IOException e) {
				System.out.println("io error");
				break;
			}

			if (ci == ' ' || ci == ';' || ci == '\r' || ci == -1) {
				//「空白」か「;」か「改行」か「EOF」が来たら、処理を終了させる
				this.pushback.unread(ci);
				break;
			}
			//文字列を形成
			target += (char) ci;
		}

		if ((this_type = this.lexical_map.get(target)) != null) {
			//予約語だったら
			if (this_type == LexicalType.END) {
				LexicalUnit lu = this.get();
				if (lu.getType() == LexicalType.IF) {
					return new LexicalUnit(this.lexical_map.get("ENDIF"));
				} else {
					this.unget(new LexicalUnit(this.lexical_map.get("END")));
					this.unget(lu);
					return new LexicalUnit(this_type);
				}
			} else {
				return new LexicalUnit(this_type);
			}
		} else {
			//変数だったら
			return new LexicalUnit(LexicalType.NAME, new ValueImpl(target, ValueType.STRING));
		}
	}

	private LexicalUnit getSymbol() throws Exception {

		String target = "";
		LexicalType this_type;

		//シンボルの時は必ずpushback readerから値を取り出す
		int ci = this.pushback.read();

		target += (char) ci;
		if ('<' <= ci && ci <= '>' || ci == '\r') {
			//<,>,=,\rの時だけ次の文字を取り出す
			ci = this.fr.read();
			if ('<' <= ci && ci <= '>' || ci == '\n') {
				//2つ目がシンボルだったら文字列を形成
				target += (char) ci;
			} else {
				//2つ目がシンボルでなかったら1文字戻す
				this.pushback.unread(ci);
			}
		}

		//文字列に対応したタイプを代入
		this_type = this.sinbol_map.get(target);
		return new LexicalUnit(this_type);
	}

}
