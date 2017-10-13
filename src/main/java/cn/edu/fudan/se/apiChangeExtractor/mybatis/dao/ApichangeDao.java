package cn.edu.fudan.se.apiChangeExtractor.mybatis.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.edu.fudan.se.apiChangeExtractor.mybatis.bean.Apichange;
import cn.edu.fudan.se.apiChangeExtractor.mybatis.mapper.ApichangeMapper;

public class ApichangeDao {
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();
	private static class SingletonHolder {  
		private static ApichangeDao singleton;
		static {
			singleton = new ApichangeDao();
		}
	}
	public static ApichangeDao getInstance(){
		return SingletonHolder.singleton;
	}
	
	public void insertOneApichange(Apichange apichange){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			sqlSession.getMapper(ApichangeMapper.class).insertApichange(apichange);;
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
	}
	public void insertApichangeList(List<Apichange> apichanges){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			sqlSession.getMapper(ApichangeMapper.class).insertApichangeList(apichanges);;
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
	}
}
