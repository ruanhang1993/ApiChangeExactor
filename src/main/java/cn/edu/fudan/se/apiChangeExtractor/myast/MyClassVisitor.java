package cn.edu.fudan.se.apiChangeExtractor.myast;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MyClassVisitor extends ASTVisitor {
	@Override  
    public boolean visit(FieldDeclaration node) {  
		System.out.println(node.getType().toString());
        for (Object obj: node.fragments()) {  
            VariableDeclarationFragment v = (VariableDeclarationFragment)obj;  
            System.out.println("Field:\t" + v.getName());  
        }  
          
        return true;  
    }  
  
    @Override  
    public boolean visit(MethodDeclaration node) {
        System.out.print("Method:\t" + node.getName());
        if(node.getRoot() instanceof CompilationUnit){
        	CompilationUnit cu = (CompilationUnit)node.getRoot();
        	System.out.println("\t"+cu.getLineNumber(node.getStartPosition())+"-"+cu.getLineNumber(node.getStartPosition()+node.getLength()));
        }
        return true;  
    }  
  
    @Override  
    public boolean visit(TypeDeclaration node) {  
        System.out.println("Class:\t" + node.getName());  
        return true;  
    }  
}
