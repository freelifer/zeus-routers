package routers.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author zhukun on 2017/5/26.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Notify {
    String value() default "";

    Class<?> clazz() default Void.class;

    int requestCode() default -1;
}
