package cn.edu.fudan.se.test;

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
}
