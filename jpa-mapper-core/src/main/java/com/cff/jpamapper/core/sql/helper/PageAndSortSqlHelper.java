package com.cff.jpamapper.core.sql.helper;

import java.util.Map;

import com.cff.jpamapper.core.domain.page.PageConstant;
import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.entity.ShardingEntity;
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
	
	public static String limitSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("limit #{");
		sql.append(PageConstant.SIZE);
		sql.append("} OFFSET #{ ");
		sql.append(PageConstant.PAGE);
		sql.append("} ");
        return sql.toString();
	}
}
