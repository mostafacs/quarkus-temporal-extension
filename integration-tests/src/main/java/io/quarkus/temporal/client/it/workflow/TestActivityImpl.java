package io.quarkus.temporal.client.it.workflow;

import io.quarkus.temporal.runtime.annotations.TemporalActivity;

@TemporalActivity(queue = "testQueue")
public class TestActivityImpl implements TestActivity {

    @Override
    public String hello() {
        return "I'm hello activity";
    }
}
