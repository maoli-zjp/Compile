package lexer;

/* 
 * ������ 
 */  
import java.io.*;  

import lexer.*;  

public class Main {  
    public static void main(String[] args) throws IOException {  
        Lexer lexer = new Lexer();  
          
        while (lexer.getReaderState() == false) {  
            lexer.scan();  
        }  
          
        /* ���������Ϣ */  
        lexer.saveTokens();  //
        lexer.saveSymbolsTable(); //
        lexer.saveErrors();//
        char peek = ' ';
        BufferedReader reader = null;
        try{
        	reader = new BufferedReader(new FileReader("test.txt")); 
        } catch(IOException e) {  
            System.out.print(e);  
        }  
        peek = (char)reader.read(); 
        System.out.println(peek);
        if(peek=='z'){
        peek = (char)reader.read(); 
        System.out.println(peek);
        }
    }  
}  
