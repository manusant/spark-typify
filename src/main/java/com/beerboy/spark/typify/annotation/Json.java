package com.beerboy.spark.typify.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE,ElementType.TYPE_PARAMETER, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Json {
}
