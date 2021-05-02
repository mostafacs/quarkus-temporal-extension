package io.quarkus.temporal.client.it.workflow;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface TestActivity {

    String hello();
}
