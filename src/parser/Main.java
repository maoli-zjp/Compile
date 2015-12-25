package parser;

/* 
 * ������ 
 */
import java.io.*;
import java.lang.reflect.Field;

import lexer.*;

public class Main {
	public static void main(String[] args) throws IOException{
		Lexer lexer = new Lexer();

		while (lexer.getReaderState() == false) {
			lexer.scan();
		}

		/* ���������Ϣ */
		lexer.saveTokens(); //
		lexer.saveSymbolsTable(); //
		lexer.saveErrors();//

	}
}
