package io.quarkus.temporal.client.it.saga;

import io.quarkus.temporal.runtime.annotations.TemporalActivityStub;
import io.quarkus.temporal.runtime.annotations.TemporalWorkflow;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Saga;

@TemporalWorkflow(queue = "testQueue", name = "test")
public class TripBookingWorkflowImpl implements TripBookingWorkflow {

    @TemporalActivityStub
    TripBookingActivities activities;

    @Override
    public void bookTrip(String name) {
        Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(true).build();
        Saga saga = new Saga(sagaOptions);
        try {
            String carReservationID = activities.reserveCar(name);
            saga.addCompensation(activities::cancelCar, carReservationID, name);

            String hotelReservationID = activities.bookHotel(name);
            saga.addCompensation(activities::cancelHotel, hotelReservationID, name);

            String flightReservationID = activities.bookFlight(name);
            saga.addCompensation(activities::cancelFlight, flightReservationID, name);
        } catch (ActivityFailure e) {
            saga.compensate();
            throw e;
        }
    }
}
