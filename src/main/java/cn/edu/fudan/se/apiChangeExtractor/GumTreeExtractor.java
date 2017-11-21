package cn.edu.fudan.se.apiChangeExtractor;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gumtreediff.actions.model.Action;

import cn.edu.fudan.se.apiChangeExtractor.bean.ChangeFile;
import cn.edu.fudan.se.apiChangeExtractor.gitReader.GitReader;
import cn.edu.fudan.se.apiChangeExtractor.gumtreeParser.GumTreeDiffParser;
import cn.edu.fudan.se.apiChangeExtractor.mybatis.bean.Repository;
import cn.edu.fudan.se.apiChangeExtractor.mybatis.dao.ApichangeDao;
import cn.edu.fudan.se.apiChangeExtractor.util.FileUtils;

public class GumTreeExtractor {
	private static final Logger logger = LoggerFactory.getLogger(GumTreeExtractor.class);
	
	private GitReader gitReader;
	private int repositoryId;
	private ApichangeDao dao;
	
	public GumTreeExtractor(String path, int repositoryId){
		gitReader = new GitReader(path);
		this.repositoryId = repositoryId;
		dao = new ApichangeDao();
	}
	public GumTreeExtractor(Repository repository){
		gitReader = new GitReader(repository.getAddress());
		this.repositoryId = repository.getRepositoryId();
		dao = new ApichangeDao();
	}
	
	public void extractActions(){
		System.out.println("repository "+repositoryId+" start extractor*****************************************************************************************************");
		String userDirPath = System.getProperty("user.dir");
		String tempDirPath = userDirPath + "/" + UUID.randomUUID().toString();
		File tempDir = new File(tempDirPath);
		tempDir.mkdirs();
		List<RevCommit> commits = gitReader.getCommits();
		if(commits==null) return;
		
		for(int i = 0; i < commits.size(); i++){
			if(commits.get(i).getParents().length==0) continue;
			System.out.println(i+"===="+commits.get(i).getName()+"=======================================================================================================================================");

			List<ChangeFile> changeFiles = gitReader.getChangeFilesId(commits.get(i));
			for(ChangeFile changeFile : changeFiles){
				byte[] newContent = gitReader.getFileByObjectId(true,changeFile.getNewBlobId());
				byte[] oldContent = gitReader.getFileByObjectId(false,changeFile.getOldBlobId());
				String randomString = UUID.randomUUID().toString();
				File newFile = FileUtils.writeBytesToFile(newContent, tempDirPath, randomString + ".v1");
				File oldFile = FileUtils.writeBytesToFile(oldContent, tempDirPath, randomString + ".v2");
				GumTreeDiffParser diff = new GumTreeDiffParser(oldFile, newFile);
				diff.init();
				List<Action> actions = diff.getActions();
				newFile.delete();
				oldFile.delete();
			}
		}
		tempDir.delete();
		System.out.println("repository "+repositoryId+" end extractor*****************************************************************************************************");
	}
}
