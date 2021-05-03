package io.quarkus.temporal.runtime.config;

import io.quarkus.temporal.runtime.WorkflowRuntimeBuildItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Map;


public class WorkflowConfigurations {

    private static final Long DEFAULT_WF_EXEC_TIMEOUT = 60l;
    private static final Long DEFAULT_WF_RUN_TIMEOUT = 60l;
    private static final Long DEFAULT_WF_TASK_TIMEOUT = 60l;

    private static final Long DEFAULT_ACTIVITY_SCHEDULE_TO_START_TIMEOUT = 60l;
    private static final Long DEFAULT_ACTIVITY_SCHEDULE_TO_CLOSE_TIMEOUT = 60l;
    private static final Long DEFAULT_ACTIVITY_START_TO_CLOSE_TIMEOUT = 60l;
    // heartbeat timeout must be shorter than START_TO_CLOSE timeout
    private static final Long DEFAULT_ACTIVITY_HEARTBEAT_TIMEOUT = 5l; // minutes
    private static final Long DEFAULT_ACTIVITY_RETRY_INIT_INTERVAL = 1l;
    private static final Long DEFAULT_ACTIVITY_RETRY_MAX_INTERVAL = 1l;
    private static final Double DEFAULT_ACTIVITY_RETRY_BACKOFF_COEFFICIENT = 1.0;
    private static final Integer DEFAULT_ACTIVITY_RETRY_MAX_Attempts = 1;

    // private String default
    Logger logger = LoggerFactory.getLogger(this.getClass());

    Map<String, WorkflowConfig> workflows;

    
    public WorkflowConfigurations(Map<String, WorkflowConfig> workflows){
        this.workflows = workflows;
    }

    public Duration workflowExecutionTimeout(Class workflow) {
        String workflowName = WorkflowRuntimeBuildItem.getWorkflowName(workflow);
        WorkflowConfig config = workflows.get(workflowName);
        String minutes = config == null ? null : config.getExecutionTimeout();
        return getWorkflowConfig(minutes, DEFAULT_WF_EXEC_TIMEOUT);
    }

    public Duration workflowRunTimeout(Class workflow) {
        String workflowName = WorkflowRuntimeBuildItem.getWorkflowName(workflow);
        WorkflowConfig config = workflows.get(workflowName);
        String minutes = config == null ? null : config.getRunTimeout();
        return getWorkflowConfig(minutes, DEFAULT_WF_RUN_TIMEOUT);
    }

    public Duration workflowTaskTimeout(Class workflow) {
        String workflowName = WorkflowRuntimeBuildItem.getWorkflowName(workflow);
        WorkflowConfig config = workflows.get(workflowName);
        String minutes = config == null ? null : config.getTaskTimeout();
        return getWorkflowConfig(minutes, DEFAULT_WF_TASK_TIMEOUT);
    }

    private Duration getWorkflowConfig(String minutes, Long defaultValue) {
        return getMinutesDuration(minutes, defaultValue);
    }

    private WorkflowConfig.ActivityConfig getActivityConfig(Class workflow, Class activity) {

        String workflowName = WorkflowRuntimeBuildItem.getWorkflowName(workflow);
        String activityName = WorkflowRuntimeBuildItem.getActivityName(activity);

        WorkflowConfig wfConfig = workflows.get(workflowName);
        if(wfConfig != null) {
            if(wfConfig.getActivities() != null) {
                return wfConfig.getActivities().getOrDefault(activityName, null);
            }
        }
        return null;
    }

    public Duration activityScheduleToStartTimeout(Class workflow, Class activity) {
        WorkflowConfig.ActivityConfig config = getActivityConfig(workflow, activity);
        String minutes = config == null ? null : config.getScheduleTostartTimeout();
        return getActivityConfig(minutes, DEFAULT_ACTIVITY_SCHEDULE_TO_START_TIMEOUT);
    }
    public Duration activityScheduleToCloseTimeout(Class workflow, Class activity) {
        WorkflowConfig.ActivityConfig config = getActivityConfig(workflow, activity);
        String minutes = config == null ? null : config.getScheduleTocloseTimeout();
        return getActivityConfig(minutes, DEFAULT_ACTIVITY_SCHEDULE_TO_CLOSE_TIMEOUT);
    }

    public Duration activityStartToCloseTimeout(Class workflow, Class activity) {
        WorkflowConfig.ActivityConfig config = getActivityConfig(workflow, activity);
        String minutes = config == null ? null : config.getStartTocloseTimeout();
        return getActivityConfig(minutes, DEFAULT_ACTIVITY_START_TO_CLOSE_TIMEOUT);
    }

    public Duration activityHeartTimeout(Class workflow, Class activity) {
        WorkflowConfig.ActivityConfig config = getActivityConfig(workflow, activity);
        String minutes = config == null ? null : config.getHeartbeatTimeout();
        return getActivityConfig(minutes, DEFAULT_ACTIVITY_HEARTBEAT_TIMEOUT);
    }
    public Duration activityRetryInitInterval(Class workflow, Class activity) {
        WorkflowConfig.ActivityConfig config = getActivityConfig(workflow, activity);
        String minutes = config == null ? null : config.getRetryInitInterval();
        return getActivityConfig(minutes , DEFAULT_ACTIVITY_RETRY_INIT_INTERVAL);
    }
    public Duration activityRetryMaxInterval(Class workflow, Class activity) {
        WorkflowConfig.ActivityConfig config = getActivityConfig(workflow, activity);
        String minutes = config == null ? null : config.getRetryMaxInterval();
        return getActivityConfig(minutes, DEFAULT_ACTIVITY_RETRY_MAX_INTERVAL);
    }
    public double activityRetryBackOffCoefficient(Class workflow, Class activity) {
        WorkflowConfig.ActivityConfig config = getActivityConfig(workflow, activity);
        String value = config == null ? null : config.getRetryBackoffCoefficient();
        return getDoubleValue(value, DEFAULT_ACTIVITY_RETRY_BACKOFF_COEFFICIENT);
    }
    public Integer activityRetryMaxAttempts(Class workflow, Class activity) {
        WorkflowConfig.ActivityConfig config = getActivityConfig(workflow, activity);
        String value = config == null ? null : config.getRetryMaxAttempts();
        return getIntValue(value, DEFAULT_ACTIVITY_RETRY_MAX_Attempts);
    }

    private Duration getActivityConfig(String minutes, Long defaultValue) {
        return getMinutesDuration(minutes, defaultValue);
    }

    private Duration getMinutesDuration(String minutes, Long defaultValue) {
        if(minutes != null) {
            Double minutesNum = Double.parseDouble(minutes);
            double fraction = minutesNum - minutesNum.longValue();
            if(fraction > 0) {
                Double secondValue = minutesNum * 60;
                return Duration.ofSeconds(secondValue.longValue());
            }
            return Duration.ofMinutes(minutesNum.longValue());
        }
        return Duration.ofMinutes(defaultValue);
    }

    private Duration getSecondsDuration(String seconds, Integer defaultValue) {
        if(seconds != null) {
            return Duration.ofMinutes(Integer.parseInt(seconds));
        }
        return Duration.ofMinutes(defaultValue);
    }

    private double getDoubleValue(String doubleString, Double defaultValue) {
        if(doubleString != null) {
            return Double.parseDouble(doubleString);
        }
        return defaultValue;
    }
    private int getIntValue(String intString, Integer defaultValue) {
        if(intString != null) {
            return Integer.parseInt(intString);
        }
        return defaultValue;
    }
}
