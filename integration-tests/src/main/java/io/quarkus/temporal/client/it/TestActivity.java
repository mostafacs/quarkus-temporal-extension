package io.quarkus.temporal.client.it;

import io.quarkus.temporal.runtime.annotations.TemporalActivity;

@TemporalActivity(queue = "testQueue")
public class TestActivity implements TestActivityInterface {

    @Override
    public String hello() {
        return "hello";
    }
}
