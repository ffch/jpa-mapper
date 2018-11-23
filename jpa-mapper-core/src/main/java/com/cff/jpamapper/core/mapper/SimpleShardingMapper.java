package com.cff.jpamapper.core.mapper;

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
	 * @param entity
	 * @return
	 */
	<S extends T> int save(S entity);
	
	/**
	 * 更新，必须设置分表字段值
	 * @param entity
	 * @return
	 */
	<S extends T> int update(@Param("object") S entity);
	
	/**
	 * 删除，必须设置分表字段值
	 * @param entity
	 * @return
	 */
	int delete(@Param("object") T entity);
	
	/**
	 * 根据分表字段查询所有,非空字段也作为条件查询
	 * @param entity
	 * @return
	 */
	Collection<T> findAll(@Param("start") Object start, @Param("end") Object end, @Param("distinct") boolean distinct);
	
	/**
	 * 根据指定的分表字段查询
	 * @param entity
	 * @return
	 */
	Collection<T> find(@Param("object") T entity);
	
	/**
	 * 根据分表字段及查询条件查询
	 * @param entity
	 * @param start
	 * @param end
	 * @return
	 */
	Collection<T> findRange(@Param("object") T entity, @Param("start") Object start, @Param("end") Object end, @Param("distinct") boolean distinct);
}
