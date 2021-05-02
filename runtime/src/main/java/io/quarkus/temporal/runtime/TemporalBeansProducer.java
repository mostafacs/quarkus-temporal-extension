package io.quarkus.temporal.runtime;

import io.quarkus.arc.Arc;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemporalBeansProducer {

    @Produces
    @ApplicationScoped
    public WorkflowClient workflowClient(TemporalServerBuildTimeConfig temporalServerBuildTimeConfig) {
        WorkflowServiceStubsOptions options = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalServerBuildTimeConfig.serviceUrl)
                .build();

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance(options);
        return WorkflowClient.newInstance(service);
    }


    @Produces
    @ApplicationScoped
    public WorkerFactory workflowClient(WorkflowClient workflowClient,
                                        WorkflowRuntimeBuildItem workflowRuntimeBuildItem) throws Exception{

        WorkerFactory factory =  WorkerFactory.newInstance(workflowClient);
        for (Map.Entry<String, List<String>> entry : workflowRuntimeBuildItem.getActivities().entrySet()) {
            String queue = entry.getKey();
            List<String> classNames = entry.getValue();
            Object[] activities = new Object[classNames.size()];
            int c=0;
            for(String clazzName : classNames) {
                Class clazz = Thread.currentThread().getContextClassLoader().loadClass(clazzName);
                activities[c]=Arc.container().select(clazz).get();
                c++;
            }

            Worker worker = factory.newWorker(queue);
            worker.registerActivitiesImplementations(activities);
        }
        return factory;
    }



}
