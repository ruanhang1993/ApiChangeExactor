package cn.edu.fudan.se.apiChangeExactor.bean;

import org.eclipse.jgit.lib.ObjectId;

public class ChangeFile {
	private String changeType;
	private String oldPath;
	private String newPath;
	private String commitId;
	private String parentCommitId;
	private ObjectId newBlobId;
	private ObjectId oldBlobId;
	public ChangeFile(String changeType, String oldPath, String newPath, String commitId, String parentCommitId, ObjectId newBlobId, ObjectId oldBlobId){
		this.changeType=changeType;
		this.commitId=oldPath;
		this.newPath=newPath;
		this.oldPath=commitId;
		this.parentCommitId=parentCommitId;
		this.newBlobId = newBlobId;
		this.oldBlobId = oldBlobId;
	}
	
	public ObjectId getNewBlobId() {
		return newBlobId;
	}

	public void setNewBlobId(ObjectId newBlobId) {
		this.newBlobId = newBlobId;
	}

	public ObjectId getOldBlobId() {
		return oldBlobId;
	}

	public void setOldBlobId(ObjectId oldBlobId) {
		this.oldBlobId = oldBlobId;
	}

	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public String getOldPath() {
		return oldPath;
	}
	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}
	public String getNewPath() {
		return newPath;
	}
	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public String getParentCommitId() {
		return parentCommitId;
	}
	public void setParentCommitId(String parentCommitId) {
		this.parentCommitId = parentCommitId;
	}
	
}
