package cn.pomit.jpamapper.core.sql.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.domain.page.PageConstant;
import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.MethodParameters;

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
		
		MethodParameters sortMethodParameters = new MethodParameters();
		sortMethodParameters.setColumn(PageConstant.SORT);
		sortMethodParameters.setProperty(PageConstant.SORT);
		sortMethodParameters.setType(String.class);
		list.add(sortMethodParameters);
		
		return list;
	}
}
