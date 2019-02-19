package cn.pomit.jpamapper.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.mapping.StatementType;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface SelectKey {
	String[] statement();

	String keyProperty();

	String keyColumn() default "";

	boolean before();

	Class<?> resultType();

	StatementType statementType() default StatementType.PREPARED;
}
