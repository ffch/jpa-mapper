package com.cff.jpamapper.core.sql.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cff.jpamapper.core.domain.page.PageConstant;
import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.entity.MethodParameters;
import com.cff.jpamapper.core.helper.DialectTypeHelper;
import com.cff.jpamapper.core.sql.dialect.Dialect;
import com.cff.jpamapper.core.util.StringUtil;

public class PageAndSortSqlHelper extends DefaultSqlHelper {

	public static String sortSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"<foreach collection =\"orders\" item=\"item\" index=\"index\" separator=\",\" open=\" order by \" close=\"\"> ");
		sql.append(" ${item.property} ${item.direction}");
		sql.append(" </foreach> ");
		return sql.toString();
	}

	public static String pageSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(1) ");
		sql.append(PageConstant.COUNT);
		sql.append(", ${page} ");
		sql.append(PageConstant.PAGE);
		sql.append(", ${size} ");
		sql.append(PageConstant.SIZE);
        return sql.toString();
	}
	
	public static String pageSql(JpaModelEntity jpaModelEntity){
		List<MethodParameters> methodParametersList = jpaModelEntity.getMethodParametersList();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(1) ");
		sql.append(PageConstant.COUNT);
		
		int index = 1;
		if(methodParametersList.size() > 2){
			for(int i = 2;i<methodParametersList.size();i++){
				MethodParameters item = methodParametersList.get(i);
				sql.append(", #{param");
				sql.append(index);
				sql.append("} ");
				sql.append(item.getProperty());
				index++;
			}
		}
		
		for(int i = 0;i<2;i++){
			MethodParameters item = methodParametersList.get(i);
			sql.append(", ${param");
			sql.append(index);
			sql.append(".");
			sql.append(item.getProperty());
			sql.append("} ");
			sql.append(item.getProperty());
		}
		
        return sql.toString();
	}
	
	public static String conditionRegBySql(JpaModelEntity jpaModelEntity) {
		List<MethodParameters> methodParametersList = jpaModelEntity.getMethodParametersList();
		
		StringBuilder sql = new StringBuilder();
		if(methodParametersList.size() > 2){
			sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");
			int index = 1;
			for(int i = 2;i<methodParametersList.size();i++){
				MethodParameters item = methodParametersList.get(i);
				sql.append("AND ");
				sql.append(item.getColumn());
				sql.append(" = #{param");
				sql.append(index);
				sql.append("} ");
				index++;
			}
			sql.append("</trim>");
		}
		return sql.toString();
	}
	
	public static String limitForAllSql(JpaModelEntity jpaModelEntity){
		Dialect dialect = DialectTypeHelper.getDialectType(jpaModelEntity.getDatabaseName());	
        return dialect.getLimitSql(jpaModelEntity);
	}
}
