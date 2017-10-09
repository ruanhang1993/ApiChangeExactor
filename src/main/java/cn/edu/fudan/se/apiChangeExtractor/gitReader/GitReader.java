package cn.edu.fudan.se.apiChangeExtractor.gitReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
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

import cn.edu.fudan.se.apiChangeExtractor.bean.ChangeFile;

public class GitReader {
	private Git git;
	private Repository repository;
	private RevWalk revWalk;

	public GitReader(String repositoryPath) {
		try {
			git = Git.open(new File(repositoryPath));
			repository = git.getRepository();
			revWalk = new RevWalk(repository);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<RevCommit> getCommits(){
		List<RevCommit> allCommits = null;
		try {
			Iterable<RevCommit> commits = git.log().call();
			allCommits = new ArrayList<RevCommit>();
			for(RevCommit commit : commits){
				allCommits.add(commit);
			}
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return allCommits;
	}
	public List<ChangeFile> getChangeFiles(RevCommit commit){
    	List<ChangeFile> changeFiles= new ArrayList<ChangeFile>();
		
		AbstractTreeIterator newTree = prepareTreeParser(commit);
    	AbstractTreeIterator oldTree = prepareTreeParser(commit.getParents()[0]);
    	List<DiffEntry> diff= null;
		try {
			diff = git.diff().setOldTree(oldTree).setNewTree(newTree).setShowNameAndStatusOnly(true).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
        //每一个diffEntry都是文件版本之间的变动差异
        for (DiffEntry diffEntry : diff) {
        	changeFiles.add(new ChangeFile(diffEntry.getChangeType().toString(), diffEntry.getOldPath(), diffEntry.getNewPath(), 
        			commit.getName(), (commit.getParents()[0]).getName(), diffEntry.getNewId().toObjectId(), diffEntry.getOldId().toObjectId()));
       } 
       return changeFiles;
	}
	
	public AbstractTreeIterator prepareTreeParser(RevCommit commit){
    	System.out.println(commit.getId());
    	try (RevWalk walk = new RevWalk(repository)) {
    		System.out.println(commit.getTree().getId());
            RevTree tree = walk.parseTree(commit.getTree().getId());

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
				
//				InputStream input = FileUtils.open(blobId, repository);
//				byte[] bytes = FileUtils.toByteArray(input);
//				return bytes;
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
	public byte[] getFileByObjectId(ObjectId blobId) {
		ObjectLoader loader;
		try {
			loader = repository.open(blobId);
			byte[] bytes = loader.getBytes();
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
