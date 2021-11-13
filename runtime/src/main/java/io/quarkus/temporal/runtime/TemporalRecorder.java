package io.quarkus.temporal.runtime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import io.quarkus.temporal.runtime.config.TemporalConfig;
import io.quarkus.temporal.runtime.config.WorkflowConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;

/**
 * @Author Mostafa
 * Recorder handle bytecode filling during build
 */
@Recorder
public class TemporalRecorder {

    private static final String WORKFLOWS_FILE_CONFIG = "workflow.yml";

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public RuntimeValue<WorkflowConfigurations> createWorkflowConfigs() throws Exception{
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TemporalConfig temporalConfig = new TemporalConfig();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(WORKFLOWS_FILE_CONFIG);
        if(is != null) {
            temporalConfig = mapper.readValue(is, new TypeReference<TemporalConfig>() {});
        }
        return new RuntimeValue<>(new WorkflowConfigurations(temporalConfig));
    }



}
