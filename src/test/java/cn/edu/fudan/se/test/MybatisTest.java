package cn.edu.fudan.se.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.edu.fudan.se.apiChangeExtractor.mybatis.bean.Apichange;
import cn.edu.fudan.se.apiChangeExtractor.mybatis.bean.Repository;
import cn.edu.fudan.se.apiChangeExtractor.mybatis.dao.ApichangeDao;
import cn.edu.fudan.se.apiChangeExtractor.mybatis.dao.RepositoryDao;

public class MybatisTest {
	ApichangeDao apichangeDao = new ApichangeDao();
	RepositoryDao repositoryDao = new RepositoryDao();
	@Test
	public void testAddOneApiChange(){
		apichangeDao.insertOneApichange(new Apichange());
	}
	@Test
	public void testAddApiChangeList(){
		List<Apichange> list = new ArrayList<Apichange>();
		list.add(new Apichange());
		list.add(new Apichange());
		list.add(new Apichange());
		list.add(new Apichange());
		list.add(new Apichange());
		apichangeDao.insertApichangeList(list);
	}
	
	@Test
	public void testSelectAllRepository(){
		List<Repository> list = repositoryDao.selectAll();
		System.out.println(list.size());
	}
	@Test
	public void testSelectRepositoryInScope(){
		int start = 1;
		int end = 5;
		List<Repository> list = repositoryDao.selectInScope(start, end);
		for(Repository r : list)
			System.out.println(r.getWebsite()+": "+r.getAddress());
		String repositoryPath1 = "D:/javaee/parser/ApiChangeExtractor";
		String repositoryPath2 = "D:/github/ChangeExtractor";
		String repositoryPath3 = "D:/github/SEDataExtractor";
		String repositoryPath4 = "D:/javaee/LykProject";
//		String repositoryPath5 = "D:/github/checkstyle";
		Repository repository1 = new Repository(-1, repositoryPath1);
		Repository repository2 = new Repository(-2, repositoryPath2);
		Repository repository3 = new Repository(-3, repositoryPath3);
		Repository repository4 = new Repository(-4, repositoryPath4);
//		Repository repository5 = new Repository(-5, repositoryPath5);
		System.out.println(repository1.getWebsite()+": "+repository1.getAddress());
		System.out.println(repository2.getWebsite()+": "+repository2.getAddress());
		System.out.println(repository3.getWebsite()+": "+repository3.getAddress());
		System.out.println(repository4.getWebsite()+": "+repository4.getAddress());
	}
}
