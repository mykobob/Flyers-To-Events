package com.mykobob.flyerstoevents;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Event {

    private Calendar eventInfo;

    private String building;
    private double roomNumber;
    private String information;

    public Event() {
        // Nothing to initialize
    }

    public void setEventInfo(GregorianCalendar e) {
        eventInfo = e;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public double getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(double roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Date getDate() {
        return new Date(getYear(), getMonth(), getDay());
    }

    private int getMonth() {
        return eventInfo.get(Calendar.MONTH);
    }

    private int getDay() {
        return eventInfo.get(Calendar.DAY_OF_MONTH);
    }

    private int getYear() {
        return eventInfo.get(Calendar.YEAR);
    }

}
