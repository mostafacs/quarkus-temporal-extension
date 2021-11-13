package io.quarkus.temporal.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Mostafa
 */
public final class WorkflowRuntimeBuildItem {

    private final class NameQueueInfo {

        public NameQueueInfo() {
        }

        public NameQueueInfo(String name, String queue) {
            this.name = name;
            this.queue = queue;
        }

        public String name;
        public String queue;
    }

    private Map<String, Set<String>> activities = new HashMap<>();

    private Map<String, List<String>> workflows = new HashMap<>();

    static Map<Class, NameQueueInfo> workflowToQueue = new HashMap<>();
    static Map<Class, NameQueueInfo> activityToQueue = new HashMap<>();


    private List<String> activitiesFlat = new ArrayList<>();


    public WorkflowRuntimeBuildItem() {

    }

    public void addActivityImpl(String clazz) {
        activitiesFlat.add(clazz);
    }

    public void addActivityInterface(String queue, String clazz) {
        if (activities.containsKey(queue)) {
            activities.get(queue).add(clazz);
        } else {
            Set a = new HashSet();
            a.add(clazz);
            activities.put(queue, a);
        }
    }

    public void addWorkflowImpl(String queue, String clazz) {
        if (workflows.containsKey(queue)) {
            workflows.get(queue).add(clazz);
        } else {
            ArrayList a = new ArrayList();
            a.add(clazz);
            workflows.put(queue, a);
        }
    }

    public void clear() {
        this.workflows.clear();
        this.activities.clear();
    }

    public void putWorkflowInterfaceInfo(Class workflowInterface, String queue, String name) {
        if (name == null || name.length() == 0) {
            name = workflowInterface.getSimpleName();
        }
        workflowToQueue.put(workflowInterface, new NameQueueInfo(name, queue));
    }

    public void putActivityInterfaceInfo(Class activityInterface, String queue, String name) {
        if (name == null || name.length() == 0) {
            name = activityInterface.getSimpleName();
        }
        activityToQueue.put(activityInterface, new NameQueueInfo(name, queue));
    }

    public static String getWorkflowName(Class workflowInterface) {
        return workflowToQueue.get(workflowInterface).name;
    }

    public static String getWorkflowQueue(Class workflowInterface) {
        return workflowToQueue.get(workflowInterface).queue;
    }

    public static String getActivityName(Class activityInterface) {
        return activityToQueue.get(activityInterface).name;
    }

    public static String getActivityQueue(Class activityInterface) {
        return activityToQueue.get(activityInterface).queue;
    }

    public Map<String, Set<String>> getActivities() {
        return activities;
    }

    public void setActivities(Map<String, Set<String>> activities) {
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
