package io.quarkus.temporal.client.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TemporalClientResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/temporal-client")
                .then()
                .statusCode(200)
                .body(is("Hello temporal-client"));
    }
}
