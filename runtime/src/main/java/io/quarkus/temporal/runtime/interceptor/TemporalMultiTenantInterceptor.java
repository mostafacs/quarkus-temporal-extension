package io.quarkus.temporal.runtime.interceptor;

import io.quarkus.temporal.runtime.interceptor.activity.MultiTenantActivityInboundInterceptor;
import io.temporal.common.interceptors.ActivityInboundCallsInterceptor;
import io.temporal.common.interceptors.WorkerInterceptor;
import io.temporal.common.interceptors.WorkflowInboundCallsInterceptor;


/**
 * @author Mostafa
 */
public class TemporalMultiTenantInterceptor implements WorkerInterceptor {
    @Override
    public WorkflowInboundCallsInterceptor interceptWorkflow(WorkflowInboundCallsInterceptor next) {
        return next;
    }

    @Override
    public ActivityInboundCallsInterceptor interceptActivity(ActivityInboundCallsInterceptor next) {
        return new MultiTenantActivityInboundInterceptor(next);
    }

}
