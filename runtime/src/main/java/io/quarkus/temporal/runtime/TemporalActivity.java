package io.quarkus.temporal.runtime;

import io.temporal.activity.ActivityInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ActivityInterface
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TemporalActivity {

    String name();

    Class workflow();
}
