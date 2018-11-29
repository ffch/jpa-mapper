package com.cff.jpamapper.core.sql.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.domain.page.PageConstant;
import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.entity.MethodParameters;

public abstract class AbstractPageSortSqlType implements SqlType {
	
	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}

	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}
	
	public abstract List<MethodParameters> getMethodParameters(JpaModelEntity jpaModelEntity, String methodName);
	
	public List<MethodParameters> defaultMethodParameters(){
		List<MethodParameters> list = new ArrayList<MethodParameters>();
		MethodParameters pageMethodParameters = new MethodParameters();
		pageMethodParameters.setColumn(PageConstant.PAGE);
		pageMethodParameters.setProperty(PageConstant.PAGE);
		pageMethodParameters.setType(int.class);
		list.add(pageMethodParameters);
		
		MethodParameters sizeMethodParameters = new MethodParameters();
		sizeMethodParameters.setColumn(PageConstant.SIZE);
		sizeMethodParameters.setProperty(PageConstant.SIZE);
		sizeMethodParameters.setType(int.class);
		list.add(sizeMethodParameters);
		
		return list;
	}
}
