package com.skyguard.teraka.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableTerakaClient {

    String name() default "";

    int type() default 1;

    String topic() default "";

    int weight() default 0;



}
