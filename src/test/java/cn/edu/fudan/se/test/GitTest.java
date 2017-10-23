package cn.edu.fudan.se.test;

import java.io.File;
import java.util.UUID;

import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import cn.edu.fudan.se.apiChangeExtractor.gitReader.GitReader;
import cn.edu.fudan.se.apiChangeExtractor.util.FileUtils;

public class GitTest {
	String repositoryPath1 = "D:/javaee/parser/ApiChangeExactor";
	String repositoryPath2 = "D:/github/ChangeExtractor";
	String repositoryPath3 = "D:/github/spring-framework";
	String repositoryPath4 = "D:/github/h2o-3";
	GitReader reader = new GitReader(repositoryPath4);
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
	
	@Test
	public void testGetTwoRevision(){
		String userDirPath = System.getProperty("user.dir");
		String tempDirPath = userDirPath + "/" + UUID.randomUUID().toString();
		File tempDir = new File(tempDirPath);
		tempDir.mkdirs();
		byte[] newContent = reader.getFileContentByCommitId("627362bceecb3f090495f7fc2d0c322b9defd35c", "h2o-core/src/main/java/water/Job.java");
		byte[] oldContent = reader.getFileContentByCommitId("9b8969d221475ea4e5e176df1c585f67ff46c400", "h2o-core/src/main/java/water/Job.java");
		String randomString = UUID.randomUUID().toString();
		File newFile = FileUtils.writeBytesToFile(newContent, tempDirPath, randomString + ".v1");
		File oldFile = FileUtils.writeBytesToFile(oldContent, tempDirPath, randomString + ".v2");
	}
	
	@Test
	public void testSeeDiff(){
		reader.getChangeFiles(reader.getOneCommit("627362bceecb3f090495f7fc2d0c322b9defd35c"));
	}
}
