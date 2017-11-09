package cn.edu.fudan.se.apiChangeExtractor.myast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

public class GumTreeDiffParser {
	String oldFile;
	String newFile;
	TreeContext srcTC;
	TreeContext dstTC;
	ITree src;
	ITree dst;
	
	public GumTreeDiffParser(String oldFile, String newFile){
		this.oldFile = oldFile;
		this.newFile = newFile;
	}
	
	public void init(){
		Run.initGenerators();
		try {
			JdtTreeGenerator parser = new JdtTreeGenerator();
			srcTC = parser.generateFromFile(new File(oldFile));
			src = srcTC.getRoot();
			dstTC = parser.generateFromFile(new File(newFile));
			dst = dstTC.getRoot();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Action> getActions(){
		Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
		m.match();
		ActionGenerator g = new ActionGenerator(src, dst, m.getMappings());
		g.generate();
		return g.getActions(); // return the actions
	}
	
	public String getNewTreeString(){
		return src.toTreeString();
	}
	public String getOldTreeString(){
		return dst.toTreeString();
	}
	public void printActions(List<Action> actions){
		System.out.println(actions.size());
		for(Action a : actions){
			if(a instanceof Delete){
				System.out.print("Delete>");
				Delete delete = (Delete)a;
				System.out.println(delete.toString());
			}
			if(a instanceof Insert){
				System.out.print("Insert>");
				Insert insert = (Insert)a;
				System.out.println(insert.toString());
			}
			if(a instanceof Move){
				System.out.print("Move>");
				Move move = (Move)a;
				System.out.println(move.toString());
			}
			if(a instanceof Update){
				System.out.print("Update>");
				Update update = (Update)a;
				ITree updateNode = update.getNode();
				System.out.println("from "+updateNode.getLabel()+" to "+update.getValue());
			}
			System.out.println("--------------------------------------------");
			System.out.println(a.getNode().getLabel()+"/"+srcTC.getTypeLabel(a.getNode()));
			System.out.println(srcTC.getTypeLabel(a.getNode().getParent())+"/"+a.getNode().getParent().getLabel());
			System.out.println("============================================");
		}
	}
	public static void main(String[] args) {
//		String file1 = "src/test/java/resources/StringBuilderCase1.java";
//		String file2 = "src/test/java/resources/StringBuilderCase2.java";
//		String file1 = "src/test/java/resources/StringEquals1.java";
//		String file2 = "src/test/java/resources/StringEquals2.java";
		String file1 = "src/test/java/resources/CloseCase1.java";
		String file2 = "src/test/java/resources/CloseCase2.java";
		GumTreeDiffParser diff = new GumTreeDiffParser(file1,file2);
		diff.init();
		diff.printActions(diff.getActions());
	}

}
