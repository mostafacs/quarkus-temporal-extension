package io.quarkus.temporal.runtime;

import io.quarkus.arc.Arc;
import io.quarkus.runtime.Startup;
import io.quarkus.temporal.runtime.annotations.CompletionClient;
import io.quarkus.temporal.runtime.annotations.TemporalActivity;
import io.quarkus.temporal.runtime.annotations.TemporalActivityStub;
import io.quarkus.temporal.runtime.annotations.TemporalWorkflow;
import io.quarkus.temporal.runtime.builder.ActivityBuilder;
import io.temporal.activity.ActivityInterface;
import io.temporal.client.ActivityCompletionClient;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerOptions;
import io.temporal.workflow.Functions;
import io.temporal.workflow.WorkflowInterface;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Mostafa
 * Define necessory Arc Beans
 */
public class TemporalBeansProducer {

    @Produces
    @ApplicationScoped
    public WorkflowClient workflowClient(TemporalServerBuildTimeConfig temporalServerBuildTimeConfig) {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalServerBuildTimeConfig.serviceUrl)
                .setEnableHttps(Boolean.parseBoolean(temporalServerBuildTimeConfig.serviceSecure))
                .build();

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(options);
        return WorkflowClient.newInstance(service);
    }

    /**
     * Injects a completion client instance 
     * into an activity implementation instance
     * 
     * @param activityImpl activity implementation
     * @param completionClient completion client
     * @return activity implementation instance
     * @throws Exception
     */
    private Object injectCompletionClient(Object activityImpl, ActivityCompletionClient completionClient) throws Exception {
        Field fields[] = activityImpl.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(CompletionClient.class)) {
                field.setAccessible(true);
                field.set(activityImpl, completionClient);
                break;
            }
        }
        return activityImpl;
    }

    @Produces
    @ApplicationScoped
    @Startup
    public WorkerFactory workflowClient(WorkflowClient workflowClient,
                                        ActivityBuilder activityBuilder,
                                        WorkflowRuntimeBuildItem workflowRuntimeBuildItem) throws Exception {

        ActivityCompletionClient completionClient = workflowClient.newActivityCompletionClient();
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (Map.Entry<String, Set<String>> entry : workflowRuntimeBuildItem.getActivities().entrySet()) {
            String queue = entry.getKey();
            Set<String> classNames = entry.getValue();
            Object[] activities = new Object[classNames.size()];
            int c = 0;
            for (String clazzName : classNames) {
                Class clazz = classLoader.loadClass(clazzName);
                activities[c] = Arc.container().select(clazz).get();
                //inject our completionClient
                activities[c] = injectCompletionClient(activities[c], completionClient);
                for (Class interfacei : activities[c].getClass().getInterfaces()) {
                    if (interfacei.isAnnotationPresent(ActivityInterface.class)) {
                        TemporalActivity ta = activities[c].getClass().getAnnotation(TemporalActivity.class);
                        workflowRuntimeBuildItem.putActivityInterfaceInfo(interfacei, queue, ta.name());
                        break;
                    }
                }
                c++;
            }

            Worker worker = factory.newWorker(queue, WorkerOptions.newBuilder()
            .setMaxConcurrentActivityExecutionSize(0)
            .build());
            worker.registerActivitiesImplementations(activities);
        }

        for (Map.Entry<String, List<String>> entry : workflowRuntimeBuildItem.getWorkflows().entrySet()) {
            String queue = entry.getKey();
            List<String> classNames = entry.getValue();

            Worker worker = null;
            try {
                worker = factory.getWorker(queue);
            } catch (IllegalArgumentException e) {
                worker = factory.newWorker(queue);
            }

            for (String clazzName : classNames) {
                Class clazz = classLoader.loadClass(clazzName);
                Class[] interfaces = clazz.getInterfaces();
                Class workflowInterface = null;
                for (Class interfacei : interfaces) {
                    if (interfacei.isAnnotationPresent(WorkflowInterface.class)) {
                        TemporalWorkflow ta = (TemporalWorkflow) clazz.getAnnotation(TemporalWorkflow.class);
                        workflowRuntimeBuildItem.putWorkflowInterfaceInfo(interfacei, queue, ta.name());
                        workflowInterface = interfacei;
                        break;
                    }
                }
                if (workflowInterface == null)
                    throw new IllegalArgumentException(clazzName + " not implement interface with @WorkflowInterface");

                worker.addWorkflowImplementationFactory(workflowInterface, new SimpleWorkflowFactory(
                        clazz, workflowInterface, activityBuilder
                ));
            }

        }
        factory.start();
        workflowRuntimeBuildItem.clear();
        return factory;
    }


    private static class SimpleWorkflowFactory implements Functions.Func {

        Class workflowImplClass;
        Class workflowInterface;
        ActivityBuilder activityBuilder;

        public SimpleWorkflowFactory(Class workflowImplClass, Class workflowInterface, ActivityBuilder activityBuilder) {
            this.workflowImplClass = workflowImplClass;
            this.workflowInterface = workflowInterface;
            this.activityBuilder = activityBuilder;
        }


        @Override
        public synchronized Object apply() {

            try {
                Object workflow = workflowImplClass.getConstructor().newInstance();
                fillActivitiesStubs(workflow);
                return workflow;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        private void fillActivitiesStubs(Object workflow) throws Exception {
            Field fields[] = workflowImplClass.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(TemporalActivityStub.class)) {
                    field.setAccessible(true);
                    Object activity = activityBuilder.build(workflowInterface, field.getType());
                    field.set(workflow, activity);
                }
            }

        }


    }

}
