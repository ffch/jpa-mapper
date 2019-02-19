package cn.pomit.jpamapper.core.sql.dialect.imp;

import java.util.List;

import cn.pomit.jpamapper.core.domain.page.PageConstant;
import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.MethodParameters;
import cn.pomit.jpamapper.core.sql.dialect.Dialect;
import cn.pomit.jpamapper.core.sql.helper.PageAndSortSqlHelper;

public class MysqlDialect implements Dialect{

	@Override
	public String getLimitSql(JpaModelEntity jpaModelEntity) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append("<bind name=\"startNum\" value=\"(");
		sql.append(PageConstant.PAGE);
		sql.append("-1)*");
		sql.append(PageConstant.SIZE);
		sql.append("\"></bind>");
		sql.append(PageAndSortSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.fromSql(jpaModelEntity));
		
		List<MethodParameters> methodParametersList = jpaModelEntity.getMethodParametersList();
		if(methodParametersList.size() > PageConstant.PAGE_START){
			sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");
			for(int i = PageConstant.PAGE_START;i<methodParametersList.size();i++){
				MethodParameters item = methodParametersList.get(i);
				sql.append("AND ");
				sql.append(item.getColumn());
				sql.append(" = #{");
				sql.append(item.getProperty());
				sql.append("} ");
			}
			sql.append("</trim>");
		}
		sql.append(" <if test='");
		sql.append(PageConstant.SORT);
		sql.append(" != null and ");
		sql.append(PageConstant.SORT);
		sql.append(" != \"\"'> order by ${");
		sql.append(PageConstant.SORT);
		sql.append("} </if> ");
		
		sql.append("limit #{");
		sql.append(PageConstant.SIZE);
		sql.append("} OFFSET #{startNum} ");
		sql.append(" </script>");
		return sql.toString();
	}
	
	
}
