package routers.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author zhukun on 2017/5/26.
 */

@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Input {
    String value() default "";
}
