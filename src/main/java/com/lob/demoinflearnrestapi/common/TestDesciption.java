package com.lob.demoinflearnrestapi.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 타입이 클래스이면 에러 발생
@Retention(RetentionPolicy.SOURCE)
public @interface TestDesciption {

    String value();

}
