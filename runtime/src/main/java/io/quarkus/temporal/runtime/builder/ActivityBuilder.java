package io.quarkus.temporal.runtime.builder;

import io.quarkus.arc.Unremovable;
import io.quarkus.temporal.runtime.config.WorkflowConfigurations;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @Author Mostafa
 * */

@Singleton
@Unremovable
public class ActivityBuilder {

    @Inject
    WorkflowConfigurations configurations;

   public <T, I> I build(Class<T> workflow, Class<I> activity) {
       if(!workflow.isInterface()) throw new IllegalArgumentException("Please pass Workflow interface (not implemented class)");
       if(!activity.isInterface()) throw new IllegalArgumentException("Please pass Activity interface (not implemented class)");

       ActivityOptions options = buildActivityOptions(workflow, activity);
       return Workflow.newActivityStub(activity, options);
   }

   public ActivityOptions buildActivityOptions(Class workflow, Class activity) {
       return
               ActivityOptions.newBuilder()
                       // disable retries for example to run faster
                       .setScheduleToStartTimeout(configurations.activityScheduleToStartTimeout(workflow, activity))
                       .setScheduleToCloseTimeout(configurations.activityScheduleToCloseTimeout(workflow, activity))
                       .setStartToCloseTimeout(configurations.activityStartToCloseTimeout(workflow, activity))
                       .setHeartbeatTimeout(configurations.activityHeartTimeout(workflow, activity))
                       .setRetryOptions(RetryOptions.newBuilder()
                               .setInitialInterval(configurations.activityRetryInitInterval(workflow, activity))
                               .setMaximumInterval(configurations.activityRetryMaxInterval(workflow, activity))
                               .setBackoffCoefficient(configurations.activityRetryBackOffCoefficient(workflow, activity))
                               .setMaximumAttempts(configurations.activityRetryMaxAttempts(workflow, activity))
                               .build())
                       .build();
   }
}
