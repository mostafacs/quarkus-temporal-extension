package io.quarkus.temporal.client.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.AdditionalApplicationArchiveMarkerBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageProxyDefinitionBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.temporal.runtime.GenericSupplier;
import io.quarkus.temporal.runtime.TemporalBeansProducer;
import io.quarkus.temporal.runtime.TemporalRecorder;
import io.quarkus.temporal.runtime.WorkflowRuntimeBuildItem;
import io.quarkus.temporal.runtime.annotations.TemporalActivity;
import io.quarkus.temporal.runtime.annotations.TemporalActivityStub;
import io.quarkus.temporal.runtime.annotations.TemporalWorkflow;
import io.quarkus.temporal.runtime.builder.ActivityBuilder;
import io.quarkus.temporal.runtime.builder.WorkflowBuilder;
import io.quarkus.temporal.runtime.config.WorkflowConfigurations;
import io.temporal.activity.ActivityInterface;
import io.temporal.workflow.WorkflowInterface;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mostafa Albana, netodevel
 */
class TemporalClientProcessor {

    private static final String FEATURE = "temporal";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalApplicationArchiveMarkerBuildItem required() {
        return new AdditionalApplicationArchiveMarkerBuildItem("workflow.yml");
    }

    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    void buildConfigs(TemporalRecorder recorder,
                      BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer) throws Exception {

        SyntheticBeanBuildItem workflowConfigBuildItem = SyntheticBeanBuildItem.configure(WorkflowConfigurations.class)
                .scope(ApplicationScoped.class)
                .runtimeValue(recorder.createWorkflowConfigs())
                .done();
        syntheticBeanBuildItemBuildProducer.produce(workflowConfigBuildItem);
    }

    @BuildStep
    WorkflowBuildItem workFlowBuildItem(
            BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer,
            CombinedIndexBuildItem combinedIndex) {

        WorkflowRuntimeBuildItem wrbi = new WorkflowRuntimeBuildItem();

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(TemporalActivity.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            wrbi.addActivityImpl(activityClassName);
        }

        Map<String, String> workfowImplToQueue = new HashMap<>();
        for (AnnotationInstance ai : combinedIndex.getIndex()
                .getAnnotations(DotName.createSimple(TemporalWorkflow.class.getName()))) {

            AnnotationValue queueValue = ai.value("queue");
            String wfClassName = ai.target().asClass().name().toString();
            wrbi.addWorkflowImpl(queueValue.value().toString(), wfClassName);
            workfowImplToQueue.put(wfClassName, queueValue.value().toString());
        }

        for (AnnotationInstance ai : combinedIndex.getIndex()
                .getAnnotations(DotName.createSimple(TemporalActivityStub.class.getName()))) {

            String wfClassName = ai.target().asField().declaringClass().name().toString();
            String activityInterface = ai.target().asField().type().toString();
            String queue = workfowImplToQueue.get(wfClassName);
            wrbi.addActivityInterface(queue, activityInterface);
        }

        SyntheticBeanBuildItem runtimeConfigBuildItem = SyntheticBeanBuildItem.configure(WorkflowRuntimeBuildItem.class)
                .scope(Singleton.class)
                .supplier(new GenericSupplier<>(wrbi))
                .done();
        syntheticBeanBuildItemBuildProducer.produce(runtimeConfigBuildItem);

        return new WorkflowBuildItem(wrbi);
    }

    @BuildStep
    void temporalReflections(BuildProducer<ReflectiveClassBuildItem> reflections, CombinedIndexBuildItem combinedIndex) {
        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(ActivityInterface.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            reflections.produce(new ReflectiveClassBuildItem(true, true, true, activityClassName));
        }

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(WorkflowInterface.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            reflections.produce(new ReflectiveClassBuildItem(true, true, true, activityClassName));
        }

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(TemporalActivity.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            reflections.produce(new ReflectiveClassBuildItem(true, true, true, activityClassName));
        }

        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(TemporalWorkflow.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            reflections.produce(new ReflectiveClassBuildItem(true, true, true, activityClassName));
        }
    }

    @BuildStep
    void configureProxies(BuildProducer<NativeImageProxyDefinitionBuildItem> proxies, CombinedIndexBuildItem combinedIndex) {
        // proxies to async internal
        List<String> proxysToAsyncInternal = new ArrayList<>();
        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(ActivityInterface.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            proxysToAsyncInternal.add(activityClassName);
        }

        proxysToAsyncInternal.add("io.temporal.internal.sync.AsyncInternal$AsyncMarker");
        proxies.produce(new NativeImageProxyDefinitionBuildItem(proxysToAsyncInternal));

        // proxies to StubMarker
        List<String> proxiesToStubMarker = new ArrayList<>();
        for (AnnotationInstance ai : combinedIndex.getIndex().getAnnotations(DotName.createSimple(WorkflowInterface.class.getName()))) {
            String activityClassName = ai.target().asClass().name().toString();
            proxiesToStubMarker.add(activityClassName);
        }
        proxiesToStubMarker.add("io.temporal.internal.sync.StubMarker");
        proxies.produce(new NativeImageProxyDefinitionBuildItem(proxiesToStubMarker));

        // temporal classes
        proxies.produce(new NativeImageProxyDefinitionBuildItem("io.temporal.serviceclient.WorkflowServiceStubs"));
        proxies.produce(new NativeImageProxyDefinitionBuildItem("io.temporal.client.WorkflowClient"));

        // app classes
        proxies.produce(new NativeImageProxyDefinitionBuildItem(proxiesToStubMarker.stream().filter(p -> !p.equalsIgnoreCase("io.temporal.internal.sync.StubMarker")).collect(Collectors.toList())));
        proxies.produce(new NativeImageProxyDefinitionBuildItem(proxysToAsyncInternal.stream().filter(p -> !p.equalsIgnoreCase("io.temporal.internal.sync.AsyncInternal$AsyncMarker")).collect(Collectors.toList())));
    }

    @BuildStep
    void initBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans,
                   WorkflowBuildItem workflowBuildItem) {

        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(ActivityBuilder.class).build());
        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(WorkflowBuilder.class).build());

        additionalBeans.produce(AdditionalBeanBuildItem.builder().setUnremovable()
                .addBeanClasses(workflowBuildItem.getWorkflowRuntimeBuildItems().getActivitiesFlat())
                .build());

        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(TemporalBeansProducer.class).build());
    }

}
