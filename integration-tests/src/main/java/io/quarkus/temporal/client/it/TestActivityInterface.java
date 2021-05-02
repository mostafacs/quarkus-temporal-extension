package io.quarkus.temporal.client.it;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface TestActivityInterface {

    String hello();
}
