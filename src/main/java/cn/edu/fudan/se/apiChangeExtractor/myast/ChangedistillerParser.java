package cn.edu.fudan.se.apiChangeExtractor.myast;

import java.io.File;
import java.util.List;

//import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
//import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;
//import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
//import cn.edu.fudan.se.apiChangeExtractor.changedistiller.ChangeExtractor;

public class ChangedistillerParser {
//	public static void main(String[] args) {
//		ChangedistillerParser parser = new ChangedistillerParser();
//		String file1 = "src/test/java/resources/Test1.java";
//		String file2 = "src/test/java/resources/Test2.java";
//		ChangeExtractor ce = new ChangeExtractor();
//		List<SourceCodeChange> changes = ce.extractChangesInFile(new File(file1), new File(file2));
//		System.out.println(changes.size());
//		for(SourceCodeChange change :changes){
//			System.out.println(change.getChangeType());
//			System.out.println(change.getChangedEntity().getLabel()+"/"+change.getChangedEntity().getUniqueName());
//			System.out.println(change.getChangedEntity().getStartPosition()+"/"+change.getChangedEntity().getEndPosition());
//			System.out.println(change.toString());
//			if(change instanceof Update){
//				Update update = (Update)change;
//
//				System.out.println(update.getNewEntity().toString());
//			}
//			System.out.println(change.getParentEntity().toString());
//			System.out.println(change.getRootEntity().toString());
//			System.out.println(change.getRootEntity().getType().toString());
//			parser.printNodeRoot(change.getParentEntity());
//			
//			System.out.println("============================================");
//		}
//	}
//	public void printNodeRoot(SourceCodeEntity entry){
//
//		 System.out.println("***************************");
//		 for(SourceCodeEntity e : entry.getAssociatedEntities()){
//			 System.out.println(e.toString());
//		 }
//	}
}
