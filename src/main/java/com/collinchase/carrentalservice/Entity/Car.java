package com.collinchase.carrentalservice.Entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "car")
public class Car {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "type")
  private String type;

  protected Car() {}

  public Car(String type) {
    this.type = type;
  }

  public Long getId() {
    return this.id;
  }

  public String getType() {
    return this.type;
  }

}
