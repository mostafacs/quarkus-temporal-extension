package io.quarkus.temporal.runtime.config;

import java.util.Map;

/**
 * @Author Mostafa
 */
public class TemporalConfig {

    private DefaultConfig defaults;

    private Map<String, WorkflowConfig> workflows;

    public DefaultConfig getDefaults() {
        return defaults;
    }

    public void setDefaults(DefaultConfig defaults) {
        this.defaults = defaults;
    }

    public Map<String, WorkflowConfig> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(Map<String, WorkflowConfig> workflows) {
        this.workflows = workflows;
    }
}
