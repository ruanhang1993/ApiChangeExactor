package cn.edu.fudan.se.apiChangeExtractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);  
	public static void main(String[] args) {
		logger.info("*****************start*************************");
		String repositoryPath3 = "D:/github/spring-framework";
		int id = -3;
		try{
			ApiChangeExtractor apiExtractor3 = new ApiChangeExtractor(repositoryPath3, id);
			apiExtractor3.extractApiChangeByDiff();
		}catch(Exception e){
			logger.info("repository "+id+" debug:");
			logger.info(e.getMessage());
			e.printStackTrace();
		}catch(Error e){
			logger.info("repository "+id+" error:");
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		logger.info("*****************end*************************");
	}

}
