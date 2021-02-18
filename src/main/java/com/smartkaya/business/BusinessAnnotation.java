package com.smartkaya.business;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**定义限制注解*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) 
public @interface BusinessAnnotation {
	String objectId() default "";
	double maxMoney() default 10000;
}
