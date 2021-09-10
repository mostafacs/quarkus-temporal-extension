package io.quarkus.temporal.client.it;

import io.quarkus.temporal.client.it.saga.TripBookingWorkflow;
import io.quarkus.temporal.runtime.builder.WorkflowBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.UUID;

@Path("/trip")
@ApplicationScoped
public class GreetingResource {

    @Inject
    WorkflowBuilder workflowBuilder;

    @GET
    public String start() {
        var uuid = UUID.randomUUID().toString();
        var tripBookingWorkflow = workflowBuilder.build(TripBookingWorkflow.class, uuid);
        tripBookingWorkflow.bookTrip(uuid);
        return "ok";
    }
}