package cn.pomit.jpamapper.core.sql.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pomit.jpamapper.core.domain.page.PageConstant;
import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.MethodParameters;
import cn.pomit.jpamapper.core.helper.DialectTypeHelper;
import cn.pomit.jpamapper.core.sql.dialect.Dialect;

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
		sql.append(", #{page} ");
		sql.append(PageConstant.PAGE);
		sql.append(", #{size} ");
		sql.append(PageConstant.SIZE);
		sql.append(", #{sort} ");
		sql.append(PageConstant.SORT);
        return sql.toString();
	}
	
	public static String pageSql(JpaModelEntity jpaModelEntity){
		List<MethodParameters> methodParametersList = jpaModelEntity.getMethodParametersList();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(1) ");
		sql.append(PageConstant.COUNT);
		
		int index = 1;
		if(methodParametersList.size() > PageConstant.PAGE_START){
			for(int i = PageConstant.PAGE_START;i<methodParametersList.size();i++){
				MethodParameters item = methodParametersList.get(i);
				sql.append(", #{param");
				sql.append(index);
				sql.append("} ");
				sql.append(item.getProperty());
				index++;
			}
		}
		
		for(int i = 0;i<PageConstant.PAGE_START;i++){
			MethodParameters item = methodParametersList.get(i);
			sql.append(", #{param");
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
		if(methodParametersList.size() > PageConstant.PAGE_START){
			sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");
			int index = 1;
			for(int i = PageConstant.PAGE_START;i<methodParametersList.size();i++){
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
	
	public static String conditionSortBySql(JpaModelEntity jpaModelEntity, String params[]) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");
		
		Map<String, String> ignoreCaseMap = new HashMap<>();
		ignoreCaseMap.put(jpaModelEntity.getIdName().toLowerCase(), jpaModelEntity.getIdColumn());
		for (Map.Entry<String, String> entry : jpaModelEntity.getFieldMap().entrySet()) {
			ignoreCaseMap.put(entry.getKey().toLowerCase(), entry.getValue());
		}

		int index = 1;
		for (String param : params) {
			String fieldDeclaredName = ignoreCaseMap.get(param.toLowerCase());
			if (fieldDeclaredName != null) {
				sql.append("AND ");
				sql.append(fieldDeclaredName);
				sql.append(" = #{param");
				sql.append(index);
				sql.append("} ");
			}
			index++;
		}
		sql.append("</trim>");
		
		sql.append(
				"<foreach collection =\"param" + index + ".orders\" item=\"item\" index=\"index\" separator=\",\" open=\" order by \" close=\"\"> ");
		sql.append(" ${item.property} ${item.direction}");
		sql.append(" </foreach> ");
		return sql.toString();
	}
	
	public static String limitForAllSql(JpaModelEntity jpaModelEntity){
		Dialect dialect = DialectTypeHelper.getDialectType(jpaModelEntity.getDatabaseName());	
        return dialect.getLimitSql(jpaModelEntity);
	}
}
