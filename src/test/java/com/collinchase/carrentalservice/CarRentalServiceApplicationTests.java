package com.collinchase.carrentalservice;

import com.collinchase.carrentalservice.Entity.Car;
import com.collinchase.carrentalservice.Entity.Reservation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


// Note: I Am using DataJpaTest so that the database is reset after each test to what was
// initially populated with the data.sql file under resources.

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = {Controller.class, Service.class}))
public class CarRentalServiceApplicationTests {

	@Autowired
  private CarRentalServiceController carRentalServiceController;

	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

  @Test
  public void testAddCar() throws Exception {
    //Note that our database is initialized with 8 cars of each type in data.sql
    carRentalServiceController.addNewCar(Constants.sedan);
    List<Car> sedanCars = carRentalServiceController.getAllCarsOfType(Constants.sedan);
    assert(sedanCars.size() == 9);

    carRentalServiceController.addNewCar(Constants.van);
    List<Car> vanCars = carRentalServiceController.getAllCarsOfType(Constants.van);
    assert(vanCars.size() == 9);

    carRentalServiceController.addNewCar(Constants.suv);
    List<Car> suvCars = carRentalServiceController.getAllCarsOfType(Constants.suv);
    assert(suvCars.size() == 9);
  }

  @Test
  public void testRemoveCar() {
    //Note that our database is initialized with 8 cars of each type in data.sql
    //The cars IDs are auto-generated in ascending order of how they were added to the database
    //I added 8 sedans then 8 suvs then 8 vans
    carRentalServiceController.removeCar(1);
    List<Car> sedanCars = carRentalServiceController.getAllCarsOfType(Constants.sedan);
    assert(sedanCars.size() == 7);

    carRentalServiceController.removeCar(9);
    List<Car> suvCars = carRentalServiceController.getAllCarsOfType(Constants.suv);
    assert(suvCars.size() == 7);

    carRentalServiceController.removeCar(20);
    List<Car> vanCars = carRentalServiceController.getAllCarsOfType(Constants.van);
    assert(vanCars.size() == 7);
  }

  @Test
  public void testRemoveCarWithReservations() throws Exception {
    Date startDate = formatter.parse("08-02-2022 08:30:00");
    Date endDate = formatter.parse("10-02-2022 08:30:00");
    carRentalServiceController.reserveCar(startDate, endDate, Constants.van);
    List<Reservation> totalReservations = carRentalServiceController.getAllReservations();
    assert(totalReservations.size() == 1);

    //Get The Reservation We Just Made
    List<Reservation> reservations = carRentalServiceController.getAllReservations();
    Car myCar = reservations.get(0).getCar();

    carRentalServiceController.removeCar(myCar.getId());

    //Get All Reservations and Make Sure There are none
    List<Reservation> zeroReservations = carRentalServiceController.getAllReservations();
    assert(zeroReservations.size() == 0);

  }

	@Test
	public void testCannotAddMoreThanLimitOfAnyCar() throws Exception {
    carRentalServiceController.addNewCar(Constants.sedan);
    carRentalServiceController.addNewCar(Constants.sedan);
    Exception thrown = assertThrows(Exception.class, () -> {
      carRentalServiceController.addNewCar(Constants.sedan);
    });
		List<Car> sedanCars = carRentalServiceController.getAllCarsOfType(Constants.sedan);
		assert(sedanCars.size() == 10);
    assertTrue(thrown.getMessage().contains(Constants.tooManyCars));

    carRentalServiceController.addNewCar(Constants.van);
    carRentalServiceController.addNewCar(Constants.van);
    Exception thrown2 = assertThrows(Exception.class, () -> {
      carRentalServiceController.addNewCar(Constants.van);
    });
		List<Car> vanCars = carRentalServiceController.getAllCarsOfType(Constants.van);
		assert(vanCars.size() == 10);
    assertTrue(thrown2.getMessage().contains(Constants.tooManyCars));

    carRentalServiceController.addNewCar(Constants.suv);
    carRentalServiceController.addNewCar(Constants.suv);
    Exception thrown3 = assertThrows(Exception.class, () -> {
      carRentalServiceController.addNewCar(Constants.suv);
    });
		List<Car> suvCars = carRentalServiceController.getAllCarsOfType(Constants.suv);
		assert(suvCars.size() == 10);
    assertTrue(thrown3.getMessage().contains(Constants.tooManyCars));
	}

	@Test
	public void testCreateReservation() throws Exception {
		Date startDate = formatter.parse("08-02-2022 08:30:00");
		Date endDate = formatter.parse("10-02-2022 08:30:00");
		carRentalServiceController.reserveCar(startDate, endDate, Constants.sedan);
    carRentalServiceController.reserveCar(startDate, endDate, Constants.van);
    carRentalServiceController.reserveCar(startDate, endDate, Constants.suv);
		List<Reservation> totalReservations = carRentalServiceController.getAllReservations();
		System.out.println(totalReservations.size());
		assert(totalReservations.size() == 3);
	}

  @Test
  public void testCannotCreateOverlappingResveration() throws Exception {
    Date startDate = formatter.parse("08-02-2022 08:30:00");
    Date endDate = formatter.parse("10-02-2022 08:30:00");
    //Reserve All 8 Of Our Sedans For the Above TimeFrame
    for (int i = 0; i <= 7; i++) {
      carRentalServiceController.reserveCar(startDate, endDate, Constants.sedan);
    }
    //Try And Reserve a Sedan Overlapping This Time
    Date overLappingStartDate = formatter.parse("09-02-2022 08:30:00");
    Date overLappingEndDate = formatter.parse("11-02-2022 08:30:00");
    Exception thrown = assertThrows(Exception.class, () -> {
      carRentalServiceController.reserveCar(overLappingStartDate,
              overLappingEndDate, Constants.sedan);
    });
    assertTrue(thrown.getMessage().contains(Constants.nothingAvailable));
  }



	@Test
	public void testInvalidReservationEndTimeBeforeStartTime() {

		Exception thrown = assertThrows(Exception.class, () -> {
			Date start = formatter.parse("10-02-2022 08:30:00");
			Date end = formatter.parse("08-02-2022 08:30:00");
      carRentalServiceController.reserveCar(start, end, Constants.sedan);
		});

		assertTrue(thrown.getMessage().contains(Constants.invalidTimes));
	}

	@Test
	public void testInvalidReservationEndTimeEqualsStartTime() {

		Exception thrown = assertThrows(Exception.class, () -> {
      Date start = formatter.parse("08-02-2022 08:30:00");
      Date end = formatter.parse("08-02-2022 08:30:00");
      carRentalServiceController.reserveCar(start, end, Constants.sedan);
		});

		assertTrue(thrown.getMessage().contains(Constants.invalidTimes));
	}

  @Test
  public void testCreateReservationStartTimeEqualsExistingEndTime() throws Exception {
    Date startDate = formatter.parse("08-02-2022 08:30:00");
    Date endDate = formatter.parse("10-02-2022 08:30:00");
    //Reserve All 8 Of Our Sedans For the Above TimeFrame
    for (int i = 0; i <= 7; i++) {
      carRentalServiceController.reserveCar(startDate, endDate, Constants.sedan);
    }
    //Try And Reserve a Sedan To Start At the EndTime of the above reservations.
    //I designed the code for this to work considering if someone returns a car at 8:30 am someone
    //else should be able to reserve that car starting right away.
    Date startDateSameAsEndDate = formatter.parse("10-02-2022 08:30:00");
    Date someEndDate = formatter.parse("20-02-2022 011:45:00");

    carRentalServiceController.reserveCar(startDateSameAsEndDate, someEndDate, Constants.sedan);

    List<Reservation> reservations = carRentalServiceController.getAllReservations();
    assert(reservations.size() == 9);
  }

  @Test
  public void testCreateReservationAndAddNewCar() throws Exception {
    Date startDate = formatter.parse("08-02-2022 08:30:00");
    Date endDate = formatter.parse("10-02-2022 08:30:00");
    //Reserve All 8 Of Our Sedans For the Above TimeFrame
    for (int i = 0; i <= 7; i++) {
      carRentalServiceController.reserveCar(startDate, endDate, Constants.sedan);
    }
    //Add a new Sedan to our Lot
    carRentalServiceController.addNewCar(Constants.sedan);
    //Now that there is a 9th sedan in our lot should be able to reserve it for the above time.
    carRentalServiceController.reserveCar(startDate, endDate, Constants.sedan);
    List<Reservation> reservations = carRentalServiceController.getAllReservations();
    assert(reservations.size() == 9);
  }

}
