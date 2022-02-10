package com.collinchase.carrentalservice.Service;


import com.collinchase.carrentalservice.Constants;
import com.collinchase.carrentalservice.Entity.Car;
import com.collinchase.carrentalservice.Entity.Reservation;
import com.collinchase.carrentalservice.Repository.CarRepository;
import com.collinchase.carrentalservice.Repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private ReservationService reservationService;


  // Add a new Car to our Lot of Cars
  public void addNewCar(String type) {
    if (this.canAddCarOfType(type)) {
      Car newCar = new Car(type);
      carRepository.save(newCar);
    }
  }

  // Checks if we can add another car of this type to our lot
  // This is dependent on our limit of cars for the given type which is kept in the Constants class
  private boolean canAddCarOfType(String type) {
    Integer maxCarsForType = 0;
    if (type.equals(Constants.sedan)) {
      maxCarsForType = Constants.maxNumberOfSedans;
    }
    if (type.equals(Constants.van)) {
      maxCarsForType = Constants.maxNumberOfVans;
    }
    if (type.equals(Constants.suv)) {
      maxCarsForType = Constants.maxNumberOfSUVs;
    }

    List<Car> numOfCarsOfType = carRepository.findByType(type);

    return numOfCarsOfType.size() < maxCarsForType;

  }

  //Remove a Car From our Lot, Also remove all reservations that this car had.
  public void removeCar(long carId) {

    List<Reservation> carsReservations = reservationRepository.findByCarId(carId);

    for (Reservation r : carsReservations) {
      reservationService.deleteReservation(r.getReservationId());
    }

    carRepository.deleteById(carId);

  }

  public List<Car> getCarsOfType(String type) {
    return carRepository.findByType(type);
  }


}
