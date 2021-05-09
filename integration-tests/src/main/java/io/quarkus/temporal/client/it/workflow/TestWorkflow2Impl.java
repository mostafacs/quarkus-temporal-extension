package io.quarkus.temporal.client.it.workflow;

import io.quarkus.temporal.runtime.annotations.TemporalActivityStub;
import io.quarkus.temporal.runtime.annotations.TemporalWorkflow;
import io.temporal.workflow.Workflow;

@TemporalWorkflow(queue = "queue2")
public class TestWorkflow2Impl implements TestWorkflow2 {

    @TemporalActivityStub
    TestActivity testActivity;


    @Override
    public void test() {
        System.out.println(testActivity.hello());
        Workflow.sleep(5000);
        System.out.println("TestWorkflow <<2>> is completed");
    }
}
