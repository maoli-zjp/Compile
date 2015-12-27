package parser;

/**
 * @author Schaepher
 *
 */
public class NextAction {
	private char type;
	private int stateNumberNext;

	public NextAction(char type, int statusNumber) {
		super();
		this.type = type;
		this.stateNumberNext = statusNumber;
	}

	// 's'Ϊstatus  'r'Ϊreduce����Լ�� 'a'Ϊaccept 'g'Ϊgoto
	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public int getStatusNumberNext() {
		return stateNumberNext;
	}

	public void setStatusNumberNext(int statusNumber) {
		this.stateNumberNext = statusNumber;
	}

	public String toString() {
		return this.type + String.valueOf(this.stateNumberNext);
	}

}
