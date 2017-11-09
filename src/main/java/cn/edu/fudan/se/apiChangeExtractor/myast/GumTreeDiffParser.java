package cn.edu.fudan.se.apiChangeExtractor.myast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Addition;
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

	public static void main(String[] args) {
		String file1 = "src/test/java/resources/Test1.java";
		String file2 = "src/test/java/resources/Test2.java";
		try {
			Run.initGenerators();
			TreeContext srcTC = new JdtTreeGenerator().generateFromFile(new File(file1));
			ITree src = srcTC.getRoot();
			TreeContext dstTC = new JdtTreeGenerator().generateFromFile(new File(file2));
			ITree dst = dstTC.getRoot();
			System.out.println(src.toTreeString());
			Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
			m.match();
			ActionGenerator g = new ActionGenerator(src, dst, m.getMappings());
			g.generate();
			List<Action> actions = g.getActions(); // return the actions
			System.out.println(actions.size());
			for(Action a : actions){
//				if(a instanceof Addition){
//					System.out.print("Addition>");
//					Addition add = (Addition)a;
//					System.out.println(add.toString());
//				}
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
				System.out.println(a.getNode().getLabel()+"/"+srcTC.getTypeLabel(a.getNode()));
				System.out.println(a.getNode().getPos()+"/"+a.getNode().getEndPos());
				System.out.println(a.getNode().getHeight()+"/"+a.getNode().getDepth());
				System.out.println(srcTC.getTypeLabel(a.getNode().getParent())+"/"+a.getNode().getParent().getLabel());
				System.out.println("============================================");
			}
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
