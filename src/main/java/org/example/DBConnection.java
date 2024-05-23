package org.example;


import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DBConnection {

    static Scanner in = new Scanner(System.in);
    final int capacityTwo = 5;
    final int capacityFour = 5;
    Two obj = new Two(capacityTwo);
    Four objfour =  new Four(capacityFour);

    private static final String URL = "jdbc:mysql://localhost:3306/spark";
    private static final String username = "root";
    private static final String password = "M@nish13";

    VehicleInfo info = new VehicleInfo();

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;



    public void storeDataOfTwoWheel() throws SQLException {
        obj.number = new ArrayList<>();
        connection = DriverManager.getConnection(URL, username, password);
        preparedStatement = connection.prepareStatement("SELECT * FROM twowheel WHERE status = 'IN' ");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            if (isFullTwo()) {
                String licenseNumber = resultSet.getString("licensePlateNo");
                obj.number.add(licenseNumber);
            }
        }
    }

    public int spotNo1(String licensePlateNo) throws SQLException {
        connection = DriverManager.getConnection(URL, username, password);
        preparedStatement = connection.prepareStatement("SELECT * FROM twowheel WHERE licensePlateNo = ? ");
        preparedStatement.setString(1, licensePlateNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            return resultSet.getInt("spotNo");
        }
        return 0;
    }
    public int spotNo2(String licensePlateNo) throws SQLException {
        connection = DriverManager.getConnection(URL, username, password);
        preparedStatement = connection.prepareStatement("SELECT * FROM fourwheel WHERE licensePlateNo = ? ");
        preparedStatement.setString(1, licensePlateNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            return resultSet.getInt("spotNo");
        }
        return 0;
    }
    public void storeDataOfFourWheel() throws SQLException {
        obj.number = new ArrayList<>();
        connection = DriverManager.getConnection(URL, username, password);
        preparedStatement = connection.prepareStatement("SELECT * FROM fourwheel WHERE status = 'IN'");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            if (isFullFour()) {
                String licenseNumber = resultSet.getString("licensePlateNo");
                objfour.num.add(licenseNumber);
            }
        }
    }


    public int assignSpotForTwoWheeler() throws SQLException {

        String queryOut = "SELECT spotNo FROM twowheel WHERE status = 'OUT'";
        String queryIn = "SELECT spotNo FROM twowheel WHERE status = 'IN'";

        try (
                Connection con = DriverManager.getConnection(URL, username, password);
                PreparedStatement stmtOut = con.prepareStatement(queryOut);
                PreparedStatement stmtIn = con.prepareStatement(queryIn);
                ResultSet rsOut = stmtOut.executeQuery();
                ResultSet rsIn = stmtIn.executeQuery()
        ) {
            // Collect all occupied spots
            Set<Integer> occupiedSpots = new HashSet<>();
            while (rsIn.next()) {
                occupiedSpots.add(rsIn.getInt("spotNo"));
            }

            // Find the first available spot
            while (rsOut.next()) {
                int spotNo = rsOut.getInt("spotNo");
                if (!occupiedSpots.contains(spotNo)) {
                    return spotNo;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception after logging it
        }

        return -1; // No available spot found
    }

    public int assignSpotForFourWheeler() throws SQLException {
        {
            String queryOut = "SELECT spotNo FROM fourwheel WHERE status = 'OUT'";
            String queryIn = "SELECT spotNo FROM fourwheel WHERE status = 'IN'";

            try (
                    Connection con = DriverManager.getConnection(URL, username, password);
                    PreparedStatement stmtOut = con.prepareStatement(queryOut);
                    PreparedStatement stmtIn = con.prepareStatement(queryIn);
                    ResultSet rsOut = stmtOut.executeQuery();
                    ResultSet rsIn = stmtIn.executeQuery()
            ) {
                // Collect all occupied spots
                Set<Integer> occupiedSpots = new HashSet<>();
                while (rsIn.next()) {
                    occupiedSpots.add(rsIn.getInt("spotNo"));
                }

                // Find the first available spot
                while (rsOut.next()) {
                    int spotNo = rsOut.getInt("spotNo");
                    if (!occupiedSpots.contains(spotNo)) {
                        return spotNo;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e; // Re-throw the exception after logging it
            }

            return -1;
        }
    }

    public boolean isFullTwo()
    {
        return obj.number.size() <= capacityTwo;
    }
    public boolean isFullFour(){
        return objfour.num.size() <= capacityFour;
    }
    public void parkVehicle(){
        obj.number = new ArrayList<>();
        objfour.num = new ArrayList<>();
        int perDayParkingCharge = 0;
        String status = null;
        String licensePlateNo;
        System.out.print("Enter your vehicle type :: \nPress 1 for twoWheeler\nPress 2 for fourWheeler");
        System.out.println();

        try {
            int choice = in.nextInt();
            if (choice == 1) {



                if (obj.number.isEmpty()) {
                    storeDataOfTwoWheel();
                }

                if (isFullTwo()) {
                    System.out.print ("Enter License Plate No. ");
                    licensePlateNo = in.next();
                    if (licensePlateNo.length() != 10) {
                        System.out.println("Invalid license plate No. ");
                        Operations.operation();
                    }
                    obj.number.add(licensePlateNo);
                    connection = DriverManager.getConnection(URL, username, password);
                    String sqlQuery = "SELECT * FROM twowheel WHERE licensePlateNo = ?";
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, licensePlateNo);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    //TwoWheel vehicle1 = new TwoWheel(licensePlateNo);
                    while (resultSet.next()) {
                        java.sql.Date date1 = Date.valueOf(LocalDate.now());
                        java.sql.Date date2 = null;
                        Time inTime = Time.valueOf(LocalTime.now());
                        Time outTime = null;

                        preparedStatement = connection.prepareStatement("UPDATE twowheel SET status = 'IN' , parkInDate = ? , parkOutDate = ? , inTime = ? , outTime = ? , spotNo = ? WHERE licensePlateNo = ?");
                        preparedStatement.setDate(1, date1);
                        preparedStatement.setDate(2, date2);
                        preparedStatement.setTime(3, inTime);
                        preparedStatement.setTime(4, outTime);
                        if (assignSpotForTwoWheeler() != -1) {
                            System.out.println(assignSpotForTwoWheeler());
                            preparedStatement.setInt(5, assignSpotForFourWheeler());
                        }
                        else if (assignSpotForTwoWheeler() == -1) {
                            preparedStatement.setInt(5, (obj.number.indexOf(licensePlateNo)+101));
                        }
                        else {
                            System.out.println("No parking spot is available");
                            Operations.operation();
                        }
                        preparedStatement.setString(6, licensePlateNo);
                        preparedStatement.executeUpdate();

                        System.out.println("vehicle with license no. " + licensePlateNo + " has been parked at spot No " + (obj.number.indexOf(licensePlateNo) + 101));
                        Operations.operation();
                        System.out.println();
                        System.out.println("****************************************************************************************************");
                        System.out.println("                 Owner Name :- " + resultSet.getString("ownerName"));
                        System.out.println("                 Owner Phone :- " + resultSet.getString("ownerPhone"));
                        System.out.println("                 License Number :- " + licensePlateNo);
                        System.out.println("                 Park In Date :- " + resultSet.getDate("parkInDate"));
                        System.out.println("                 Park In Time :- " + resultSet.getDate("inTime"));
                        System.out.println("                 Parked at Spot No. :- " + spotNo1(licensePlateNo));
                        System.out.println("*****************************************************************************************************");
                        Operations.operation();
                    }


                    if (obj.number.isEmpty()) {
                        storeDataOfTwoWheel();
                    } else {
                        obj.number.add(licensePlateNo);
                    }

                    System.out.print("Enter Owner's Name ");
                    String ownerName = in.next();
                    System.out.print("Enter Owner's Phone ");
                    String ownerPhone = in.next();
                    if (ownerPhone.length() != 10) {
                        System.out.println("Invalid Mobile No. ");
                        Operations.operation();
                    }
                    Date date = Date.valueOf(LocalDate.now());
                    Time time = Time.valueOf(LocalTime.now());
                    perDayParkingCharge = 50;
                    status = "IN";
                    sqlQuery = "INSERT INTO twowheel(ownerName, ownerPhone, licensePlateNo, parkInDate, perDayParkingCharge, inTime, status, spotNo) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, ownerName);
                    preparedStatement.setString(2, ownerPhone);
                    preparedStatement.setString(3, licensePlateNo);
                    preparedStatement.setDate(4, date);
                    preparedStatement.setTime(5, time);
                    preparedStatement.setDouble(6, perDayParkingCharge);
                    preparedStatement.setString(7, status);
                    if (assignSpotForTwoWheeler() != -1) {
                        preparedStatement.setInt(8, assignSpotForTwoWheeler());
                    }
                    else if (assignSpotForTwoWheeler() == -1) {
                        preparedStatement.setInt(8, (obj.number.indexOf(licensePlateNo)+101));
                    }
                    else {
                        System.out.println("No parking spot is available");
                        Operations.operation();
                    }
                    preparedStatement.executeUpdate();

                    System.out.println("****************************************************************************************************");
                    System.out.println("                 Owner Name :- " + ownerName);
                    System.out.println("                 Owner Phone :- " + ownerPhone);
                    System.out.println("                 License Number :- " + licensePlateNo);
                    System.out.println("                 Park In Date :- " + date);
                    System.out.println("                 Parked at Spot No. :- " + spotNo1(licensePlateNo));
                    //System.out.println("                 Parked at Spot No. :- " + info.getSpotNo());
                    System.out.println("*****************************************************************************************************");
                }


                else {
                    System.out.println("SORRY, No Parking Spot is available.\nTHANKS for coming");
                }



            }
            else if (choice == 2) {

                if (objfour.num.isEmpty()) {
                    storeDataOfFourWheel();
                }
                if (isFullFour()) {
                    System.out.print("Enter License Plate No. ");
                     licensePlateNo = in.next();
                    if (licensePlateNo.length() != 10) {
                        System.out.println("Invalid license plate No. ");
                        Operations.operation();
                    }
                    objfour.num.add(licensePlateNo);
                    connection = DriverManager.getConnection(URL, username, password);
                    String sqlQuery = "SELECT * FROM fourwheel WHERE licensePlateNo = ?";
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, licensePlateNo);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        java.sql.Date date1 = Date.valueOf(LocalDate.now());
                        java.sql.Date date2 = null;
                        Time inTime = Time.valueOf(LocalTime.now());
                        Time outTime = null;

                        preparedStatement = connection.prepareStatement("UPDATE fourwheel SET status = 'IN' , parkInDate = ? , parkOutDate = ? , inTime = ? , outTime = ? , spotNo = ? WHERE licensePlateNo = ?");
                        preparedStatement.setDate(1, date1);
                        preparedStatement.setDate(2, date2);
                        preparedStatement.setTime(3, inTime);
                        preparedStatement.setTime(4, outTime);

                        if (assignSpotForFourWheeler() != -1) {
                            preparedStatement.setInt(5, assignSpotForFourWheeler());
                        }
                        else if (assignSpotForTwoWheeler() == -1) {
                            preparedStatement.setInt(5, (objfour.num.indexOf(licensePlateNo)+101));
                        }
                        else {
                            System.out.println("No parking spot is available");
                            Operations.operation();
                        }
                        preparedStatement.setString(6, licensePlateNo);
                        preparedStatement.executeUpdate();

                        System.out.println("vehicle with license no. " + licensePlateNo + " has been parked at spot No " + (objfour.num.indexOf(licensePlateNo) + 101));
                        System.out.println();
                        System.out.println("****************************************************************************************************");
                        System.out.println("                 Owner Name :- " + resultSet.getString("ownerName"));
                        System.out.println("                 Owner Phone :- " + resultSet.getString("ownerPhone"));
                        System.out.println("                 License Number :- " + licensePlateNo);
                        System.out.println("                 Park In Date :- " + resultSet.getDate("parkInDate"));
                        System.out.println("                 Park In Date :- " + resultSet.getTime("inTime"));

                        System.out.println("                 Parked at Spot No. :- " + spotNo2(licensePlateNo));
                        System.out.println("*****************************************************************************************************");
                        Operations.operation();
                    }


                    if (objfour.num.isEmpty()) {
                        storeDataOfFourWheel();
                    } else {
                        objfour.num.add(licensePlateNo);
                    }

                    System.out.print("Enter Owner's Name ");
                    String ownerName = in.next();
                    System.out.print("Enter Owner's Phone ");
                    String ownerPhone = in.next();
                    if (ownerPhone.length() != 10) {
                        System.out.println("Invalid Mobile No. ");
                        Operations.operation();
                    }
                    Date date = Date.valueOf(LocalDate.now());
                    Time time = Time.valueOf(LocalTime.now());
                    perDayParkingCharge = 100;
                    status = "IN";
                    sqlQuery = "INSERT INTO fourwheel(ownerName, ownerPhone, licensePlateNo, parkInDate, inTime, perDayParkingCharge, status, spotNo) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, ownerName);
                    preparedStatement.setString(2, ownerPhone);
                    preparedStatement.setString(3, licensePlateNo);
                    preparedStatement.setDate(4, date);
                    preparedStatement.setTime(5, time);
                    preparedStatement.setDouble(6, perDayParkingCharge);
                    preparedStatement.setString(7, status);
                    if (assignSpotForFourWheeler() != -1) {
                        preparedStatement.setInt(8, assignSpotForFourWheeler());
                    }
                    else if (assignSpotForFourWheeler() == -1) {
                        preparedStatement.setInt(8, (objfour.num.indexOf(licensePlateNo)+101));
                    }
                    else {
                        System.out.println("No parking spot is available");
                        Operations.operation();
                    }
                    preparedStatement.executeUpdate();

                    System.out.println("****************************************************************************************************");
                    System.out.println("                 Owner Name :- " + ownerName);
                    System.out.println("                 Owner Phone :- " + ownerPhone);
                    System.out.println("                 License Number :- " + licensePlateNo);
                    System.out.println("                 Park In Date :- " + date);
                    System.out.println("                 Park In Time :- " + time);
                    System.out.println("                 Parked at Spot No. :- " + spotNo2(licensePlateNo));
                    System.out.println("*****************************************************************************************************");
                }
                else {
                    System.out.println("SORRY, No Parking Spot is available.\nTHANKS for coming");
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
    public void parkOutVehicle(){
        try {
            System.out.println("Press 1 for two wheeler\nPress 2 for four wheeler");
            int type = in.nextInt();
            if (type == 1) {
                System.out.print("Enter License Plate No. ");
                String licensePlateNo = in.next();
                    connection = DriverManager.getConnection(URL, username, password);
                    String sqlQuery = "SELECT * FROM twowheel WHERE licensePlateNo = ?";
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, licensePlateNo);

                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        java.sql.Date parkOutDate = Date.valueOf(LocalDate.now());
                        int Day = parkOutDate.getDate();
                        System.out.println(Day);
                        java.sql.Date Date1 = resultSet.getDate("parkInDate");
                        int day = Date1.getDate();
                        Time time = Time.valueOf(LocalTime.now());

                        preparedStatement = connection.prepareStatement("UPDATE twowheel SET status = 'OUT', parkOutDate = ?, outTime = ? WHERE licensePlateNo = ?");
                        preparedStatement.setDate(1, parkOutDate);
                        preparedStatement.setTime(2, time);
                        preparedStatement.setString(3, licensePlateNo);
                        preparedStatement.executeUpdate();

                        if (resultSet.getString("status").contentEquals("IN")){
                            if ((Day - day) == 0){
                                System.out.println("You are to pay "+resultSet.getDouble("perDayParkingCharge")+"RS. as parking charge");
                            }
                            else {
                                System.out.println("You are to pay "+(Day-day)*resultSet.getDouble("perDayParkingCharge")+"RS. as parking charge");
                            }
                        }
                        Operations.operation();

                    }
                System.out.println("Vehicle with License Plate no. "+licensePlateNo+" has not found in parking....");

            }
            else if (type == 2) {
                System.out.print("Enter License Plate No. ");
                String licensePlateNo = in.next();
                connection = DriverManager.getConnection(URL, username, password);
                String sqlQuery = "SELECT * FROM fourwheel WHERE licensePlateNo = ?";
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, licensePlateNo);

                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    java.sql.Date parkOutDate = Date.valueOf(LocalDate.now());
                    int Day = parkOutDate.getDate();
                    java.sql.Date Date1 = resultSet.getDate("parkInDate");
                    int day = Date1.getDate();
                    Time time = Time.valueOf(LocalTime.now());

                    preparedStatement = connection.prepareStatement("UPDATE fourwheel SET status = 'OUT', parkOutDate = ?, outTime = ? WHERE licensePlateNo = ?");
                    preparedStatement.setDate(1, parkOutDate);
                    preparedStatement.setTime(2, time);
                    preparedStatement.setString(3, licensePlateNo);
                    preparedStatement.executeUpdate();

                    if (resultSet.getString("status").contentEquals("IN")){
                        //lot1.remove(licensePlateNo);
                        if ((Day - day) == 0){
                            System.out.println("You are to pay "+resultSet.getDouble("perDayParkingCharge")+"RS. as parking charge");
                        }
                        else {
                            System.out.println("You are to pay "+(Day-day)*resultSet.getDouble("perDayParkingCharge")+"RS. as parking charge");
                        }
                    }
                    Operations.operation();
                }

                System.out.println("Vehicle with License Plate no. "+licensePlateNo+" has not found in parking....");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkParkingChargeForVehicle(){
        double charge;
        try {
            System.out.println("Press 1 for two wheeler\nPress 2 for four wheeler");
            int type = in.nextInt();
            if (type == 1) {
                System.out.print("Enter License Plate No. ");
                String licensePlateNo = in.next();
                connection = DriverManager.getConnection(URL, username, password);
                String sqlQuery = "SELECT * FROM twowheel WHERE licensePlateNo = ?";
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, licensePlateNo);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    java.sql.Date upToDate = Date.valueOf(LocalDate.now());
                    int Day = upToDate.getDate();
                    java.sql.Date date = resultSet.getDate("parkInDate");
                    int day = date.getDate();
                    if ((Day-day) == 0 && resultSet.getString("status").contentEquals("IN")){
                        System.out.println("**************************************************************************************************************************************************************");
                        System.out.println();
                        System.out.println(                                                   "Owner :- "+resultSet.getString("ownerName"));
                        System.out.println("                                                 Park In Date :- "+resultSet.getString("parkInDate"));
                        System.out.println("                                                 Park In Date :- "+resultSet.getTime("inTime"));
                        System.out.println("                 The parking charge for vehicle with License Plate No. "+resultSet.getString("licensePlateNo")+" is "+resultSet.getDouble("perDayParkingCharge")+"Rs.");
                        System.out.println("                                                     Today :- "+ upToDate);
                        System.out.println();
                        System.out.println("**************************************************************************************************************************************************************");

                    }


                    else if ((Day-day) > 0 && resultSet.getString("status").contentEquals("IN")) {
                        System.out.println("**************************************************************************************************************************************************************");
                        System.out.println();
                        System.out.println(                                                   "Owner :- "+resultSet.getString("ownerName"));
                        System.out.println("                                                 Park In Date :- "+resultSet.getString("parkInDate"));
                        System.out.println("                                                 Park In Date :- "+resultSet.getTime("inTime"));
                        System.out.println("                 The parking charge for vehicle with License Plate No. "+resultSet.getString("licensePlateNo")+" is "+(Day-day)*resultSet.getDouble("perDayParkingCharge")+"Rs.");
                        System.out.println("                                                     Today :- "+ upToDate);
                        System.out.println();
                        System.out.println("**************************************************************************************************************************************************************");
                    }
                    else if (resultSet.getString("status").contentEquals("OUT")){
                        System.out.println("You vehicle is already parked out");
                    }
                    Operations.operation();



                }
                    System.out.println("Vehicle with License Plate no. "+licensePlateNo+" has not found in parking....");

            }
            else if (type == 2) {
                System.out.print("Enter License Plate No. ");
                String licensePlateNo = in.next();
                connection = DriverManager.getConnection(URL, username, password);
                String sqlQuery = "SELECT * FROM fourwheel WHERE licensePlateNo = ?";
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, licensePlateNo);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    java.sql.Date upToDate = Date.valueOf(LocalDate.now());
                    int Day = upToDate.getDate();
                    java.sql.Date date = resultSet.getDate("parkInDate");
                    int day = date.getDate();
                    if ((Day - day) == 0 && resultSet.getString("status").contentEquals("IN")) {
                        System.out.println("**************************************************************************************************************************************************************");
                        System.out.println();
                        System.out.println("                                                 Owner :- " + resultSet.getString("ownerName"));
                        System.out.println("                                                 Park In Date :- " + resultSet.getString("parkInDate"));
                        System.out.println("                                                 Park In Time :- " + resultSet.getTime("inTime"));

                        System.out.println("                 The parking charge for vehicle with License Plate No. " + resultSet.getString("licensePlateNo") + " is " + resultSet.getDouble("perDayParkingCharge") + "Rs.");
                        System.out.println("                                                     Today :- " + upToDate);
                        System.out.println();
                        System.out.println("**************************************************************************************************************************************************************");
                    } else if ((Day - day) > 0 && resultSet.getString("status").contentEquals("IN")) {
                        System.out.println("**************************************************************************************************************************************************************");
                        System.out.println();
                        System.out.println("                                             Owner :- " + resultSet.getString("ownerName"));
                        System.out.println("                                        Park In Date :- " + resultSet.getString("parkInDate"));
                        System.out.println("                                        Park In Time :- " + resultSet.getTime("inTime"));
                        System.out.println("                 The parking charge for vehicle with License Plate No. " + resultSet.getString("licensePlateNo") + " is " + (Day - day) * resultSet.getDouble("perDayParkingCharge") + "Rs.");
                        System.out.println("                                                      Today :- " + upToDate);
                        System.out.println();
                        System.out.println("**************************************************************************************************************************************************************");
                    } else if (resultSet.getString("status").contentEquals("OUT")) {
                        System.out.println("You vehicle is already parked out");
                    }
                    Operations.operation();

                }
                    System.out.println("Vehicle with License Plate no. "+licensePlateNo+" has not found in parking....");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getVehicleInfo() {
        try {
            System.out.println("Press 1 for two wheeler\nPress 2 for four wheeler");
            int type = in.nextInt();
            if (type == 1) {
                System.out.print("Enter License Plate No. ");
                String licensePlateNo = in.next();
                connection = DriverManager.getConnection(URL, username, password);
                String sqlQuery = "SELECT * FROM twowheel WHERE licensePlateNo = ?";
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, licensePlateNo);
                resultSet = preparedStatement.executeQuery();
                System.out.println();
                while (resultSet.next()){
                    System.out.println();
                    System.out.println("***************** Your vehicle details are *******************");
                    System.out.println();
                    System.out.println("Owner,s Name            ::   "+resultSet.getString("ownerName"));
                    System.out.println("Owner,s Phone           ::   "+resultSet.getString("ownerPhone"));
                    System.out.println("License Plate Number    ::   "+resultSet.getString("licensePlateNo"));
                    System.out.println("Vehicle Parked at       ::   "+resultSet.getDate("parkInDate"));
                    System.out.println("Vehicle Park In Time       ::   "+resultSet.getTime("inTime"));
                    System.out.println("Per Day Parking Charge  ::   "+resultSet.getDouble("perDayParkingCharge"));
                    System.out.println("Status                  ::   "+resultSet.getString("status"));
                    System.out.println();
                    System.out.println("************************* THANKS *****************************");
                    System.out.println();
                    Operations.operation();

                }
                    System.out.println("Vehicle with License Plate no. "+licensePlateNo+" has not found in parking....");


            }
            else if (type == 2) {
                System.out.print("Enter License Plate No. ");
                String licensePlateNo = in.nextLine();
                connection = DriverManager.getConnection(URL, username, password);
                String sqlQuery = "SELECT * FROM fourwheel WHERE licensePlateNo = ?";
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, licensePlateNo);
                resultSet = preparedStatement.executeQuery();
                System.out.println();
                while (resultSet.next()){
                    System.out.println("**************************************************************");
                    System.out.println();
                    System.out.println("***************** Your vehicle details are *******************");
                    System.out.println("Owner,s Name            ::   "+resultSet.getString("ownerName"));
                    System.out.println("Owner,s Phone           ::   "+resultSet.getString("ownerPhone"));
                    System.out.println("License Plate Number    ::   "+resultSet.getString("licensePlateNo"));
                    System.out.println("Vehicle Parked at       ::   "+resultSet.getDate("parkInDate"));
                    System.out.println("Vehicle Park In Time       ::   "+resultSet.getTime("inTime"));
                    System.out.println("Per Day Parking Charge  ::   "+resultSet.getDouble("perDayParkingCharge"));
                    System.out.println("Status                  ::   "+resultSet.getString("status"));
                    System.out.println("************************* THANKS *****************************");
                    System.out.println();
                    System.out.println("***************************************************************");
                    Operations.operation();

                }
                    System.out.println("Vehicle with License Plate no. "+licensePlateNo+" has not found in parking....");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
