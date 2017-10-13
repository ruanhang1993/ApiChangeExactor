package cn.edu.fudan.se.test;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import cn.edu.fudan.se.apiChangeExtractor.ApiChangeExtractor;
import cn.edu.fudan.se.apiChangeExtractor.bean.JdkSequence;
import cn.edu.fudan.se.apiChangeExtractor.bean.MethodCall;

public class ApiChangeTest {
	String repositoryPath1 = "D:/javaee/parser/ApiChangeExtractor";
	String repositoryPath2 = "D:/github/ChangeExtractor";
	ApiChangeExtractor apiExtractor = new ApiChangeExtractor(repositoryPath1);
	@Test
	public void testExtractApiChange(){
		apiExtractor.extractApiChange();
	}
	@Test
	public void testConstructData(){
		Map<Integer, JdkSequence> jdkCall = apiExtractor.constructData(new File("D:/ApiChangeExtractor.java"));
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
		apiExtractor.extractApiChangeByDiff();
	}
}

