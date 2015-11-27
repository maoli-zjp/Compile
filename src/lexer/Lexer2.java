package lexer;

import java.io.*;
import java.util.*;
import symbols.*;

public class Lexer {
	public static int line = 1; /* �к� */
	char peek = ' '; /* ��һ�������ַ� */
	Hashtable<String, Word> words = new Hashtable<String, Word>(); /* ���ű� */
	private Hashtable<Token, String> table = new Hashtable<Token, String>(); /* token�� */
	private List<String> tokens = new LinkedList<String>(); /* token�� */
	private List<String> errors = new LinkedList<String>(); /* ����� */
	BufferedReader reader = null;
	private Boolean isEnd = false; /* �Ƿ��ȡ���ļ��Ľ�β */
	private static int numlimit = 1<<24; /* �������� */
	private static int lengthlimit = 32; /* ��ʶ���������� */
	private static int lengthvalid = 8; /* ��ʶ����Ч���� */
	private static String sep = System.getProperty("line.separator"); /* ϵͳ���� */
	
 	public Boolean getReaderState() {/* ��ȡ����״̬ */
		return this.isEnd;
	}

	public void saveSymbolsTable() throws IOException { /* ����������� */
		FileWriter writer = new FileWriter("TypeOfCharater.txt");
		writer.write("-------->TypeOfCharater<--------" + sep);
		for(Enumeration<Token> e = table.keys();e.hasMoreElements();) { 
			Token token = e.nextElement();
			writer.write("<" + token + ">:<" + table.get(token) + ">" + sep);
		}
		writer.flush();
		writer.close();
	}

	public void saveTokens() throws IOException {/* ����Tokens */
		FileWriter writer = new FileWriter("Tokens.txt");
		writer.write("-------->Tokens<--------" + sep);
		for(String token : tokens){
			writer.write("< " + token + " >" + sep);
		}
		writer.flush();
		writer.close();
	}

	public void saveErrors() throws IOException {/* ����Tokens */
		FileWriter writer = new FileWriter("Errors.txt");
		writer.write("-------->Errors<--------" + sep);
		for(String error : errors){
			writer.write( error + sep);
		}
		writer.flush();
		writer.close();
	}

	void reserve(Word w) { /* ���浥�ʵ����ű� */
		words.put(w.lexme, w);
	}

	public Lexer() {
		try {	/* ��ʼ����ȡ�ļ����� */
			reader = new BufferedReader(new FileReader("in.txt"));
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
		/* ���� */ {
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
	
	public void readch() throws IOException {  
        peek = (char)reader.read();
        if(Character.isUpperCase(peek)) peek = (char) (peek + 32); /*��Сд������*/
        if((int)peek == 0xffff){  
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

    public void clearBlank() throws IOException{ /* ����հ� */
		for( ; ; readch() ) {   
            if(peek == ' ' || peek == '\t' || peek == '\r')  
                continue;  
            else if (peek == '\n')   
                line = line + 1;  
            else return ;
        }    	
    }
    
    public Token digitGet() throws IOException{ /* ȡ���� */
        int value = 0;int mult = 10;
        boolean outofnumlimit = false;
        if(peek == '0'){
        	if(readch('x')){
        		readch();
        		mult = 36;
        	}
        	else if(!Character.isDigit(peek)){
        		Num n = new Num(value);
        		tokens.add(n.toString());
        		return n;
        	}
        }
        do {  
            value = mult * value + (Character.isDigit(peek) ? (peek - '0') : (peek - 'a' + 10));
            if(value >= numlimit){
            	outofnumlimit = true;
            }
            readch();  
        } while (Character.isLetterOrDigit(peek));
        if(outofnumlimit == true){
        	errors.add("line " + line + " : " + "Number out of range.");
        	tokens.add(Word.Inf.toString());
        	return Word.Inf;
        }
        Num n = new Num(value);  
        tokens.add(n.toString());
        return n;  
    }
    
    public Token wordGet() throws IOException{ /* ȡ�� */
        StringBuffer sb = new StringBuffer();
        do {
            sb.append(peek);  
            readch();  
        } while (Character.isLetterOrDigit(peek));  
        
        String s = sb.toString();
        if(s.length() >= lengthlimit){
        	errors.add("line " + line + " : " + "The length of id is exceed " + lengthlimit);
        }
        if(s.length() >= lengthvalid){
        	s = s.substring(0, lengthvalid);
        }
        Word w = words.get(s);  
        if(w != null) { /* �ʴ��� */  
            tokens.add(w.toString());
        } else {
        	w = new Word(s , Tag.ID);
        	
        	tokens.add("id,"+w.toString());
        	table.put(w, "id");
        	words.put(s, w);
        }
        return w;  
    }
    
    private boolean isOperation(char peek) {
    	return peek == '+' || peek == '-' || peek == '*' || peek == '/' ||
    			peek == '>' || peek == '<' || peek == '=' || peek == ':';
    }
    
	private Token operationGet() throws IOException{ /* ȡ����� */
        switch (peek) {
        	case ':':
        		if(readch('=')) return Operation.Asn;
        		else return Operation.Col;
        	case '+':
        		readch();
        		return Operation.Plus;
        	case '-':{
        		if(readch('-')){ /* ע�� */
        			while(peek != '\n' && (int)peek != 0xffff)
        				readch();
        			return Word.Note;
        		}
        		else return Operation.Minus;
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
        		if(readch('=')) return Operation.Ge;
        		else return Operation.Gr;
        	case '<':
        		if(readch('=')) return Operation.Le;
        		else if(peek == '>'){
        			peek = ' ';
        			return Operation.Ne;
        		}
        		else return Operation.Ls;
        }
        return null;
	}
   
	public Token scan() throws IOException {
		/*����հ�*/
		clearBlank();
//		System.out.println(peek);
		
		/*�����ʶ��*/
		if(isOperation(peek)){
			Token t = operationGet();
			tokens.add(t.toString());
			return t;
		} 
          
        /*����(����)ʶ��*/
        if(Character.isDigit(peek)) {
        	return digitGet();
        }  
            
        /*�ؼ���&��ʶ��ʶ��*/
        if(Character.isLetter(peek)) {
        	return wordGet();
        }  
          
        /* ���ַ����� */  
        Token tok = new Token(peek);    
        if ((int)peek != 0xffff )   
            tokens.add(tok.toString());  
        peek = ' ';
        return tok;  
    }
}





