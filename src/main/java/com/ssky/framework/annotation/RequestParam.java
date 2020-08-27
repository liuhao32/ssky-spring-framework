package com.ssky.framework.annotation;

import java.lang.annotation.*;

/**
 * @author YCKJ3275
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
