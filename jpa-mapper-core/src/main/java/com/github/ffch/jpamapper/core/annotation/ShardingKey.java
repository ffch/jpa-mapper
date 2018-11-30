package com.github.ffch.jpamapper.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author cff
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ShardingKey {
	String prefix() default "";
	String suffix() default "";
	
	//分表方法
	String methodRange() default "";
	String methodPrecis() default "";
}
