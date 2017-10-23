package cn.edu.fudan.se.apiChangeExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.fudan.se.apiChangeExtractor.mybatis.bean.Repository;
import cn.edu.fudan.se.apiChangeExtractor.mybatis.dao.RepositoryDao;
import cn.edu.fudan.se.apiChangeExtractor.util.PathUtils;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	private ExecutorService service = new MyThreadPool(8, 8, 0, TimeUnit.MINUTES, queue);

	private RepositoryDao dao = new RepositoryDao();
	public static void main(String[] args) {
		Main main = new Main();
		main.extractRepositories(main.getTestData());
	}
	
	public List<Repository> getData(){
		return dao.selectAll();
	}
	public List<Repository> getData(int start, int end){
		return dao.selectInScope(start, end);
	}
	public List<Repository> getTestData(){
//		String repositoryPath1 = "D:/javaee/parser/ApiChangeExtractor";
//		String repositoryPath2 = "D:/github/ChangeExtractor";
//		String repositoryPath3 = "D:/github/SEDataExtractor";
//		String repositoryPath4 = "D:/javaee/LykProject";
//		String repositoryPath5 = "D:/github/checkstyle";
//		String repositoryPath6 = "D:/github/spring-framework";
		String repositoryPath7 = "D:/github/h2o-3";
//		Repository repository1 = new Repository(-1, repositoryPath1);
//		Repository repository2 = new Repository(-2, repositoryPath2);
//		Repository repository3 = new Repository(-3, repositoryPath3);
//		Repository repository4 = new Repository(-4, repositoryPath4);
//		Repository repository5 = new Repository(-5, repositoryPath5);
//		Repository repository6 = new Repository(-6, repositoryPath6);
		Repository repository7 = new Repository(-7, repositoryPath7);
		
		List<Repository> list = new ArrayList<>(); 
//		list.add(repository1);
//		list.add(repository2);
//		list.add(repository3);
//		list.add(repository4);
//		list.add(repository5);
//		list.add(repository6);
		list.add(repository7);
		return list;
	}
	
	public void extractRepositories(List<Repository> list){
		for(Repository r : list){
			RepositoryTask task = new RepositoryTask(r);
			service.submit(task);
		}
		service.shutdown();
	}
	public void extractRepositoriesInLine(List<Repository> list){
		for(Repository r : list){
			ApiChangeExtractor a = new ApiChangeExtractor(r);
			a.extractApiChangeByDiff();
		}
	}
}
