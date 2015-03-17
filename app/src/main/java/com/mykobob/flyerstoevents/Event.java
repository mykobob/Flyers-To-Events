package com.mykobob.flyerstoevents;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Event implements Parcelable {

    private Calendar eventInfo;

    private String building;
    private double roomNumber;
    private String information;
    private String text;


    public Event(String text) {
        // Nothing to initialize
        this.text = text;
    }

    public Event(Parcel source) {
        eventInfo = (GregorianCalendar) source.readValue(GregorianCalendar.class.getClassLoader());
        building = source.readString();
        roomNumber = source.readDouble();
        information = source.readString();
//        Object tmp = source.readValue(StringBuffer.class.getClassLoader());
        text = source.readString();
//        text = new StringBuffer(""+source.readValue(StringBuffer.class.getClassLoader()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(eventInfo);
        dest.writeString(building);
        dest.writeDouble(roomNumber);
        dest.writeString(information);
        dest.writeString(text);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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



    @Override
    public String toString() {
        return text.toString();
    }
}
