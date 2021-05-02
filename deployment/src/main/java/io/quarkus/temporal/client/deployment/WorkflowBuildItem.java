package io.quarkus.temporal.client.deployment;

import io.quarkus.builder.item.SimpleBuildItem;
import io.quarkus.temporal.runtime.WorkflowRuntimeBuildItem;

public final class WorkflowBuildItem extends SimpleBuildItem {

    private WorkflowRuntimeBuildItem workflowRuntimeBuildItems;


    public WorkflowBuildItem(WorkflowRuntimeBuildItem workflowRuntimeBuildItems) {
        this.workflowRuntimeBuildItems = workflowRuntimeBuildItems;
    }

    public WorkflowBuildItem() {
    }

    public WorkflowRuntimeBuildItem getWorkflowRuntimeBuildItems() {
        return workflowRuntimeBuildItems;
    }

    public void setWorkflowRuntimeBuildItems(WorkflowRuntimeBuildItem workflowRuntimeBuildItems) {
        this.workflowRuntimeBuildItems = workflowRuntimeBuildItems;
    }


}
