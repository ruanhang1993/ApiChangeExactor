package cn.edu.fudan.se.apiChangeExtractor.myast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

public class MyAstParser {
	public static void main(String[] args){
		MyAstParser parser = new MyAstParser();
		CompilationUnit classCU = parser.parseFile("D:/ApiChangeExtractor.java");
		MyClassVisitor visitor = new MyClassVisitor();
		classCU.accept(visitor);
		for( Object i :classCU.imports()){
			if(i instanceof ImportDeclaration){
				ImportDeclaration impo = (ImportDeclaration)i;
			}
		}
    }
	
	public CompilationUnit parseFile(String path){
		File file = new File(path);
		ASTParser astParser = ASTParser.newParser(AST.JLS8);  
        astParser.setSource(getFileContent(file));  
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);  
        return (CompilationUnit) (astParser.createAST(null));  
	}
	
	public char[] getFileContent(File file){
		byte[] input = null;  
		BufferedInputStream bufferedInputStream = null;
        try {  
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));  
            input = new byte[bufferedInputStream.available()];  
            bufferedInputStream.read(input);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally{
        	if(bufferedInputStream != null){
	            try {
					bufferedInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
        	}
        }
        if(input==null) return null;
        return new String(input).toCharArray();
	}
}
