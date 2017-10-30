package cn.edu.fudan.se.test;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import cn.edu.fudan.se.apiChangeExtractor.bean.ChangeFile;
import cn.edu.fudan.se.apiChangeExtractor.bean.ChangeLine;
import cn.edu.fudan.se.apiChangeExtractor.gitReader.GitReader;
import cn.edu.fudan.se.apiChangeExtractor.util.FileUtils;

public class GitTest {
	String repositoryPath1 = "D:/javaee/parser/ApiChangeExtractor";
	String repositoryPath2 = "D:/github/ChangeExtractor";
	String repositoryPath3 = "D:/github/spring-framework";
	String repositoryPath4 = "D:/github/h2o-3";
	String repositoryPath5 = "D:/github/checkstyle";
	GitReader reader = new GitReader(repositoryPath1);
	@Test
	public void testGetLastCommit(){
		RevCommit last = reader.getLastCommit();
		System.out.println(last.getFullMessage());
	}
	
	@Test
	public void testWalkCommit(){
		RevCommit last = reader.getLastCommit();
		reader.walkCommit(last);
	}
	
	@Test
	public void testGetChangeFiles(){
		RevCommit last = reader.getLastCommit();
		reader.getChangeFiles(last);
	}
	
	@Test
	public void testGetFileContentByCommitId(){
		reader.getFileContentByCommitId("69d371c442e74bd03ff56a0f4cdb220599c0167a", "src/test/resources/com/puppycrawl/tools/checkstyle/checks/misc/avoidescapedunicodecharacters/InputAllEscapedUnicodeCharacters.java");
	}
	
	@Test
	public void testGetCommits(){
		reader.getCommits();
	}
	
	@Test
	public void testGetTwoRevision(){
		String userDirPath = System.getProperty("user.dir");
		String tempDirPath = userDirPath + "/" + UUID.randomUUID().toString();
		File tempDir = new File(tempDirPath);
		tempDir.mkdirs();
		byte[] newContent = reader.getFileContentByCommitId("69d371c442e74bd03ff56a0f4cdb220599c0167a", "src/test/resources/com/puppycrawl/tools/checkstyle/checks/misc/avoidescapedunicodecharacters/InputAllEscapedUnicodeCharacters.java");
		byte[] oldContent = reader.getFileContentByCommitId("69d371c442e74bd03ff56a0f4cdb220599c0167a", "src/test/resources/com/puppycrawl/tools/checkstyle/checks/misc/avoidescapedunicodecharacters/InputAllEscapedUnicodeCharacters.java");
		String randomString = UUID.randomUUID().toString();
		File newFile = FileUtils.writeBytesToFile(newContent, tempDirPath, randomString + ".v1");
		File oldFile = FileUtils.writeBytesToFile(oldContent, tempDirPath, randomString + ".v2");
	}
	
	@Test
	public void testSeeDiff(){
		reader.getChangeFiles(reader.getOneCommit("4f3a70d1ab940e38d445a3cf73fcc74c8c248831"));
	}
	@Test
	public void testSeeChangeFile(){
		List<ChangeFile> files = reader.getChangeFiles(reader.getOneCommit("4f3a70d1ab940e38d445a3cf73fcc74c8c248831"));
		for(ChangeFile f:files){
			System.out.println("================================================================================================================================================");
			printChangeFile(f);
		}
	}
	public void printChangeFile(ChangeFile f){
		System.out.println("++++ "+f.getNewPath());
		System.out.println("---- "+f.getOldPath());
		for(ChangeLine l: f.getChangeLines()){
			if(!"CONTENT".equals(l.getType()))
				System.out.println(l.getLineNum()+"   "+l.getSequence());
			else
				System.out.println(l.getLineNum()+"/"+l.getOldNum()+"   "+l.getSequence());
		}
	}
}
