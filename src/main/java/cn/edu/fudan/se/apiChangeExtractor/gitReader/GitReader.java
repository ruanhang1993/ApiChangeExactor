package cn.edu.fudan.se.apiChangeExtractor.gitReader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.se.apiChangeExtractor.ApiChangeExtractor;
import cn.edu.fudan.se.apiChangeExtractor.bean.ChangeFile;
import cn.edu.fudan.se.apiChangeExtractor.bean.ChangeLine;

public class GitReader {
	private static final Logger logger = LoggerFactory.getLogger(GitReader.class);
	private Git git;
	private Repository repository;
	private RevWalk revWalk;
	public static final String ADD ="ADD";
	public static final String DELETE ="DELETE";

	public GitReader(String repositoryPath) {
		try {
			git = Git.open(new File(repositoryPath));
			repository = git.getRepository();
			revWalk = new RevWalk(repository);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("repositoryPath: "+repositoryPath+" Error");
			logger.info(e.getMessage());
		}
	}
	
	public List<RevCommit> getCommits(){
		List<RevCommit> allCommits = null;
		try {
			Iterable<RevCommit> commits = git.log().call();
			allCommits = new ArrayList<RevCommit>();
			int count = 0;
			for(RevCommit commit : commits){
				if(commit.getParentCount()==0)
					System.out.println(count+"/"+commit.getName());
				allCommits.add(commit);
				count++;
			}
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return allCommits;
	}
	
	public RevCommit getOneCommit(String sha){
		ObjectId objId;
		try {
			objId = repository.resolve(sha);
			if (objId == null) {
				System.err.println("The commit: " + sha + " does not exist.");
				return null;
			}
			return revWalk.parseCommit(objId);
		} catch (RevisionSyntaxException e) {
			e.printStackTrace();
		} catch (AmbiguousObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<ChangeFile> getChangeFiles(RevCommit commit){
    	List<ChangeFile> changeFiles= new ArrayList<ChangeFile>();
		
		AbstractTreeIterator newTree = prepareTreeParser(commit);
		if(commit.getParentCount()==0) return changeFiles;
    	AbstractTreeIterator oldTree = prepareTreeParser(commit.getParent(0));
    	List<DiffEntry> diff= null;
		try {
			diff = git.diff().setOldTree(oldTree).setNewTree(newTree).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
        //每一个diffEntry都是文件版本之间的变动差异
		for (DiffEntry diffEntry : diff) {
			//DiffEntry.ChangeType.MODIFY.toString().equals(diffEntry.getChangeType().toString())&&
			if(diffEntry.getNewPath()!=null&&diffEntry.getNewPath().endsWith(".java")){
				changeFiles.add(new ChangeFile(diffEntry.getChangeType().toString(), diffEntry.getOldPath(), diffEntry.getNewPath(), 
	        			commit.getName(), (commit.getParents()[0]).getName(), diffEntry.getNewId().toObjectId(), diffEntry.getOldId().toObjectId()));

				String fName = "aa"+(new Random()).nextInt(1000);
				while(new File(fName).exists()){
					fName = "aa"+(new Random()).nextInt(1000);
				}
				BufferedOutputStream out =null;
	            try {
	            	out = new BufferedOutputStream(new FileOutputStream(fName));
					DiffFormatter df = new DiffFormatter(out);
	                df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
	                df.setRepository(git.getRepository());
	            	df.format(diffEntry);
	            	out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						if(out!=null)
							out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	            
	            insertAllAddAndDelete(commit.getName(),changeFiles.get(changeFiles.size()-1),fName);
				File file = new File(fName);
				file.delete();
			}
		} 
       return changeFiles;
	}
	public ArrayList<Integer> getRange(String line){
		ArrayList<Integer> ret = new ArrayList<>();
		if(line.length()>2&&"@@".equals(line.substring(0, 2))){
			String pattern = "\\d+";
			Pattern rPattern = Pattern.compile(pattern);
			Matcher matcher = rPattern.matcher(line);
			while(matcher.find()){
				ret.add(Integer.valueOf(matcher.group(0)));
			}
		}
		return ret;
		
	}
	private void insertAllAddAndDelete(String name, ChangeFile changeFile, String fName) {
		BufferedReader inputStream =null;
		try {
        	String str;
            inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(fName),"UTF-8"));
            ArrayList<Integer> range = new ArrayList<>();
            boolean go = false;
            
			while((str = inputStream.readLine()) != null){
				ArrayList<Integer> tempRange = getRange(str);
				if(tempRange.size()>0){
					go = false;
					range.clear();
					range.addAll(tempRange);
				}else {
					if(range.size()!=4){
						continue;
					}
					if(range.get(1)==0&&range.get(3)==0){
						go = true;
					}
					if(!go){
						if("+".equals(str.substring(0, 1))){
							changeFile.getChangeLines().add(new ChangeLine(range.get(2), str.replace("\\", "\\\\").replace("\"", "\\\""), ADD));
							range.set(2, range.get(2)+1);
							range.set(3, range.get(3)-1);
						}else if("-".equals(str.substring(0, 1))){
							changeFile.getChangeLines().add(new ChangeLine(range.get(0), str.replace("\\", "\\\\").replace("\"", "\\\""), DELETE));
							range.set(0, range.get(0)+1);
							range.set(1, range.get(1)-1);
						}
						else{
							if(range.get(1)!=0){
								range.set(0, range.get(0)+1);
								range.set(1, range.get(1)-1);
							}
							if(range.get(3)!=0){
								range.set(2, range.get(2)+1);
								range.set(3, range.get(3)-1);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(inputStream!=null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public AbstractTreeIterator prepareTreeParser(RevCommit commit){
    	try (RevWalk walk = new RevWalk(repository)) {
    		RevCommit temp = walk.parseCommit( commit.getId() );
            RevTree tree = walk.parseTree(temp.getTree().getId());

            CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
            try (ObjectReader oldReader = repository.newObjectReader()) {
                oldTreeParser.reset(oldReader, tree.getId());
            }
            walk.dispose();
            return oldTreeParser;
	    }catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
	public void walkCommit(RevCommit commit){
		System.out.println("\ncommit: " + commit.getName());
	    try (TreeWalk treeWalk = new TreeWalk(repository)) {
	        treeWalk.addTree(commit.getTree());
	        treeWalk.setRecursive(true);
	        while (treeWalk.next()) {
	            System.out.println("filename: " + treeWalk.getPathString());
	            ObjectId objectId = treeWalk.getObjectId(0);
	            ObjectLoader loader = repository.open(objectId);
	            loader.copyTo(System.out);
	        }
	    } catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (CorruptObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	public RevCommit getLastCommit(){
		RevCommit lastCommit = null;
		try {
			Iterable<RevCommit> commits = git.log().setMaxCount(1).call();
			for(RevCommit commit:commits){
				lastCommit = commit;
			}
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return lastCommit;
	}

	public byte[] getFileContentByCommitId(String commitId, String filePath) {
		if (commitId == null || filePath == null) {
			System.err.println("revisionId or fileName is null");
			return null;
		}
		if (repository == null || git == null || revWalk == null) {
			System.err.println("git repository is null..");
			return null;
		}

		try {
			ObjectId objId = repository.resolve(commitId);
			if (objId == null) {
				System.err.println("The commit: " + commitId + " does not exist.");
				return null;
			}
			RevCommit revCommit = revWalk.parseCommit(objId);
			if (revCommit != null) {
				RevTree revTree = revCommit.getTree();
				TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, revTree);
				ObjectId blobId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(blobId);
				byte[] bytes = loader.getBytes();
				return bytes;
			} else {
				System.err.println("Cannot found file(" + filePath + ") in commit (" + commitId + "): " + revWalk);
			}
		} catch (RevisionSyntaxException e) {
			e.printStackTrace();
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (AmbiguousObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public byte[] getFileByObjectId(boolean isNewFile, ObjectId blobId) {
		if("0000000000000000000000000000000000000000".equals(blobId.getName())) return new byte[0];
		ObjectLoader loader;
		try {
			loader = repository.open(blobId);
			byte[] bytes = loader.getBytes();
//			System.out.println("-------------------------------"+isNewFile+"-----------------------");
//			loader.copyTo(System.out);
			return bytes;
		} catch (MissingObjectException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
