package org.example;

import java.sql.Time;
import java.time.LocalDate;

public class VehicleInfo {

    private String ownerName;
    private String OwnerPhone;
    private String licensePlateNo;

    private LocalDate parkInDate;

    private LocalDate parkOutDate;

    private Double perDayParkingCharge;

    private double charge;


    private Time inTime;
    private Time outTime;

    public Time getInTime() {
        return inTime;
    }

    public void setInTime(Time inTime) {
        this.inTime = inTime;
    }

    public Time getOutTime() {
        return outTime;
    }

    public void setOutTime(Time outTime) {
        this.outTime = outTime;
    }

    private String status;

    private int spotNo;

    public int getSpotNo() {
        return spotNo;
    }

    public void setSpotNo(int spotNo) {
        this.spotNo = spotNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    public LocalDate getParkInDate() {
        return parkInDate;
    }

    public void setParkInDate(LocalDate parkInDate) {
        this.parkInDate = parkInDate;
    }

    public LocalDate getParkOutDate() {
        return parkOutDate;
    }

    public void setParkOutDate(LocalDate parkOutDate) {
        this.parkOutDate = parkOutDate;
    }

    public Double getPerDayParkingCharge() {
        return perDayParkingCharge;
    }

    public void setPerDayParkingCharge(Double perDayParkingCharge) {
        this.perDayParkingCharge = perDayParkingCharge;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }



    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return OwnerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        OwnerPhone = ownerPhone;
    }
}
class TwoWheeler extends VehicleInfo{
    @Override
    public String getLicensePlateNo() {
        return super.getLicensePlateNo();
    }
}
class FourWheeler extends VehicleInfo {
    @Override
    public String getLicensePlateNo() {
        return super.getLicensePlateNo();
    }
}
