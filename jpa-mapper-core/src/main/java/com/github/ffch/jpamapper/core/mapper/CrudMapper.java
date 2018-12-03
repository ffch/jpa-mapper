package com.github.ffch.jpamapper.core.mapper;

import java.io.Serializable;
import java.util.Collection;

import org.apache.ibatis.annotations.Param;

public interface CrudMapper<T, ID extends Serializable> extends JMapper<T, ID>{
	
	/**
	 * 保存实体
	 * @param entity 实体
	 * @return 保存成功数
	 */
	int save(T entity);

	/**
	 * 批量保存
	 * @param entities 实体列表
	 * @return 成功数
	 */
	int saveAll(Collection<T> entities);
	
	/**
	 * 更新实体
	 * @param entity 实体，不为null的全部更新
	 * @return 成功数
	 */
	int update(@Param("object") T entity);
	
	/**
	 * 更新所有
	 * @param entity 更新实体，不为null的全部更新
	 * @param ids id列表
	 * @return 更新数量
	 */
	int updateAll(@Param("object") T entity, @Param("list") Collection<ID> ids);

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return 实体
	 */
	T findOne(@Param("id") ID id);

	/**
	 * 根据id判断是否存在
	 * @param id 主键
	 * @return true 存在 false 不存在
	 */
	boolean exists(@Param("id") ID id);

	/**
	 * Returns all instances of the type.
	 * 
	 * @return all entities
	 */
	Collection<T> findAll();

	/**
	 * Returns all instances of the type with the given IDs.
	 * 
	 * @param ids id列表
	 * @return 实体列表
	 */
	Collection<T> findBatch(@Param("list") Collection<ID> ids);

	/**
	 * Returns the number of entities available.
	 * 
	 * @return the number of entities
	 */
	long count();

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	int delete(@Param("id") ID id);

	/**
	 * Deletes a given entity.
	 * 
	 * @param entity 实体
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 * @return 删除数量
	 */
	int deleteEntity(@Param("object") T entity);

	/**
	 * 批量删除
	 * @param ids id列表
	 * @return 成功数
	 */
	int deleteBatch(@Param("list") Collection<ID> ids);

	/**
	 * 批量删除
	 * @return 成功数
	 */
	int deleteAll();
}
