package io.quarkus.temporal.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WorkflowRuntimeBuildItem {

    private Map<String, List<String>> activities = new HashMap<>();

    private Map<String, List<String>> workflows = new HashMap<>();

    Map<Class, String> workflowToQueue = new HashMap<>();

    private List<String> activitiesFlat = new ArrayList<>();


    public WorkflowRuntimeBuildItem(){

    }

    public void addActivity(String queue, String clazz) {
        if(activities.containsKey(queue)) {
            activities.get(queue).add(clazz);
        } else {
            ArrayList a = new ArrayList();
            a.add(clazz);
            activities.put(queue, a);
        }
        activitiesFlat.add(clazz);
    }

    public void addWorkflow(String queue, String clazz) {
        if(workflows.containsKey(queue)) {
            workflows.get(queue).add(clazz);
        } else {
            ArrayList a = new ArrayList();
            a.add(clazz);
            workflows.put(queue, a);
        }
    }


    public void putWorkflow(Class workflowInterface, String queue) {
        workflowToQueue.put(workflowInterface, queue);
    }

    public String getQueue(Class workflowInterface) {
        return workflowToQueue.get(workflowInterface);
    }

    public Map<String, List<String>> getActivities() {
        return activities;
    }

    public void setActivities(Map<String, List<String>> activities) {
        this.activities = activities;
    }

    public Map<String, List<String>> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(Map<String, List<String>> workflows) {
        this.workflows = workflows;
    }

    public List<String> getActivitiesFlat() {
        return activitiesFlat;
    }
}
