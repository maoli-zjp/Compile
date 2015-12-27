package parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;
import lexer.Input;

public class Main {

	private static Input input;
	private static LinkedList<String> inputList;
	private static Stack<Integer> statusStack;
	private static Stack<String> signStack;
	private static Action action;
	private static Expr expr;
	private static NextAction nextAction;
	
	public static void init() throws IOException{
		input = new Input();
		inputList = input.getList();// �����
		statusStack = new Stack<Integer>();// ״̬ջ
		signStack = new Stack<String>();// ���ű�
		action = Action.getInstance();
		expr = Expr.getInstance();
		statusStack.push(0);
		signStack.push("$");
		System.out.println("״̬ջ��" + statusStack.toString());
		System.out.println("����ջ��" + signStack.toString());
		System.out.println("����ջ��" + inputList.toString());
		System.out.println("����������" + "\n");

	}
	public static void main(String[] args) throws IOException {

		init();
		
		String inputCur = inputList.pop();
		int stateCur; // ��ǰ״̬
		String signCur; // ��ǰ����
		for (; inputList != null;) {
			stateCur = statusStack.peek();//peek()��ȡջ��Ԫ�ص����׳���
			signCur = signStack.peek();
			// �ӵ�ǰ״̬�����룬�ó���һ��Ҫ��ʲô
			nextAction = action.get(stateCur, inputCur);
			if (nextAction == null) {
				System.out.println("������ǰ״̬Ϊ��S" + stateCur);
				break;
			}
			char type = nextAction.getType();
			int nextState = nextAction.getStatusNumberNext();
			String actionPrint = "";
			switch (type) {
			case 's':
				statusStack.push(nextState);
				signStack.push(inputCur);
				actionPrint = "����";
				break;
			case 'r':
				Rules ruleObject = expr.get(nextState);

				int reduceNum = ruleObject.getRightNum();
				// ��Լ���Ҳ���ܲ�ֹһ�������ݷ��صĸ����ж�
				for (int i = reduceNum; i > 0; i--) {
					statusStack.pop();
					signStack.pop();
				}
				signStack.push(ruleObject.getLeft());
				String gotoStatement = signStack.peek();
				stateCur = statusStack.peek();
				nextAction = action.get(stateCur, gotoStatement);
				nextState = nextAction.getStatusNumberNext();
				statusStack.push(nextState);
				actionPrint = "��Լ/" + ruleObject.getLeft() + " -> " + ruleObject.getRight();
				break;
			case 'a':
				System.out.println("accept");
				break;
			default:
				break;
			}
			System.out.println("״̬ջ��" + statusStack.toString());
			System.out.println("����ջ��" + signStack.toString());
			System.out.println("����ջ��" + inputList.toString());
			System.out.println("������" + actionPrint + "\n");

			if (type == 's') {
				inputCur = inputList.pop();
			}
			if (type == 'a') {
				break;
			}
		}

	}
}
