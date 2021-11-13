package io.quarkus.temporal.client.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Disabled("to run this test it is necessary to have the temporal stack up")
public class TemporalClientResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/trip")
                .then()
                .statusCode(200);
    }
}
