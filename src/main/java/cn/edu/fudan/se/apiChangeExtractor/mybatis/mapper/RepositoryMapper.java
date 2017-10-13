package cn.edu.fudan.se.apiChangeExtractor.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.apiChangeExtractor.bean.Repository;

public interface RepositoryMapper {
    public long countAll();
    public List<Repository> selectAllRepository();
    public List<Repository> selectByName(String name);
    public Repository selectByPrimaryKey(Integer repositoryId);
    public List<Repository> selectLikeName(String name);
    
    public List<Repository> selectInScope(@Param("start")int start, @Param("count")int count);
}