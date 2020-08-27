package com.ssky.framework.annotation;

import java.lang.annotation.*;

/**
 * @author YCKJ3275
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostMapping {
    String value() default "";
}
