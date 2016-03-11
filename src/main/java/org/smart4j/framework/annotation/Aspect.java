package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by hjy on 15-12-31.
 * 切面注解
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
    * 注解
    */
    Class<? extends Annotation> value();
}
