package cn.edu.fudan.se.test;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import cn.edu.fudan.se.apiChangeExtractor.ApiChangeExtractor;
import cn.edu.fudan.se.apiChangeExtractor.bean.JdkSequence;
import cn.edu.fudan.se.apiChangeExtractor.bean.MethodCall;

public class ApiChangeTest {
	String repositoryPath1 = "D:/javaee/parser/ApiChangeExtractor";
	String repositoryPath2 = "D:/github/checkstyle";
	String repositoryPath3 = "D:/github/spring-framework";
	ApiChangeExtractor apiExtractor1 = new ApiChangeExtractor(repositoryPath1,-1);
	ApiChangeExtractor apiExtractor2 = new ApiChangeExtractor(repositoryPath2,-2);
	ApiChangeExtractor apiExtractor3 = new ApiChangeExtractor(repositoryPath3,-3);
	@Test
	public void testExtractApiChange(){
		apiExtractor2.extractApiChange();
		apiExtractor3.extractApiChange();
	}
	@Test
	public void testConstructData(){
		Map<Integer, JdkSequence> jdkCall = apiExtractor1.constructData(new File("D:/ApiChangeExtractor.java"));
		if(jdkCall==null) return;
		int count = 0;
		for(Integer i : jdkCall.keySet()){
			count++;
			JdkSequence j = jdkCall.get(i);
			System.out.println(i+"//"+j.getStmt());
			for(MethodCall s: j.getApiList()){
				System.out.print("|| c="+s.getCompleteClassName()+" m="+s.getMethodName()+" p="+s.getParameter());
			}
			System.out.println();
		}
		System.out.println(count);
		while(true){}
	}
	
	@Test
	public void testExtractApiChangeByDiff(){
//		apiExtractor2.extractApiChangeByDiff();
		apiExtractor3.extractApiChangeByDiff();
	}
	
	@Test
	public void testExtractApiChangeByDiffAfterCommit(){
		apiExtractor3.extractApiChangeByDiffAfterCommit("f6d2fe471a26fb6ff5894480f50dd55365e62f06");
	}
}

