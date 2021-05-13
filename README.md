# QUARKUS - TEMPORAL EXTENSION

With this extension you can easily implement a temporal workflow in your quarkus project.

## How to use ?

1- Add extension dependency to your maven POM file.
 ```aidl
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>temporal-client</artifactId>
            <version>${quarkus.version}</version>
        </dependency>
```

2- Add configuration file named `workflow.yml` to resources folder
* Field `name` in annotation `@TemporalWorkflow` used to load workflow configurations 
* Field `name` in annotation `@TemporalActivity` used to load activities configurations

#### Example
```aidl
test:
  executionTimeout: 2
  runTimeout: 2
  taskTimeout: 2
  activities:
    test:
      scheduleTostartTimeout: 10
      scheduleTocloseTimeout: 10
      startTocloseTimeout: 20
      heartbeatTimeout: 2
      retryInitInterval: 1
      retryMaxInterval: 1
      retryMaxAttempts: 1
```

### Declare your Temporal Activities:

```aidl
    @ActivityInterface
    public interface TestActivity {
    
        String hello();
    }
```

```aidl
    // name used to get the activity configurations from workflow.yml
    @TemporalActivity(name="test")
    public class TestActivityImpl implements TestActivity {
    
        // you can inject your services here.
 
        @Override
        public String hello() {
            return "I'm hello activity";
        }
    }
```

### Declare your Temporal Workflows:
```aidl
    @WorkflowInterface
    public interface TestWorkflow {
    
        @WorkflowMethod
        void run();
    }
```

```aidl
    @TemporalWorkflow(queue = "testQueue", name="test")
    public class TestWorkflowImpl implements TestWorkflow {
    
        @TemporalActivityStub
        TestActivity testActivity;
    
        @Override
        public void run() {
            System.out.println(testActivity.hello());
            Workflow.sleep(10000);
            System.out.println("Workflow <<1>> completed");
        }
    }
```

### Run your workflow:

```aidl

    @Path("/temporal-client")
    @ApplicationScoped
    public class TemporalClientController {
    
        @Inject
        WorkflowBuilder workflowBuilder;
    
        @GET
        public String hello() {
            TestWorkflow testWorkflow = workflowBuilder.build(TestWorkflow.class, "test123");
            WorkflowClient.execute(testWorkflow::run);
            return "workfow started";
        }
    }

```
