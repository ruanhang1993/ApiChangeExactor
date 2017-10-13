package cn.edu.fudan.se.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.edu.fudan.se.apiChangeExtractor.mybatis.bean.Apichange;
import cn.edu.fudan.se.apiChangeExtractor.mybatis.dao.ApichangeDao;

public class MybatisTest {
	ApichangeDao dao = new ApichangeDao();
	@Test
	public void testAddOneApiChange(){
		dao.insertOneApichange(new Apichange());
	}
	@Test
	public void testAddOneApiChangeList(){
		List<Apichange> list = new ArrayList<Apichange>();
		list.add(new Apichange());
		list.add(new Apichange());
		list.add(new Apichange());
		list.add(new Apichange());
		list.add(new Apichange());
		dao.insertApichangeList(list);
	}
}
