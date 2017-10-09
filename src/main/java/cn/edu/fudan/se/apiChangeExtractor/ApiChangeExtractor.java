package cn.edu.fudan.se.apiChangeExtractor;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.apiChangeExtractor.bean.ChangeFile;
import cn.edu.fudan.se.apiChangeExtractor.changedistiller.ChangeExtractor;
import cn.edu.fudan.se.apiChangeExtractor.gitReader.GitReader;
import cn.edu.fudan.se.apiChangeExtractor.util.FileUtils;

public class ApiChangeExtractor {
	private GitReader gitReader;
	private ChangeExtractor changeExactor;
	public ApiChangeExtractor(String path){
		changeExactor = new ChangeExtractor();
		gitReader = new GitReader(path);
	}
	
	public void extractApiChange(){
		String userDirPath = System.getProperty("user.dir");
		String tempDirPath = userDirPath + "/" + UUID.randomUUID().toString();
		File tempDir = new File(tempDirPath);
		tempDir.mkdirs();
		System.out.println(tempDirPath);
		List<RevCommit> commits = gitReader.getCommits();
		for(RevCommit commit : commits){
			System.out.println("***************************************************************************");
			List<ChangeFile> changeFiles = gitReader.getChangeFiles(commit);
			for(ChangeFile changeFile : changeFiles){
				if(DiffEntry.ChangeType.MODIFY.toString().equals(changeFile.getChangeType())&&changeFile.getNewPath().endsWith(".java")){
					System.out.println("===============================================");
					System.out.println(changeFile.getNewPath());
					byte[] newContent = gitReader.getFileByObjectId(changeFile.getNewBlobId());
					byte[] oldContent = gitReader.getFileByObjectId(changeFile.getOldBlobId());
					String randomString = UUID.randomUUID().toString();
					File newFile = FileUtils.writeBytesToFile(newContent, tempDirPath, randomString + ".v1");
					File oldFile = FileUtils.writeBytesToFile(oldContent, tempDirPath, randomString + ".v2");
					List<SourceCodeChange> changes = changeExactor.extractChangesInFile(oldFile, newFile);
					for(SourceCodeChange change:changes){
						System.out.println(change.toString());
					}
					newFile.delete();
					oldFile.delete();
				}
			}
		}
		tempDir.delete();
	}
}
