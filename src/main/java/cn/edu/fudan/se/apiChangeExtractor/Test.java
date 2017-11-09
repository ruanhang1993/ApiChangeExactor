package cn.edu.fudan.se.apiChangeExtractor;

import java.io.File;
import java.util.Map;

import cn.edu.fudan.se.apiChangeExtractor.bean.JdkSequence;

public class Test {

	public static void main(String[] args) {
		String repositoryPath1 = "D:/javaee/parser/ApiChangeExtractor";
		ApiChangeExtractor apiExtractor1 = new ApiChangeExtractor(repositoryPath1,-1);
		Map<Integer, JdkSequence> jdkCall = apiExtractor1.constructData(new File("D:/ApiChangeExtractor.java"));
	}

}
