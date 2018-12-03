package com.github.ffch.jpamapper.core.mapper;

import java.io.Serializable;
import java.util.Collection;

import org.apache.ibatis.annotations.Param;

/**
 * 简单分表
 * @author fufei
 *
 * @param <T>
 * @param <ID>
 */
public interface SimpleShardingMapper<T, ID extends Serializable>  extends JMapper<T, ID>{
	/**
	 * 保存，必须设置分表字段值
	 * @param entity 实体
	 * @return 成功数
	 */
	int save(T entity);
	
	/**
	 * 更新，必须设置分表字段值
	 * @param entity 实体
	 * @return 成功数
	 */
	int update(@Param("object") T entity);
	
	/**
	 * 删除，必须设置分表字段值
	 * @param entity 实体
	 * @return 成功数
	 */
	int delete(@Param("object") T entity);
	
	/**
	 * 根据分表字段查询所有,非空字段也作为条件查询
	 * @param start 开始条件
	 * @param end 结束条件
	 * @param distinct 是否distinct
	 * @return 实体列表
	 */
	Collection<T> findAll(@Param("start") Object start, @Param("end") Object end, @Param("distinct") boolean distinct);
	
	/**
	 * 根据指定的分表字段查询
	 * @param entity 实体条件
	 * @return 实体列表
	 */
	Collection<T> find(@Param("object") T entity);
	
	/**
	 * 根据分表字段及查询条件查询
	 * @param entity 实体条件
	 * @param start 开始条件
	 * @param end 结束条件
	 * @param distinct 是否distinct
	 * @return 实体列表
	 */
	Collection<T> findRange(@Param("object") T entity, @Param("start") Object start, @Param("end") Object end, @Param("distinct") boolean distinct);
}
