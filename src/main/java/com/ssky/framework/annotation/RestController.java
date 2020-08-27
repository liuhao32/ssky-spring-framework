package com.ssky.framework.annotation;

import java.lang.annotation.*;

/**
 * @author YCKJ3275
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestController {
    String value() default "";
}
