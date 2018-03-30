package cn.edu.fudan.se.apiChangeExtractor.util;

public class PathUtils {
	public static String changeWebsite2Path(String s){
		if(s==null) return s;
		String temp = s.replace("https://github.com/", "");
		temp = temp.replace("/", "-");
		return temp;
	}
	
	public static String getUnitName(String s){
		if(s==null) return s;
		String[] temp = s.replace("\\\\", "/").split("/");
		String t = temp[temp.length-1];
		temp = t.split(".");
		t = temp[0];
		return t+".java";
	}
}
