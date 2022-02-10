package com.collinchase.carrentalservice;


import com.collinchase.carrentalservice.Entity.Car;
import com.collinchase.carrentalservice.Entity.Reservation;
import com.collinchase.carrentalservice.Service.CarService;
import com.collinchase.carrentalservice.Service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
public class CarRentalServiceController {

  @Autowired
  private CarService carService;

  @Autowired
  private ReservationService reservationService;



  public void reserveCar(Date startTime, Date endTime, String type) throws Exception {
    reservationService.tryAndReserveCar(startTime, endTime, type);
  }

  public void deleteReservation(long reservationId) {
    reservationService.deleteReservation(reservationId);
  }

  public void addNewCar(String type) {
    carService.addNewCar(type);
  }

  public void removeCar(long carId) {
    carService.removeCar(carId);
  }


  public List<Car> getAllCarsOfType(String type) {
    return carService.getCarsOfType(type);
  }

  public List<Reservation> getAllReservationsForCar(long carId) {
    return reservationService.getAllResvervationsForCar(carId);
  }

  public List<Reservation> getAllReservations() {
    return reservationService.getAllReservations();
  }


}
