package cn.edu.fudan.se.test;

import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import cn.edu.fudan.se.apiChangeExtractor.gitReader.GitReader;

public class GitTest {
	String repositoryPath1 = "D:/javaee/parser/ApiChangeExactor";
	String repositoryPath2 = "D:/github/ChangeExtractor";
	String repositoryPath3 = "D:/github/spring-framework";
	GitReader reader = new GitReader(repositoryPath3);
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
		reader.getFileContentByCommitId("159c4b82b941edf7a1612bf76fdce3f400ed753e", "src/main/java/cn/edu/fudan/se/ApiChangeExactor/Main.java");
	}
	
	@Test
	public void testGetCommits(){
		reader.getCommits();
	}
}
