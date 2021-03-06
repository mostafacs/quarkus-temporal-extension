package io.quarkus.temporal.runtime;

import io.quarkus.arc.Arc;
import io.quarkus.temporal.runtime.annotations.EnableRequestScope;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @Author Mostafa
 * TODO still not working -> require fixing
 */

@EnableRequestScope
@Priority(2020)
@Interceptor
public class TemporalRequestScopeInterceptor {

    @AroundInvoke
    Object handleRequestScope(InvocationContext context) throws Exception {
        try {
            Arc.container().requestContext().activate();
            return context.proceed();
        } finally {
            Arc.container().requestContext().deactivate();
        }
    }
}
