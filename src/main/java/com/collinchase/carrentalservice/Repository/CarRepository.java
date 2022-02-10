package com.collinchase.carrentalservice.Repository;

import com.collinchase.carrentalservice.Entity.Car;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends CrudRepository<Car, Long> {

  List<Car> findByType(String type);

  Car findById(long id);

  void deleteById(long id);
}
