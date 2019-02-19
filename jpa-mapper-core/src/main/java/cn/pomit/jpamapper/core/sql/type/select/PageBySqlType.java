package cn.pomit.jpamapper.core.sql.type.select;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.MethodParameters;
import cn.pomit.jpamapper.core.exception.JpaMapperException;
import cn.pomit.jpamapper.core.helper.MethodTypeHelper;
import cn.pomit.jpamapper.core.sql.helper.PageAndSortSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractPageSortSqlType;
import cn.pomit.jpamapper.core.util.StringUtil;

public class PageBySqlType extends AbstractPageSortSqlType {

	public static final PageBySqlType INSTANCE = new PageBySqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(PageAndSortSqlHelper.pageSql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.fromSql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.conditionRegBySql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Override
	public boolean pageSupport() {
		return true;
	}

	@Override
	public List<MethodParameters> getMethodParameters(JpaModelEntity jpaModelEntity, String methodName) {
		List<MethodParameters> list = defaultMethodParameters();

		String para = methodName.replaceFirst(MethodTypeHelper.PAGE, "");
		if (StringUtil.isEmpty(para)) {
			throw new JpaMapperException("findBy条件不完整！");
		}
		String params[] = para.split(CONDITION_AND);
		if (params == null || params.length < 1) {
			throw new JpaMapperException("findBy条件不完整！");
		}

		Map<String, Field> ignoreCaseMap = new HashMap<>();

		Field fields[] = jpaModelEntity.getTargertEntity().getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName().toLowerCase();
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				ignoreCaseMap.put(fieldName, field);
				continue;
			}
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				ignoreCaseMap.put(fieldName, field);
			}
		}

		for (String param : params) {
			Field field = ignoreCaseMap.get(param.toLowerCase());
			String fieldName = field.getName();

			if (field != null) {
				MethodParameters methodParameters = new MethodParameters();
				methodParameters.setProperty(fieldName);
				Column columnAnnotation = field.getAnnotation(Column.class);
				if (columnAnnotation != null && StringUtil.isNotEmpty(columnAnnotation.name())) {
					methodParameters.setColumn(columnAnnotation.name());
				} else {
					methodParameters.setColumn(fieldName);
				}
				methodParameters.setType(field.getType());
				list.add(methodParameters);
			}
		}

		return list;
	}
}
