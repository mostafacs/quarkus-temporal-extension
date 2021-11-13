package io.quarkus.temporal.runtime.config;

import java.util.Map;

/**
 * @Author Mostafa
 * */
public class WorkflowConfig {

    String executionTimeout;
    String runTimeout;
    String taskTimeout;

    Map<String, ActivityConfig> activities;

    public String getExecutionTimeout() {
        return executionTimeout;
    }

    public void setExecutionTimeout(String executionTimeout) {
        this.executionTimeout = executionTimeout;
    }

    public String getRunTimeout() {
        return runTimeout;
    }

    public void setRunTimeout(String runTimeout) {
        this.runTimeout = runTimeout;
    }

    public String getTaskTimeout() {
        return taskTimeout;
    }

    public void setTaskTimeout(String taskTimeout) {
        this.taskTimeout = taskTimeout;
    }

    public Map<String, ActivityConfig> getActivities() {
        return activities;
    }

    public void setActivities(Map<String, ActivityConfig> activities) {
        this.activities = activities;
    }

    public static class ActivityConfig {

        private String scheduleTostartTimeout;
        private String scheduleTocloseTimeout;
        private String startTocloseTimeout;
        private String heartbeatTimeout;
        private String retryInitInterval;
        private String retryMaxInterval;
        private String retryBackoffCoefficient;
        private String retryMaxAttempts;

        public String getScheduleTostartTimeout() {
            return scheduleTostartTimeout;
        }

        public void setScheduleTostartTimeout(String scheduleTostartTimeout) {
            this.scheduleTostartTimeout = scheduleTostartTimeout;
        }

        public String getScheduleTocloseTimeout() {
            return scheduleTocloseTimeout;
        }

        public void setScheduleTocloseTimeout(String scheduleTocloseTimeout) {
            this.scheduleTocloseTimeout = scheduleTocloseTimeout;
        }

        public String getStartTocloseTimeout() {
            return startTocloseTimeout;
        }

        public void setStartTocloseTimeout(String startTocloseTimeout) {
            this.startTocloseTimeout = startTocloseTimeout;
        }

        public String getHeartbeatTimeout() {
            return heartbeatTimeout;
        }

        public void setHeartbeatTimeout(String heartbeatTimeout) {
            this.heartbeatTimeout = heartbeatTimeout;
        }

        public String getRetryInitInterval() {
            return retryInitInterval;
        }

        public void setRetryInitInterval(String retryInitInterval) {
            this.retryInitInterval = retryInitInterval;
        }

        public String getRetryMaxInterval() {
            return retryMaxInterval;
        }

        public void setRetryMaxInterval(String retryMaxInterval) {
            this.retryMaxInterval = retryMaxInterval;
        }

        public String getRetryBackoffCoefficient() {
            return retryBackoffCoefficient;
        }

        public void setRetryBackoffCoefficient(String retryBackoffCoefficient) {
            this.retryBackoffCoefficient = retryBackoffCoefficient;
        }

        public String getRetryMaxAttempts() {
            return retryMaxAttempts;
        }

        public void setRetryMaxAttempts(String retryMaxAttempts) {
            this.retryMaxAttempts = retryMaxAttempts;
        }
    }
}
