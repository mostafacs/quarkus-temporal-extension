package io.quarkus.temporal.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

/**
 * @Author Mostafa
 */
@ConfigRoot(name = "temporal", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class TemporalServerBuildTimeConfig {

    /**
     * Temporal service url
     */
    @ConfigItem(name = "service.url")
    public String serviceUrl;

    /**
     * enable multi-tenancy to init request scopes
     */
    @ConfigItem(name = "multi-tenant.enabled")
    public boolean multiTenantEnabled;
}
