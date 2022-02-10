package com.collinchase.carrentalservice.Service;


import com.collinchase.carrentalservice.Constants;
import com.collinchase.carrentalservice.Entity.Car;
import com.collinchase.carrentalservice.Entity.Reservation;
import com.collinchase.carrentalservice.Repository.CarRepository;
import com.collinchase.carrentalservice.Repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservationService {

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private ReservationRepository reservationRepository;


  public void tryAndReserveCar(Date startTime, Date endTime, String type) throws Exception {
    if (endTime.before(startTime) || startTime.equals(endTime)) {
      throw new Exception(Constants.invalidTimes);
    }

    List<Car> carsOfRequestedType = carRepository.findByType(type);
    boolean didReserveCar = false;

    //Check if Any of our Cars Are Free During The Requested StartTime to EndTime
    for (Car c: carsOfRequestedType) {
      if (isCarAvailable(startTime, endTime, c)) {
        createReservation(startTime, endTime, c);
        didReserveCar = true;
        break;
      }
    }

    if (!didReserveCar) {
      throw new Exception(Constants.nothingAvailable);
    }

  }



  //Checks if a given car is available to reserve from the given start time to the given end time;
  private boolean isCarAvailable(Date startTime, Date endTime, Car c) {

    //Get All the Reservations For this Car
    List<Reservation> carsReservations = reservationRepository.findByCarId(c.getId());
    boolean isCarAvailable = true;

    for (Reservation r : carsReservations) {
      Date resStart = r.getStartDate();
      Date resEnd = r.getEndDate();

      // If the startTime or the endTime of the requested reservation is between any reservations
      // start and end for this car then it isnt available
      if (startTime.after(resStart) && startTime.before(resEnd) || endTime.after(resStart) &&
              endTime.before(resEnd) || startTime == resStart)  {
        isCarAvailable = false;
        break;
      }
    }
    return isCarAvailable;
  }



  //Create A Reservation and Save it into our Database
  private void createReservation(Date startTime, Date endTime, Car c) {
    Reservation newReservation = new Reservation(startTime, endTime, c);
    reservationRepository.save(newReservation);
  }

  //Delete Already Saved Reservation From our Database
  public void deleteReservation(long reservationId) {
    reservationRepository.deleteById(reservationId);
  }

  public List<Reservation> getAllResvervationsForCar(long carId) {
    return reservationRepository.findByCarId(carId);
  }

  public List<Reservation> getAllReservations() {
    return reservationRepository.findAll();
  }

}
