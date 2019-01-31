package newlang5;

import java.util.ArrayList;
import java.util.List;

public class ValueList extends ValueImpl {
	List<Value> valueList = new ArrayList<Value>();

	public ValueList(String src, ValueType targetType) {
		super(src, targetType);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public ValueList() {
		super("0", ValueType.INTEGER);
	}

	public void addTop(Value v) {
		this.valueList.set(0, v);
	}

	public void addElement(Value v) {
		this.valueList.add(v);
	}

	public Value getElement(int i) {
		return this.valueList.get(i);
	}

	public int size() {
		return this.valueList.size();
	}
}
