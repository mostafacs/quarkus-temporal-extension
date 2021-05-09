package io.quarkus.temporal.client.it.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TestWorkflow2 {

    @WorkflowMethod
    void test();
}
