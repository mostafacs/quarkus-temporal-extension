package io.quarkus.temporal.runtime.annotations;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author Mostafa
 * still not working
 * TODO require fixing
 * */
//@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableRequestScope {
}
