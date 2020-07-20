package io.github.manusant.spark.typify.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE_USE, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Xml {
}
