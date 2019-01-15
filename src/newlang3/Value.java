package newlang3;

public interface Value {
// 実行すべきコンストラクタ
//    public Value(String s);
//    public Value(int i);
//    public Value(double d);
//    public Value(boolean b);
//    public String get_sValue();
	public String getSValue();
	// ストリング型で値を渡す。必要であれば型変換を行う。以下も同じ。また、変換できないときは適当な値を返す。
    public int getIValue();
    	// �����^�Œl�����o���B�K�v������΁A�^�ϊ����s���B
    public double getDValue();
    	// �����_�^�Œl�����o���B�K�v������΁A�^�ϊ����s���B
    public boolean getBValue();
    	// �_���^�Œl�����o���B�K�v������΁A�^�ϊ����s���B
    public ValueType getType();
}
