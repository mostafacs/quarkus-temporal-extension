package io.quarkus.temporal.client.it;

import io.quarkus.temporal.runtime.annotations.TemporalWorkflow;

@TemporalWorkflow(queue = "testQueue")
public class TestWorkflow {
}
