package cn.pomit.jpamapper.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.mapping.FetchType;


/**
 * 一对多查询注解，配合@JoinColumn或@JoinColumns使用，JoinColumn(s)
 * 的name和referencedColumnName属性分别对应两个实体中的属性名称，不是表字段。
 * 
 * @author fufei
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Many {	
	FetchType fetchType() default FetchType.DEFAULT;
}
