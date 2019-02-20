package cn.pomit.jpamapper.core.sql.type.select;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.MethodParameters;
import cn.pomit.jpamapper.core.exception.JpaMapperException;
import cn.pomit.jpamapper.core.helper.MethodTypeHelper;
import cn.pomit.jpamapper.core.sql.helper.PageAndSortSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractPageSortSqlType;
import cn.pomit.jpamapper.core.util.StringUtil;

public class SortBySqlType extends AbstractPageSortSqlType {

	public static final SortBySqlType INSTANCE = new SortBySqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(PageAndSortSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.fromSql(jpaModelEntity));

		String name = method.getName();
		String para = name.replaceFirst(MethodTypeHelper.SORT, "");
		if (StringUtil.isEmpty(para)) {
			throw new JpaMapperException("sortBy条件不完整！");
		}
		String params[] = para.split(CONDITION_AND);
		if (params == null || params.length < 1) {
			throw new JpaMapperException("sortBy条件不完整！");
		}
		sql.append(PageAndSortSqlHelper.conditionSortBySql(jpaModelEntity, params));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Override
	public List<MethodParameters> getMethodParameters(JpaModelEntity jpaModelEntity, String methodName) {
		return null;
	}
}
