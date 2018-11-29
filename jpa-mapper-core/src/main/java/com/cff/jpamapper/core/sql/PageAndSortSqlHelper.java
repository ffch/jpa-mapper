package com.cff.jpamapper.core.sql;

import java.util.Map;
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

}
