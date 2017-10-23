package cn.edu.fudan.se.apiChangeExtractor.mybatis.bean;

public class Apichange {
	private int apichangeId;
	private int repositoryId;
	private String commitId;
	private String parentCommitId;
	private String newFileName;
	private String oldFileName;
	private int lineNumber;
	private String changeType;
	private String content;
	private String completeClassName;
	private String methodName;
	private String parameter;
	public int getApichangeId() {
		return apichangeId;
	}
	public void setApichangeId(int apichangeId) {
		this.apichangeId = apichangeId;
	}
	public int getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(int repositoryId) {
		this.repositoryId = repositoryId;
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
	public String getNewFileName() {
		return newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	public String getOldFileName() {
		return oldFileName;
	}
	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCompleteClassName() {
		return completeClassName;
	}
	public void setCompleteClassName(String completeClassName) {
		this.completeClassName = completeClassName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String toString(){
		return this.repositoryId+"/"+this.commitId+"/"+this.parentCommitId+"/"+this.newFileName+"/"+this.oldFileName+"/"+this.lineNumber+"/"+this.changeType+"/"+this.content+"/"+this.completeClassName+"/"+this.methodName+"/"+this.parameter;
	}
}
