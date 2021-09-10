package io.quarkus.temporal.client.it.saga;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface TripBookingActivities {

  /**
   * Request a car rental reservation.
   *
   * @param name customer name
   * @return reservationID
   */
  String reserveCar(String name);

  /**
   * Request a flight reservation.
   *
   * @param name customer name
   * @return reservationID
   */
  String bookFlight(String name);

  /**
   * Request a hotel reservation.
   *
   * @param name customer name
   * @return reservationID
   */
  String bookHotel(String name);

  /**
   * Cancel a flight reservation.
   *
   * @param name customer name
   * @param reservationID id returned by bookFlight
   * @return cancellationConfirmationID
   */
  String cancelFlight(String reservationID, String name);

  /**
   * Cancel a hotel reservation.
   *
   * @param name customer name
   * @param reservationID id returned by bookHotel
   * @return cancellationConfirmationID
   */
  String cancelHotel(String reservationID, String name);

  /**
   * Cancel a car rental reservation.
   *
   * @param name customer name
   * @param reservationID id returned by reserveCar
   * @return cancellationConfirmationID
   */
  String cancelCar(String reservationID, String name);
}