package lexer;
/*
 * ��ɴʷ��������ܵ��࣬���ݳ�ԱlineΪ�кţ�peek����ǰ��һ���ַ���words�Ƿ��ű�
 * ��Ա����reserve�������Ǻż�����ű����Ĺ��캯�������б����ּ�����ű�
 */
import java.io.*;  
import java.util.*;  

import symbols.*;  
  
public class Lexer {  
    public static int line = 1;     /* ��¼�к� */  
    char peek = ' ';        /* ��һ�������ַ� */  
    Hashtable<String, Word> words =   
        new Hashtable<String, Word>();  
    /* ���ű� */  
    private Hashtable<Token, String> table =   
        new Hashtable<Token, String>();  
    /* token���� */  
    private List<String> tokens =   
        new LinkedList<String> ();  
    /* errors���� */
    private List<Error> errors =   
            new LinkedList<Error> ();  
    /* ��ȡ�ļ����� */  
    BufferedReader reader = null;   
    /* ���浱ǰ�Ƿ��ȡ�����ļ��Ľ�β  */  
    private Boolean isEnd = false;  
      
    /* �Ƿ��ȡ���ļ��Ľ�β */  
    public Boolean getReaderState() {  
        return this.isEnd;  
    }  
      
    /* ����洢��table�е� */  
    public void saveSymbolsTable() throws IOException {  
        FileWriter writer = new FileWriter("���ű�.txt");  
        writer.write("[����]          [����������Ϣ]\n");  
        writer.write("\r\n");  
          
        Enumeration<Token> e = table.keys();  
        while( e.hasMoreElements() ){  
            Token token = (Token)e.nextElement();  
            String desc = table.get(token);  
              
             /* д���ļ� */ 
            writer.write(token + "\t\t\t" + desc + "\r\n");  
        }  
              
        writer.flush();  
    }  
      
    /* ���� Errors*/
    public void saveErrors() throws IOException{           //	��д
    	FileWriter ew = new FileWriter("errors.txt");  
       // ew.write("[����]          [����������Ϣ]\n");  
        ew.write("\r\n");  
          
        for(int i = 0; i < errors.size(); ++i) {  
        	//int er = (int)errors.get(i).line;
           // String err = (String)errors.get(i).error;  
            Error error = errors.get(i);
            /* д���ļ� */  
            ew.write(error.writeError() + "\r\n");  
        }     
              
        ew.flush();  
    }
    /* ����Tokens */  
    public void saveTokens() throws IOException {  
        FileWriter writer = new FileWriter("Tokens��.txt");  
        writer.write("[����]  \n");  
        writer.write("\r\n");  
          
        for(int i = 0; i < tokens.size(); ++i) {  
            String tok = (String)tokens.get(i);  
              
            /* д���ļ� */  
            writer.write(tok + "\r\n");  
        }     
              
        writer.flush();  
    }  
      
    void reserve(Word w) {  
        words.put(w.lexme, w);  
    }  
      
    /* 
     * ���캯���н��ؼ��ֺ�������ӵ�hashtable words�� 
     */  
    public Lexer() {  
        /* ��ʼ����ȡ�ļ����� */  
        try {  
            reader = new BufferedReader(new FileReader("����.txt"));  
        }  
        catch(IOException e) {  
            System.out.print(e);  
        }  
          
          
        /* �ؼ��� */  
        this.reserve(new Word("if", Tag.IF));  
        this.reserve(new Word("then", Tag.THEN));  
        this.reserve(new Word("else", Tag.ELSE));  
        this.reserve(new Word("while", Tag.WHILE));  
        this.reserve(new Word("do", Tag.DO));  
        //�ؼ������ڷָ�ؼ���ʱʹ�ã��������ӦTagֵ
        this.reserve(new Word("begin", Tag.BEGIN));
        this.reserve(new Word("end", Tag.END));
        
        /* ���� */  
        this.reserve(Word.True);  
        this.reserve(Word.False);  
        this.reserve(Type.Int);  
        this.reserve(Type.Char);  
        this.reserve(Type.Bool);  
        this.reserve(Type.Float);  
    }  
      
    public void readch() throws IOException {  
        /* ����Ӧ����ʹ�õ��� */  
        peek = (char)reader.read();  
        if((int)peek == 0xffff){  
            this.isEnd = true;  
        }  
        // peek = (char)System.in.read();  
        peek = bts(peek);
    }  
      
    public Boolean readch(char ch) throws IOException {  
        readch();  
        if (this.peek != ch) {  
            return false;  
        }  
          
        this.peek = ' ';  
        return true;  
    }  
    public char bts(char p){
    	/* ���peek�Ǵ�д��ĸ����ΪСд��ĸ */
        if(p>='A'&&p<='Z'){
        	p =(char) (p+32);
        }
        return p;
    }
    //�жϹؼ���
    public boolean isKeyWord(Word w){
    	if(w.lexme.equals("if")||w.lexme.equals("then")||w.lexme.equals("while")
    			||w.lexme.equals("do")||w.lexme.equals("else")||w.lexme.equals("end")
    			||w.lexme.equals("begin"))
    		return true;
    	else
    	return false;
    }
    /* ʮ������תΪ������������������λ����1 */
    public int c10t2(int x){
    	int count = 0 ;
    	while((x=x/2)>0){
    		count++;
    	}
    	return count;
    }
    public Token scan() throws IOException {  
        /* �����հ� */   
        for( ; ; readch() ) {  
            if(peek == ' ' || peek == '\t')  
                continue;  
             /* ����ע��  */
            else if(peek=='-'&&readch('-')){
            	//[]--bjgnb  
            	for(;!readch('\n');readch()){
            		continue;
            	}
            	line++;
            }
            else if (peek == '\n')   
                line = line + 1;  
            else  
                break;  
        }  
        
        /* ���濪ʼ�ָ�ؼ��֣���ʶ������Ϣ    */
        switch (peek) {  								
        /* ���� :=, <>, >, <, >=, <=, ������ʹ��״̬��ʵ�� */  
        case ':' :  
            if (readch('=')) {  
            	tokens.add("<:=>");  
                return new Token('=');   
            }  
            else {  
                tokens.add("<:>");  
                return new Token(':');  
            }  
        case '>' :  
            if (readch('=')) {  
                tokens.add("<>=>");  
                return Word.ge;  
            }  
            else {  
                tokens.add("<>>");  
                return new Token('>');  
            }  
        case '<' :  								//�˴�������
            if (readch('=')) {  
                tokens.add("<<=>");  
                return Word.le;  
            }  
            else if(readch('>')){
            	tokens.add("<<>>"); 
                return Word.ne; 
            }
            else {  //
                tokens.add("<<>");  
                return new Token('<');  
            }  
        case ';':
        	readch();
        	tokens.add("<;>");
        	return new Token(';');
        case '(':
        	readch();
        	tokens.add("<(>");
        	return new Token('(');
        case ')':
        	readch();
        	tokens.add("<)>");
        	return new Token(')');
        case '-':					//������
        	if (!readch('-')){
        	tokens.add("<->");
        	return new Token('-');
        	}
        }
        /* �����Ƕ����ֵ�ʶ�𣬸����ķ��Ĺ涨�Ļ�������� 
         * ����ֻҪ���ܹ�ʶ����������. 
         */  
        if(Character.isDigit(peek)) {  
            int value = 0;  
            /* �����ԡ�0x����ͷ,����ʮ������������ */
            if(peek=='0'&&readch('x')){
            	readch();
            	do{
            		value = 36 * value + Character.digit(peek, 36);  
                    readch();
            	}while (Character.isLetterOrDigit(peek));
            }
            else if(peek=='0'){
            	readch();
            	if(!Character.isDigit(peek)){
            		value=0;
            	}
            }
            else {
            	do {  
            		value = 10 * value + Character.digit(peek, 10);  
            		readch();  
            	} while (Character.isDigit(peek));  
            }
            if(c10t2(value)>=24){ 				//value����Math.pow(2.0,24)������Խ�����;
            	//
            	Error e = new Error(line,"����Խ��");
            	errors.add(e);
            }
            Num n = new Num(value);  			
            tokens.add(n.toString());      
            //table.put(n, "Num");  
            return n;  
        }  
          
        /* 
         * �ؼ��ֻ����Ǳ�ʶ����ʶ�� 
         */  
        if(Character.isLetter(peek)) {  
            StringBuffer sb = new StringBuffer();  
              
            /* ���ȵõ�������һ���ָ� */  
            do {  
                sb.append(peek);  //����һ���ַ�/�ַ�����ĩβ
                readch();  
            } while (Character.isLetterOrDigit(peek));  
              
            /* �ж��ǹؼ��ֻ��Ǳ�ʶ�� */  
            String s = sb.toString();  //������תΪString��
            Word w = (Word)words.get(s);  
              
            /* ����ǹؼ��ֻ��������͵Ļ���w��Ӧ���ǿյ� */  
            /*if(w != null&& w.lexme=="if") {  //
                // table.put(w, "KeyWord or Type");  
                tokens.add(w.keyWordtoString());  
                return w;  ˵���ǹؼ��� ������������ 
            }*/
            if(w != null) {  //
                // table.put(w, "KeyWord or Type");  
            	if(isKeyWord(w))
            		tokens.add(w.keyWordtoString());
            	else
                tokens.add(w.toString());  
                return w;  /*˵���ǹؼ��� ������������*/   
            }  
            else{
            /* �������һ����ʶ��id */  
            w = new Word(s, Tag.ID);  
            tokens.add(w.toString());  
            //����
            //Word we = new Word("bb",Tag.BASIC);
            //table.put(we, "basic");
            //
            table.put(w, "id");  
            words.put(s,  w);    
            return w;  
            }
        }  
          
        /* peek�е������ַ�������Ϊ�Ǵʷ���Ԫ���� */  
        Token tok  = new Token(peek);  
        // table.put(tok, "Token or Seprator");  
        if ((int)peek != 0xffff )   
            tokens.add(tok.toString());  
        peek = ' ';  
          
        return tok;  
    }  
  /*@zjp����*/
    public static void main(String[] args) {
		Lexer lexer = new Lexer();
		try {
			lexer.readch();
			//System.out.println(lexer.peek);
			Boolean b = lexer.readch('W');
			if(!b){
				//System.out.println(lexer.peek);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
}  
}