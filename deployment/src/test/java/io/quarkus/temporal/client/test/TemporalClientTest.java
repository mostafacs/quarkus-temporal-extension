package io.quarkus.temporal.client.test;

import io.quarkus.test.QuarkusUnitTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class TemporalClientTest {

    // Start unit test with your extension loaded
    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() ->
                    ShrinkWrap.create(JavaArchive.class)
                            //.addClass(DevModeResource.class)
                            .add(new StringAsset("quarkus.temporal.service.url=localhost:7233"), "application.properties")
            );

    @Test
    public void writeYourOwnUnitTest() {
        // Write your unit tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-extensions for more information
        Assertions.assertTrue(true, "Add some assertions to " + getClass().getName());
    }
}
