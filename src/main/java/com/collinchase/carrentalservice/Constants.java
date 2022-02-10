package com.collinchase.carrentalservice;


// Constants To Be Used Throughout The Program.
// Note: As per the requirements, the number of each car is limited.
// I Set the Limit to 10 each.
public class Constants {

  public static final String sedan = "Sedan";

  public static final String suv = "SUV";

  public static final String van = "Van";

  public static final Integer maxNumberOfSedans = 10;

  public static final Integer maxNumberOfSUVs = 10;

  public static final Integer maxNumberOfVans = 10;

  public static final String tooManyCars = "Our Rental Service Already Has Too Many Cars of" +
          " The Type: ";

  public static final String nothingAvailable = "There are no cars available of the type you" +
          " chose for the given start and end date. Either pick a different type of car," +
          " or choose new times.";

  public static final String invalidTimes = "Cannot Reserve a Car With End Time Before the Start" +
          " Time or the Start Time and End Time Being The Same";

}
