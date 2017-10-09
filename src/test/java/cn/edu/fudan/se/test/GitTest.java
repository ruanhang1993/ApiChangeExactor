package cn.edu.fudan.se.test;

import java.io.IOException;

import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import cn.edu.fudan.se.apiChangeExactor.gitReader.GitReader;

public class GitTest {
	String repositoryPath = "D:/javaee/parser/ApiChangeExactor";
	GitReader reader = new GitReader(repositoryPath);
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
		try {
			reader.getChangeFiles(last);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetFileContentByCommitId(){
		reader.getFileContentByCommitId("159c4b82b941edf7a1612bf76fdce3f400ed753e", "src/main/java/cn/edu/fudan/se/ApiChangeExactor/Main.java");
	}
}
