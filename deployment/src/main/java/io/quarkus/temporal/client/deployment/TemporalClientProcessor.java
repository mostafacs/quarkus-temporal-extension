package io.quarkus.temporal.client.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.AdditionalApplicationArchiveMarkerBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.temporal.runtime.TemporalAbstractConfiguration;
import io.quarkus.temporal.runtime.TemporalRecorder;
import io.quarkus.temporal.runtime.config.WorkflowConfigurations;
import javax.enterprise.context.ApplicationScoped;

/**
 * @Author Mostafa Albana
 */
class TemporalClientProcessor {

    private static final String FEATURE = "temporal-client";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void initBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(AdditionalBeanBuildItem.builder().addBeanClass(TemporalAbstractConfiguration.class).build());
    }

    @BuildStep
    AdditionalApplicationArchiveMarkerBuildItem required() {
        return new AdditionalApplicationArchiveMarkerBuildItem("workflow.yml");
    }
    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    void buildConfigs(TemporalRecorder recorder,
                      BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer) throws Exception{

        SyntheticBeanBuildItem syntheticBeanBuildItem = SyntheticBeanBuildItem.configure(WorkflowConfigurations.class)
                .scope(ApplicationScoped.class)
                .runtimeValue(recorder.createWorkflowConfigs())
                .done();
        syntheticBeanBuildItemBuildProducer.produce(syntheticBeanBuildItem);

    }

//    buildWorkflowFactories(BeanContainerBuildItem beanContainerBuildItem,
//    CombinedIndexBuildItem combinedIndex
//    ) {
//        WorkflowClient workflowClient = beanContainerBuildItem.getValue().instance(WorkflowClient.class);
//        //Work = recorder.
////        for (AnnotationInstance ai : combinedIndex.getIndex()
////                .getAnnotations(DotName.createSimple(ActivityInterface.class.getName()))) {
////            AnnotationValue flowId = ai.value("value");
////            if (flowId != null && flowId.asString() != null) {
////
////                AnnotationValue activityName = ai.value();
////                recorder.registerFlowReference(ai.target().asClass().name().toString(),
////                        definingDocumentId == null ? "" : definingDocumentId.asString(),
////                        flowId.asString());
////            }
////        }
//    }

}
