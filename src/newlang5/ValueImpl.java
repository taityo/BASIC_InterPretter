package newlang5;

public class ValueImpl implements Value {

	ValueType type;
	int ivalue;
	double dvalue;
	String svalue;
	boolean bvalue;

	public ValueImpl(String src, ValueType targetType) {
		//コンストラクタでValueの型を決定してあげる
		this.type = targetType;

		//System.out.println("value imple start");
		switch (targetType) {
		case INTEGER:
			//System.out.println("integer");
			svalue = src;
			ivalue = Integer.valueOf(src);
			dvalue = Double.valueOf(src);
			bvalue = this.ivalue != 0;
			break;
		case DOUBLE:
			//System.out.println("double");
			svalue = src;
			dvalue = Double.valueOf(src);
			ivalue = (int) dvalue;
			bvalue = this.dvalue != 0;
			break;
		case STRING:
			//System.out.println("string");
			svalue = src;
			if (svalue.equals("")) {
				ivalue = 0;
				dvalue = 0.0;
				bvalue = false;
			} else {
				ivalue = 1;
				dvalue = 1.0;
				bvalue = true;
			}
			break;
		case BOOL:
			//System.out.println("bool");
			bvalue = Boolean.valueOf(src);
			if (bvalue) {
				ivalue = 1;
				dvalue = 1.0;
				svalue = "TRUE";
			} else {
				ivalue = 0;
				dvalue = 0.0;
				svalue = "FALSE";
			}
			break;
		default:
			break;
		}

		//System.out.println("end");
	}

	@Override
	public String getSValue() {
		// TODO 自動生成されたメソッド・スタブ
		//System.out.println("string out");
		return this.svalue;
	}

	@Override
	public int getIValue() {
		// TODO 自動生成されたメソッド・スタブ
		return this.ivalue;
	}

	@Override
	public double getDValue() {
		// TODO 自動生成されたメソッド・スタブ
		return this.dvalue;
	}

	@Override
	public boolean getBValue() {
		// TODO 自動生成されたメソッド・スタブ
		return this.bvalue;
	}

	@Override
	public ValueType getType() {
		// TODO 自動生成されたメソッド・スタブ
		return this.type;
	}

}
