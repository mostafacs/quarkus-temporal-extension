/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package io.quarkus.temporal.client.it;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.inject.Inject;

import io.quarkus.temporal.client.it.workflow.TestActivityImpl;
import io.quarkus.temporal.client.it.workflow.TestWorkflow;
import io.quarkus.temporal.runtime.builder.WorkflowBuilder;
import io.temporal.client.WorkflowClient;
import io.quarkus.temporal.runtime.config.WorkflowConfigurations;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;


@Path("/temporal-client")
@ApplicationScoped
public class TemporalClientResource {
    // add some rest methods here


//    @Inject
//    WorkerFactory workerFactory;
//
//    @Inject
//    WorkflowClient workflowClient;

//    @Inject
//    WorkflowConfigurations workflowConfigurations;

    @Inject
    WorkflowBuilder workflowBuilder;

//    @Inject
//    TestActivityImpl testActivity;

    @GET
    public String hello() {

//        Worker worker = workerFactory.getWorker("testQueue");
//        workflowConfigurations.activityHeartTimeout(Object.class, Object.class);
//        WorkflowClientOptions opt= workflowClient.getOptions();
//        System.out.println(testActivity.hello());

        TestWorkflow testWorkflow = workflowBuilder.build(TestWorkflow.class, "test123");
        testWorkflow.run();
        return "workfow started";
    }
}
