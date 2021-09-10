package io.quarkus.temporal.client.it.saga;

import io.quarkus.temporal.runtime.annotations.TemporalActivity;

import java.util.UUID;

@TemporalActivity(name = "test")
public class TripBookingActivitiesImpl implements TripBookingActivities {

    @Override
    public String reserveCar(String name) {
        System.out.println("reserve car for '" + name + "'");
        return UUID.randomUUID().toString();
    }

    @Override
    public String bookFlight(String name) {
        System.out.println("reserve book flight for '" + name + "'");
        return UUID.randomUUID().toString();
    }

    @Override
    public String bookHotel(String name) {
        System.out.println("booking hotel for '" + name + "'");
        return UUID.randomUUID().toString();
    }

    @Override
    public String cancelFlight(String reservationID, String name) {
        System.out.println("cancelling flight reservation '" + reservationID + "' for '" + name + "'");
        return UUID.randomUUID().toString();
    }

    @Override
    public String cancelHotel(String reservationID, String name) {
        System.out.println("cancelling hotel reservation '" + reservationID + "' for '" + name + "'");
        return UUID.randomUUID().toString();
    }

    @Override
    public String cancelCar(String reservationID, String name) {
        System.out.println("cancelling car reservation '" + reservationID + "' for '" + name + "'");
        return UUID.randomUUID().toString();
    }
}
