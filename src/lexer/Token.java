package lexer;

/*
 * �������мǺŵĻ��࣬����һ�����ݳ�Աtag�����ֲ�ͬ�Ǻţ����캯����tagֵ.
 */
public class Token {
	public final int tag;

	public Token(int t) {
		this.tag = t;
	}

	public String toString() {
		return "" + (char) tag;
	}
	public String getString() {
		return (char) tag + "";
	}
}
