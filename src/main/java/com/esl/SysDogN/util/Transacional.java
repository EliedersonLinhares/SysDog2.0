package com.esl.SysDogN.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@Target({ElementType.METHOD, ElementType.TYPE})// em que momento essa anotação ira funcionar
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface Transacional {

}
