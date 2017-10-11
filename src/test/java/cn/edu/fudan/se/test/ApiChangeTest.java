package cn.edu.fudan.se.test;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import cn.edu.fudan.se.apiChangeExtractor.ApiChangeExtractor;

public class ApiChangeTest {
	String repositoryPath1 = "D:/javaee/parser/ApiChangeExtractor";
	String repositoryPath2 = "D:/github/ChangeExtractor";
	ApiChangeExtractor apiExactor = new ApiChangeExtractor(repositoryPath1);
	@Test
	public void testExtractApiChange(){
		apiExactor.extractApiChange();
	}
	@Test
	public void testConstructData(){
		Map<String, Set<String>> jdkCall = apiExactor.constructData(new File("D:/ApiChangeExtractor.java"));
		if(jdkCall==null) return;
		for(String s : jdkCall.keySet()){
			System.out.println(s+"//"+jdkCall.get(s));
		}
		while(true){}
	}
}

