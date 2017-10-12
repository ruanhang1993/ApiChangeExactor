package cn.edu.fudan.se.apiChangeExtractor.bean;

public class ChangeLine {
	private int lineNum;
	private String sequence;
	private String type;
	public ChangeLine(int lineNum, String sequence,String type){
		this.lineNum = lineNum;
		this.sequence=sequence;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLineNum() {
		return lineNum;
	}
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
}
