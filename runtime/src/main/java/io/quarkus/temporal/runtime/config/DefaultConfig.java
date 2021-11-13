package io.quarkus.temporal.runtime.config;

/**
 * @Author Mostafa
 * */
public class DefaultConfig {

    private Long workflowExecutionTimeout;
    private Long workflowRunTimeout;
    private Long workflowTaskTimeout;

    private Long activityScheduleToStartTimeout;
    private Long activityScheduleToCloseTimeout;
    private Long activityStartToCloseTimeout;
    // heartbeat timeout must be shorter than START_TO_CLOSE timeout
    private Long activityHeartBeatTimeout;
    private Long activityRetryInitInterval;
    private Long activityRetryMaxInterval;
    private Double activityRetryBackOffCoefficient;
    private Integer activityRetryMaxAttempts;


    public Long getWorkflowExecutionTimeout() {
        return workflowExecutionTimeout;
    }

    public void setWorkflowExecutionTimeout(Long workflowExecutionTimeout) {
        this.workflowExecutionTimeout = workflowExecutionTimeout;
    }

    public Long getWorkflowRunTimeout() {
        return workflowRunTimeout;
    }

    public void setWorkflowRunTimeout(Long workflowRunTimeout) {
        this.workflowRunTimeout = workflowRunTimeout;
    }

    public Long getWorkflowTaskTimeout() {
        return workflowTaskTimeout;
    }

    public void setWorkflowTaskTimeout(Long workflowTaskTimeout) {
        this.workflowTaskTimeout = workflowTaskTimeout;
    }

    public Long getActivityScheduleToStartTimeout() {
        return activityScheduleToStartTimeout;
    }

    public void setActivityScheduleToStartTimeout(Long activityScheduleToStartTimeout) {
        this.activityScheduleToStartTimeout = activityScheduleToStartTimeout;
    }

    public Long getActivityScheduleToCloseTimeout() {
        return activityScheduleToCloseTimeout;
    }

    public void setActivityScheduleToCloseTimeout(Long activityScheduleToCloseTimeout) {
        this.activityScheduleToCloseTimeout = activityScheduleToCloseTimeout;
    }

    public Long getActivityStartToCloseTimeout() {
        return activityStartToCloseTimeout;
    }

    public void setActivityStartToCloseTimeout(Long activityStartToCloseTimeout) {
        this.activityStartToCloseTimeout = activityStartToCloseTimeout;
    }

    public Long getActivityHeartBeatTimeout() {
        return activityHeartBeatTimeout;
    }

    public void setActivityHeartBeatTimeout(Long activityHeartBeatTimeout) {
        this.activityHeartBeatTimeout = activityHeartBeatTimeout;
    }

    public Long getActivityRetryInitInterval() {
        return activityRetryInitInterval;
    }

    public void setActivityRetryInitInterval(Long activityRetryInitInterval) {
        this.activityRetryInitInterval = activityRetryInitInterval;
    }

    public Long getActivityRetryMaxInterval() {
        return activityRetryMaxInterval;
    }

    public void setActivityRetryMaxInterval(Long activityRetryMaxInterval) {
        this.activityRetryMaxInterval = activityRetryMaxInterval;
    }

    public Double getActivityRetryBackOffCoefficient() {
        return activityRetryBackOffCoefficient;
    }

    public void setActivityRetryBackOffCoefficient(Double activityRetryBackOffCoefficient) {
        this.activityRetryBackOffCoefficient = activityRetryBackOffCoefficient;
    }

    public Integer getActivityRetryMaxAttempts() {
        return activityRetryMaxAttempts;
    }

    public void setActivityRetryMaxAttempts(Integer activityRetryMaxAttempts) {
        this.activityRetryMaxAttempts = activityRetryMaxAttempts;
    }
}
