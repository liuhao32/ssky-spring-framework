package com.ssky.framework.annotation;

import java.lang.annotation.*;

/**
 * @author YCKJ3275
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
