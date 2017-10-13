package cn.edu.fudan.se.apiChangeExtractor.mybatis.mapper;

import java.util.List;

import cn.edu.fudan.se.apiChangeExtractor.mybatis.bean.Apichange;

public interface ApichangeMapper {
	public void insertApichange(Apichange apichange);
	public void insertApichangeList(List<Apichange> apichanges);
}
