package com.cff.jpamapper.core.mapper;

import java.io.Serializable;
import java.util.Collection;

import org.apache.ibatis.annotations.Param;

public interface CrudMapper<T, ID extends Serializable>{
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	<S extends T> int save(S entity);

	/**
	 * Saves all given entities. no id save 
	 * <p>for single save the {@link save} can detect the id and auto save
	 * <p>to save Collection with id, define {@link saveAllWithId}
	 * @param entities
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	<S extends T> int saveAll(Collection<S> entities);
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	<S extends T> int update(@Param("object") S entity);
	
	/**
	 * update all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	<S extends T> int updateAll(@Param("object") S entity, @Param("list") Collection<ID> ids);

	/**
	 * Retrieves an entity by its id.
	 * 
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	T findOne(@Param("id") ID id);

	/**
	 * Returns whether an entity with the given id exists.
	 * 
	 * @param id must not be {@literal null}.
	 * @return true if an entity with the given id exists, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
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
	 * @param ids
	 * @return
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
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	int deleteEntity(@Param("object") T entity);

	/**
	 * Deletes the given entities.
	 * 
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
	 */
	int deleteBatch(@Param("list") Collection<ID> ids);

	/**
	 * Deletes all entities managed by the repository.
	 */
	int deleteAll();
}
