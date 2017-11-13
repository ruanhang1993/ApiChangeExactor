package cn.edu.fudan.se.apiChangeExtractor.gumtreeParser;

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
import com.github.gumtreediff.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;

public class GumTreeDiffParser {
	String oldFile;
	String newFile;
	TreeContext srcTC;
	TreeContext dstTC;
	ITree src;
	ITree dst;
	List<Action> actions;
	MappingStore mapping;
	
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
			Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
			m.match();
			mapping = m.getMappings();
			ActionGenerator g = new ActionGenerator(src, dst, mapping);
			g.generate();
			actions = g.getActions();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Action> getActions(){
		return actions;
	}
	public MappingStore getMapping(){
		return mapping;
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
				ITree deleteNode = delete.getNode();
				System.out.println(prettyString(dstTC,deleteNode)+" from "+prettyString(dstTC,deleteNode.getParent()));
				System.out.println(delete.toString());
			}
			if(a instanceof Insert){
				System.out.print("Insert>");
				Insert insert = (Insert)a;
				ITree insertNode = insert.getNode();
				System.out.println(prettyString(dstTC,insertNode)+" to "+prettyString(dstTC,insertNode.getParent())+" at "+ insert.getPosition());
				System.out.println(insert.toString());
				System.out.println(insert.getParent()==insert.getNode().getParent());
			}
			if(a instanceof Move){
				System.out.print("Move>");
				Move move = (Move)a;
				ITree moveNode = move.getNode();
				System.out.println(prettyString(dstTC,moveNode)+" to "+prettyString(dstTC,moveNode.getParent())+" at "+ move.getPosition());
				System.out.println(move.toString());
			}
			if(a instanceof Update){
				System.out.print("Update>");
				Update update = (Update)a;
				ITree updateNode = update.getNode();
				System.out.println("from "+updateNode.getLabel()+" to "+update.getValue());
			}
			System.out.println("----------------Node----------------------------");
			System.out.println(dstTC.getTypeLabel(a.getNode())+"/"+a.getNode().getLabel());
			System.out.println(toTreeString(dstTC, a.getNode()));
			System.out.println("-----------------Parent---------------------------");
			System.out.println(dstTC.getTypeLabel(a.getNode().getParent())+"/"+a.getNode().getParent().getLabel());
			System.out.println(toTreeString(dstTC, a.getNode().getParent()));
			System.out.println("============================================");
		}
	}
	
	public String prettyString(TreeContext con, ITree node){
		return con.getTypeLabel(node)+":"+node.getLabel();
	}
	private String indent(ITree t) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < t.getDepth(); i++)
            b.append("\t");
        return b.toString();
    }
    public String toTreeString(TreeContext con, ITree tree) {
        StringBuilder b = new StringBuilder();
        for (ITree t : TreeUtils.preOrder(tree))
            b.append(indent(t) + prettyString(con, t) + "\n");
        return b.toString();
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
		MappingStore store = diff.getMapping();
	}

}
