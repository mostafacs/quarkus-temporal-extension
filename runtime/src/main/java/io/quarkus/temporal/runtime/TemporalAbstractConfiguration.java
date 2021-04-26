package io.quarkus.temporal.runtime;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class TemporalAbstractConfiguration {

    @Produces
    @ApplicationScoped
    public WorkflowClient workflowClient(TemporalServerBuildTimeConfig temporalServerBuildTimeConfig) {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalServerBuildTimeConfig.serviceUrl)
                .build();

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(options);
        return WorkflowClient.newInstance(service);
    }
}
