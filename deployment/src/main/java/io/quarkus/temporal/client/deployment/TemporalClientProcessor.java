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
import io.quarkus.temporal.runtime.GenericSupplier;
import io.quarkus.temporal.runtime.TemporalBeansProducer;
import io.quarkus.temporal.runtime.TemporalRecorder;
import io.quarkus.temporal.runtime.TemporalRequestScopeInterceptor;
import io.quarkus.temporal.runtime.WorkflowRuntimeBuildItem;
import io.quarkus.temporal.runtime.annotations.TemporalActivity;
import io.quarkus.temporal.runtime.annotations.TemporalWorkflow;
import io.quarkus.temporal.runtime.builder.ActivityBuilder;
import io.quarkus.temporal.runtime.builder.WorkflowBuilder;
import io.quarkus.temporal.runtime.config.WorkflowConfigurations;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @Author Mostafa Albana
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

        for (AnnotationInstance ai : combinedIndex.getIndex()
                .getAnnotations(DotName.createSimple(TemporalActivity.class.getName()))) {

            AnnotationValue[] queuesAnnValues = (AnnotationValue[]) ai.value("queue").value();
            String activityClassName = ai.target().asClass().name().toString();
            for(AnnotationValue queueValue : queuesAnnValues) {
                wrbi.addActivityImpl(queueValue.value().toString(), activityClassName);
            }
        }

        for (AnnotationInstance ai : combinedIndex.getIndex()
                .getAnnotations(DotName.createSimple(TemporalWorkflow.class.getName()))) {

            AnnotationValue queueValue = ai.value("queue");
            String wfClassName = ai.target().asClass().name().toString();
            wrbi.addWorkflowImpl(queueValue.value().toString(), wfClassName);
        }

        SyntheticBeanBuildItem runtimeConfigBuildItem = SyntheticBeanBuildItem.configure(WorkflowRuntimeBuildItem.class)
                .scope(Singleton.class)
                .supplier(new GenericSupplier<>(wrbi))
                .done();
        syntheticBeanBuildItemBuildProducer.produce(runtimeConfigBuildItem);

        return new WorkflowBuildItem(wrbi);
    }

    @BuildStep
    void initBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans,
                   WorkflowBuildItem workflowBuildItem) {

        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(TemporalRequestScopeInterceptor.class).build());
        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(ActivityBuilder.class).build());
        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(WorkflowBuilder.class).build());

        additionalBeans.produce(AdditionalBeanBuildItem.builder().setUnremovable()
                .addBeanClasses(workflowBuildItem.getWorkflowRuntimeBuildItems().getActivitiesFlat())
                .build());

        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(TemporalBeansProducer.class).build());
    }

}
