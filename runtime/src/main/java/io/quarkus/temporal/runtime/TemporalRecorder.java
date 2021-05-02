package io.quarkus.temporal.runtime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import io.quarkus.temporal.runtime.config.WorkflowConfig;
import io.quarkus.temporal.runtime.config.WorkflowConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


@Recorder
public class TemporalRecorder {

    private static final String WORKFLOWS_FILE_CONFIG = "/Users/mac/quarkus-temporal-extension/integration-tests/src/main/resources/workflow.yml";

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public RuntimeValue<WorkflowConfigurations> createWorkflowConfigs() {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<String, WorkflowConfig> configs = null;
        try {
            InputStream is = new FileInputStream(WORKFLOWS_FILE_CONFIG);//getClass().getResourceAsStream(WORKFLOWS_FILE_CONFIG);
            configs = mapper.readValue(is, new TypeReference<Map<String, WorkflowConfig>>() {});
            return new RuntimeValue<>(new WorkflowConfigurations(configs));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
