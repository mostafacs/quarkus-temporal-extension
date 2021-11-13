package io.quarkus.temporal.runtime.builder;

import io.quarkus.arc.Unremovable;
import io.quarkus.temporal.runtime.WorkflowRuntimeBuildItem;
import io.quarkus.temporal.runtime.config.WorkflowConfigurations;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * @Author Mostafa
 */

@Singleton
@Unremovable
public class WorkflowBuilder {

    @Inject
    WorkflowConfigurations configurations;

    @Inject
    WorkflowClient workflowClient;

    @Inject
    WorkflowRuntimeBuildItem workflowRuntimeBuildItem;

    public <T> T build(Class<T> workflowClass, String workflowId) {
        if (!workflowClass.isInterface())
            throw new IllegalArgumentException("Please pass workflow interface (not implemented class)");
        WorkflowOptions workflowOptions = buildDefaultWorkflowOptions(workflowClass, workflowId);
        return workflowClient.newWorkflowStub(workflowClass, workflowOptions);
    }

    public WorkflowOptions buildDefaultWorkflowOptions(Class workflow, String workflowId) {
        return WorkflowOptions.newBuilder()
                .setWorkflowId(workflowId)
                .setTaskQueue(workflowRuntimeBuildItem.getWorkflowQueue(workflow))
                .setWorkflowExecutionTimeout(configurations.workflowExecutionTimeout(workflow))
                .setWorkflowRunTimeout(configurations.workflowRunTimeout(workflow))
                .setWorkflowTaskTimeout(configurations.workflowTaskTimeout(workflow))
                .build();
    }
}
