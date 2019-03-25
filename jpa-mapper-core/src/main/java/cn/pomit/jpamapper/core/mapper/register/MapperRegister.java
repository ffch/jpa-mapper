package cn.pomit.jpamapper.core.mapper.register;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Table;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;

import cn.pomit.jpamapper.core.annotation.Many;
import cn.pomit.jpamapper.core.annotation.One;
import cn.pomit.jpamapper.core.annotation.ShardingKey;
import cn.pomit.jpamapper.core.entity.JoinEntity;
import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.ShardingEntity;
import cn.pomit.jpamapper.core.exception.JpaMapperException;
import cn.pomit.jpamapper.core.mapper.JMapper;
import cn.pomit.jpamapper.core.mapper.PagingAndSortingMapper;
import cn.pomit.jpamapper.core.mapper.SimpleShardingMapper;
import cn.pomit.jpamapper.core.mapper.builder.JpaMapperAnnotationBuilder;
import cn.pomit.jpamapper.core.util.ReflectUtil;
import cn.pomit.jpamapper.core.util.StringUtil;

public class MapperRegister {
	private static final Log LOGGER = LogFactory.getLog(MapperRegister.class);
	private Class<?> mapper;
	private List<Method> registerMethod = new ArrayList<>();
	private Configuration configuration;

	public static final int NO_MAPPER = -1;
	public static final int CRUD_MAPPER = 0;
	public static final int SHARDING_MAPPER = 2;
	public static final int PAGESORT_MAPPER = 1;
	
	int type = NO_MAPPER;

	public MapperRegister(Class<?> mapper, Configuration configuration) {
		this.mapper = mapper;
		this.configuration = configuration;
		scanMappers();
	}

	private void scanMappers() {
		Method[] methods = mapper.getMethods();
		for (Method method : methods) {
			if (method.getAnnotations() == null || method.getAnnotations().length < 1) {
				registerMethod.add(method);
			}
		}
	}

	public void genMappedStatement(String databaseName) {
		type = checkMapperType();
		if (type == NO_MAPPER)
			return;

		JpaMapperAnnotationBuilder jpaMapperAnnotationBuilder = new JpaMapperAnnotationBuilder(configuration, mapper);
		JpaModelEntity jpaModelEntity = parseModel();
		jpaModelEntity.setDatabaseName(databaseName);
		jpaMapperAnnotationBuilder.setJpaModelEntity(jpaModelEntity);
		for (Method method : registerMethod) {
			jpaMapperAnnotationBuilder.parseStatement(method);
		}
	}

	private int checkMapperType() {
		Class<?> interfases[] = mapper.getInterfaces();
		if (interfases == null || interfases.length < 1) {
			return NO_MAPPER;
		}
		for (Class<?> interfase : interfases) {
			if (ReflectUtil.checkTypeFit(interfase, JMapper.class)) {
				if (interfase.equals(SimpleShardingMapper.class)) {
					return SHARDING_MAPPER;
				} else if (interfase.equals(PagingAndSortingMapper.class)) {
					return PAGESORT_MAPPER;
				} else {
					return CRUD_MAPPER;
				}
			}
		}
		return NO_MAPPER;
	}

	/**
	 * 解析Mapper的泛型实体
	 * @return JpaModelEntity实体信息
	 */
	private JpaModelEntity parseModel() {
		JpaModelEntity jpaModelEntity = new JpaModelEntity();
		if (type == SHARDING_MAPPER) {
			jpaModelEntity.setSharding(true);
		} else if (type == PAGESORT_MAPPER) {
			jpaModelEntity.setPageSort(true);
		}
		Class<?> entity = ReflectUtil.findGenericClass(mapper);
		if (entity == null) {
			throw new JpaMapperException("未能获取到Mapper的泛型类型");
		}
		Table tableAnnotation = entity.getAnnotation(Table.class);
		String tableName = entity.getSimpleName();
		jpaModelEntity.setTargertEntity(entity);
		jpaModelEntity.setId(entity.getSimpleName());
		if (tableAnnotation != null) {
			tableName = tableAnnotation.name();
		}
		jpaModelEntity.setTableName(tableName);

		Field fields[] = entity.getDeclaredFields();
		for (Field field : fields) {

			Id id = field.getAnnotation(Id.class);
			boolean isId = false;
			if (id != null)
				isId = true;

			// 联表注解
			if (field.getAnnotation(JoinColumns.class) != null || field.getAnnotation(JoinColumn.class) != null) {
				if (jpaModelEntity.isJoin())
					throw new JpaMapperException("JoinColumn(s)只能用一次哦！");
				JoinEntity joinEntity = new JoinEntity();

				One one = field.getAnnotation(One.class);
				Many many = field.getAnnotation(Many.class);
				
				joinEntity.setEntityName(field.getName());
				if (one != null) {
					joinEntity.setMappingType(JoinEntity.ONE);
					joinEntity.setFetchType(one.fetchType());
					Class<?> typeField = field.getType();
					if(ReflectUtil.isCollection(typeField)){
						throw new JpaMapperException("@One注解不能用在集合类属性上");
					}
					joinEntity.setEntityType(typeField);
					
				} else if (many != null) {
					joinEntity.setMappingType(JoinEntity.MANY);
					joinEntity.setFetchType(many.fetchType());
					Class<?> typeField = ReflectUtil.findFeildGenericClass(field);
					if (entity == null) {
						throw new JpaMapperException("未能获取到Many注解的泛型类型");
					}
					joinEntity.setEntityType(typeField);
				} else {
					throw new JpaMapperException("JoinColumn(s)需要搭配cn.pomit.jpamapper.core.annotation.One(Many)一起使用哦！");
				}
				Map<String, String> joinColumns = new HashMap<>();
				JoinColumns joinColumnsAnno = field.getAnnotation(JoinColumns.class);
				if (joinColumnsAnno != null) {
					JoinColumn[] joinColumnArr = joinColumnsAnno.value();
					for (JoinColumn item : joinColumnArr) {
						joinColumns.put(item.name(), item.referencedColumnName());
					}
				}
				JoinColumn joinColumnAnno = field.getAnnotation(JoinColumn.class);
				if (joinColumnAnno != null) {
					joinColumns.put(joinColumnAnno.name(), joinColumnAnno.referencedColumnName());
				}
				if (joinColumns.size() > 0) {
					LOGGER.debug("提示：检测到" + joinColumns.size() + "个联表字段");
					joinEntity.setJoinColumns(joinColumns);
					jpaModelEntity.setJoinEntity(joinEntity);
					jpaModelEntity.setJoin(true);
				} 
			}

			Column columnAnnotation = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				if (!isId)
					continue;
			}
			
			// 判断是否分表
			if (jpaModelEntity.isSharding()) {
				ShardingKey shardingKey = field.getAnnotation(ShardingKey.class);
				if (shardingKey != null) {
					String entityFullName = entity.getCanonicalName();
					ShardingEntity shardingEntity = new ShardingEntity(shardingKey, fieldName, fieldDeclaredName,
							entityFullName);
					jpaModelEntity.setShardingEntity(shardingEntity);
					continue;
				}
			}
			
			if (isId) {
				jpaModelEntity.setHasId(true);
				jpaModelEntity.setIdName(fieldName);
				jpaModelEntity.setIdColumn(fieldDeclaredName);
				jpaModelEntity.setIdField(field);
			} else {
				jpaModelEntity.addField(fieldName, fieldDeclaredName);
				jpaModelEntity.addFieldType(fieldName, field.getType().getSimpleName());
			}
		}
		if(!jpaModelEntity.isHasId()){
			throw new JpaMapperException("JpaMapper要求必须有ID字段！");
		}
		return jpaModelEntity;
	}
}
