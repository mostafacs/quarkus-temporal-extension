package io.quarkus.temporal.runtime.interceptor.activity;

import io.quarkus.arc.Arc;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.common.interceptors.ActivityInboundCallsInterceptor;
import io.temporal.common.interceptors.ActivityInboundCallsInterceptorBase;


/**
 * @author Mostafa
 */
public class MultiTenantActivityInboundInterceptor extends ActivityInboundCallsInterceptorBase {

    private ActivityExecutionContext activityExecutionContext;

    public MultiTenantActivityInboundInterceptor(ActivityInboundCallsInterceptor next) {
        super(next);
    }

    @Override
    public void init(ActivityExecutionContext context) {
        this.activityExecutionContext = context;
        super.init(context);
    }

    @Override
    public ActivityOutput execute(ActivityInput input) {
            try {
                Arc.container().requestContext().activate();
                return super.execute(input);
            } finally {
                 Arc.container().requestContext().deactivate();
            }
    }
}
