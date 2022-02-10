package com.collinchase.carrentalservice.Repository;

import com.collinchase.carrentalservice.Entity.Reservation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

  Reservation findById(long id);

  List<Reservation> findByCarId(long carId);

  List<Reservation> findAll();

  void deleteById(long id);
}
