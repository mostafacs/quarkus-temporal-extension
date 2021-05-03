package io.quarkus.temporal.runtime.annotations;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableRequestScope {
}
