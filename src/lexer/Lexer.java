package lexer;

import java.io.*;
import java.util.*;
import symbols.*;

public class Lexer {
	public static int line = 1; /* �к� */
	char peek = ' '; /* ��һ�������ַ� */
	Hashtable<String, Word> words = new Hashtable<String, Word>(); /* ���ű� */
	// /* token�� */
	private List<String> table = new LinkedList<String>();/* token�� */
	private List<String> tokens = new LinkedList<String>(); /* token�� */
	private List<Error> errors = new LinkedList<Error>(); /* ����� */
	BufferedReader reader = null;
	private Boolean isEnd = false; /* �Ƿ��ȡ���ļ��Ľ�β */
	private static int numlimit = 1<<24; /* �������� */
	private static int lengthlimit = 32; /* ��ʶ���������� */
	private static int lengthvalid = 8; /* ��ʶ����Ч���� */

	public Boolean getReaderState() {/* ��ȡ����״̬ */
		return this.isEnd;
	}

	/* ����洢��table�е� */
	public void saveSymbolsTable() throws IOException {
		FileWriter writer = new FileWriter("���ű�.txt");
		writer.write("[���ű�]          [����������Ϣ]\n");
		writer.write("\r\n");

		for (int i = 0; i < table.size(); ++i) {
			String w = table.get(i);
			/* д���ļ� */
			if (i > 120)
				writer.write("<" + w + ">	:			<id>" + "\r\n");
			else
				writer.write("<" + w + ">	:			<key>" + "\r\n");
		}

		writer.flush();
	}

	/* ����Tokens */
	public void saveTokens() throws IOException {
		FileWriter writer = new FileWriter("Tokens��.txt");
		writer.write("[����]  \n");
		writer.write("\r\n");

		for (int i = 0; i < tokens.size(); ++i) {
			String tok = tokens.get(i);

			// д���ļ�
			writer.write("< " + tok + " >" + "\r\n");
		}

		writer.flush();
	}

	/* ���� Errors */
	public void saveErrors() throws IOException {
		FileWriter ew = new FileWriter("errors.txt");
		ew.write("\r\n");

		for (int i = 0; i < errors.size(); ++i) {

			Error error = errors.get(i);
			/* д���ļ� */
			ew.write(error.writeError() + "\r\n");
		}

		ew.flush();
	}

	void reserve(Word w) { /* ���浥�ʵ����ű� */
		words.put(w.lexme, w);
		table.add(w.lexme);
	}

	public Lexer() {
		try { /* ��ʼ����ȡ�ļ����� */
			reader = new BufferedReader(new FileReader("����.txt"));
		} catch (IOException e) {
			System.out.print(e);
		}

		/* �ؼ��� */{
			this.reserve(Word.Absolute);
			this.reserve(Word.Abstract);
			this.reserve(Word.Array);
			this.reserve(Word.As);
			this.reserve(Word.Asm);
			this.reserve(Word.Assembler);
			this.reserve(Word.At);
			this.reserve(Word.Automated);
			this.reserve(Word.Begin);
			this.reserve(Word.Case);
			this.reserve(Word.Cdecl);
			this.reserve(Word.Class);
			this.reserve(Word.Const);
			this.reserve(Word.Constructor);
			this.reserve(Word.Contains);
			this.reserve(Word.Default);
			this.reserve(Word.Destructor);
			this.reserve(Word.Dispid);
			this.reserve(Word.Dispinterface);
			this.reserve(Word.Do);
			this.reserve(Word.Downto);
			this.reserve(Word.Dynamic);
			this.reserve(Word.Else);
			this.reserve(Word.End);
			this.reserve(Word.Except);
			this.reserve(Word.Export);
			this.reserve(Word.Exports);
			this.reserve(Word.External);
			this.reserve(Word.Far);
			this.reserve(Word.File);
			this.reserve(Word.Finalization);
			this.reserve(Word.Finally);
			this.reserve(Word.For);
			this.reserve(Word.Forward);
			this.reserve(Word.Function);
			this.reserve(Word.Goto);
			this.reserve(Word.If);
			this.reserve(Word.Implementation);
			this.reserve(Word.Implements);
			this.reserve(Word.In);
			this.reserve(Word.Index);
			this.reserve(Word.Inherited);
			this.reserve(Word.Initialization);
			this.reserve(Word.Inline);
			this.reserve(Word.Interface);
			this.reserve(Word.Is);
			this.reserve(Word.Label);
			this.reserve(Word.Library);
			this.reserve(Word.Message);
			this.reserve(Word.Name);
			this.reserve(Word.Near);
			this.reserve(Word.Nil);
			this.reserve(Word.Nodefault);
			this.reserve(Word.Object);
			this.reserve(Word.Of);
			this.reserve(Word.On);
			this.reserve(Word.Out);
			this.reserve(Word.Overload);
			this.reserve(Word.Override);
			this.reserve(Word.Package);
			this.reserve(Word.Packed);
			this.reserve(Word.Pascal);
			this.reserve(Word.Private);
			this.reserve(Word.Procedure);
			this.reserve(Word.Program);
			this.reserve(Word.Property);
			this.reserve(Word.Protected);
			this.reserve(Word.Public);
			this.reserve(Word.Published);
			this.reserve(Word.Raise);
			this.reserve(Word.Read);
			this.reserve(Word.Readonly);
			this.reserve(Word.Record);
			this.reserve(Word.Register);
			this.reserve(Word.Reintroduce);
			this.reserve(Word.Repeat);
			this.reserve(Word.Requires);
			this.reserve(Word.Resident);
			this.reserve(Word.Resourcestring);
			this.reserve(Word.Safecall);
			this.reserve(Word.Set);
			this.reserve(Word.Shl);
			this.reserve(Word.Shr);
			this.reserve(Word.Stdcall);
			this.reserve(Word.Stored);
			this.reserve(Word.String);
			this.reserve(Word.Then);
			this.reserve(Word.Threadvar);
			this.reserve(Word.To);
			this.reserve(Word.Try);
			this.reserve(Word.Type);
			this.reserve(Word.Unit);
			this.reserve(Word.Until);
			this.reserve(Word.Uses);
			this.reserve(Word.Var);
			this.reserve(Word.Virtual);
			this.reserve(Word.While);
			this.reserve(Word.With);
			this.reserve(Word.Write);
			this.reserve(Word.Writeonly);
		}
		/* ����ؼ��� */{
			this.reserve(Operation.And);
			this.reserve(Operation.DivInt);
			this.reserve(Operation.Mod);
			this.reserve(Operation.Not);
			this.reserve(Operation.Or);
			this.reserve(Operation.Xor);
		}
		/* ���� */{
			this.reserve(Type.Shortint);
			this.reserve(Type.Integer);
			this.reserve(Type.Longint);
			this.reserve(Type.Byte);
			this.reserve(Type.Word);
			this.reserve(Type.Dword);
			this.reserve(Type.Int64);
			this.reserve(Type.Qword);
			this.reserve(Type.Singer);
			this.reserve(Type.Real);
			this.reserve(Type.Double);
			this.reserve(Type.Extended);
			this.reserve(Type.Comp);
			this.reserve(Type.Char);
			this.reserve(Type.Boolean);
		}
	}

	public char bts(char p) {
		/* ���peek�Ǵ�д��ĸ����ΪСд��ĸ */
		if (p >= 'A' && p <= 'Z') {
			p = (char) (p + 32);
		}
		return p;
	}

	public void readch() throws IOException {
		peek = (char) reader.read();
		peek = bts(peek);
		if ((int) peek == 0xffff) {
			this.isEnd = true;
		}
	}

	public Boolean readch(char ch) throws IOException {
		readch();
		if (this.peek != ch) {
			return false;
		}
		this.peek = ' ';
		return true;
	}

	/* ����հ� */
	public void clear() throws IOException {
		for (;; readch()) {
			if (peek == ' ' || peek == '\t' || peek == '\r')
				continue;
			else if (peek == '\n')
				line++;
			else
				return;
		}
	}

	/* ȡ���� */
	public Token digitGet() throws IOException {
		int value = 0;
		int mult = 10;
		double v = 0.0;
		double mu = 0.1;
		boolean isReal = false;
		if (peek == '0') {
			if (readch('x')) {
				readch();
				mult = 36;
			} else if (peek == '.') {
				isReal = true;
			} else if (!Character.isDigit(peek)) {
				Num n = new Num(value);
				tokens.add(n.toString());
				return n;
			}
		}
		if (!isReal) {
			do {
				value = mult
						* value
						+ (Character.isDigit(peek) ? (peek - '0')
								: (peek - 'a' + 10));
				readch();
			} while (Character.isLetterOrDigit(peek));
			if (value >= numlimit) {
				Error error = new Error(line, "����Խ��");
				errors.add(error);
				// tokens.add(Word.Inf.toString());
				// return Word.Inf;

			}
		}
		if (isReal || peek == '.') {
			readch();
			do {
				v = v + mu * (peek - '0');
				mu = mu * 0.1;
				readch();
			} while (Character.isDigit(peek));
		}
		if (v == 0.000000) {
			Num n = new Num(value);
			tokens.add(n.toString());
			return n;
		} else {
			Real r = new Real(v + value);
			tokens.add(r.toString());
			return r;
		}
	}

	public Token wordGet() throws IOException { /* ȡ�� */
		StringBuffer sb = new StringBuffer();
		do {
			sb.append(peek);
			readch();
		} while (Character.isLetterOrDigit(peek));

		String s = sb.toString();
		if (s.length() >= lengthlimit) {
			Error error = new Error(line, "��ʶ�����ȳ�������");
			errors.add(error);
			s = "";
		}
		if (s.length() >= lengthvalid) {
			s = s.substring(0, lengthvalid);
		}
		Word w = words.get(s);
		if (w != null) { /* �ʴ��� */
			tokens.add(w.toString());
		} else {
			w = new Word(s, Tag.ID);
			if (!s.equals("")) {
				tokens.add("id," + w.toString());
				table.add(w.lexme);// ������ű�
			}
			words.put(s, w);
		}
		return w;
	}

	private boolean isOperation(char peek) {
		return peek == '+' || peek == '-' || peek == '*' || peek == '/'
				|| peek == '>' || peek == '<' || peek == '=' || peek == ':';
	}

	/* ȡ����� */
	private Token operationGet() throws IOException {
		switch (peek) {
		case ':':
			if (readch('='))
				return Operation.Asn;
			else
				return Operation.Col;
		case '+':
			readch();
			return Operation.Plus;
		case '-': {
			if (readch('-')) { /* ���ע�� */
				while (peek != '\n' && (int) peek != 0xffff)
					readch();
				return Word.Note;
			} else
				return Operation.Minus;
		}
		case '*':
			readch();
			return Operation.Mult;
		case '/':
			readch();
			return Operation.DivReal;
		case '=':
			readch();
			return Operation.Eq;
		case '>':
			if (readch('='))
				return Operation.Ge;
			else
				return Operation.Gr;
		case '<':
			if (readch('='))
				return Operation.Le;
			else if (peek == '>') { // peekֵ�Ѿ��ı�
				peek = ' ';
				return Operation.Ne;
			} else
				return Operation.Ls;
		}
		return null;
	}

	public Token scan() throws IOException {

		/* ����հ� */
		clear();
		// System.out.println(peek);

		/* �����ʶ�� */
		if (isOperation(peek)) {
			Token t = operationGet();
			tokens.add(t.toString());
			return t;
		}

		/* ����float��ʶ�� */
		if (Character.isDigit(peek)) {
			return digitGet();
		}

		/* �ؼ���&��ʶ��ʶ�� */
		if (Character.isLetter(peek)) {
			return wordGet();
		}

		/* ���ַ����� */
		Token tok = new Token(peek);
		if ((int) peek != 0xffff)
			tokens.add(tok.toString());
		peek = ' ';
		return tok;
	}
}
