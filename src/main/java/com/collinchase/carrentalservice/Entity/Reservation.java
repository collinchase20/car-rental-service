package com.collinchase.carrentalservice.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "starttime")
  private Date startDate;

  @Column(name = "endtime")
  private Date endDate;

  @ManyToOne
  @JoinColumn(name = "carid")
  private Car car;

  protected Reservation () {}

  public Reservation(Date startDate, Date endDate, Car car) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.car = car;
  }

  public Long getReservationId() {
    return this.id;
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public Car getCar() {
    return this.car;
  }



}
