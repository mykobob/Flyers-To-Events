package com.mykobob.flyerstoevents;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCROperations {

    private static final String TAG = "com.mykobob.flytoevents";

    private String[] MONTHS = {
            "January Jan january jan", "February Feb february feb", "March Mar march mar",
            "April Apr april apr", "May May may may", "June Jun june jun", "July Jul july jul",
            "August Aug august aug", "September Sept september sept", "October Oct october oct",
            "November Nov november nov", "December Dec december dec"};

    private TessBaseAPI baseAPI;
    private Bitmap picture;
    private String text;
    private String DATA_PATH = Environment.getExternalStorageDirectory() + "/flyerstoevents/";
    private String language = "eng";

    private String[] allowedCharacters= {"a-z", "A-Z", "0-9", "\\p{Punct}"};
    public String regexAllowed;
    private String allMonths = "jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sept(?:ember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?";

    {
        regexAllowed = Arrays.toString(allowedCharacters).replace(", ", "");
        regexAllowed = "[^" + regexAllowed.substring(1);
    }

    private Map<String, Integer> monthsNumber;

    public OCROperations() {
        baseAPI = new TessBaseAPI();
        monthsNumber = new HashMap<>();

        readInDefaultValues();
    }

    private void readInDefaultValues() {
        int value = 1;
        for(String tmpMonth : MONTHS) {
            Scanner in = new Scanner(tmpMonth);
            while(in.hasNext()) {
                monthsNumber.put(in.next(), value);
            }
            value++;
        }
    }

    public void setInfo(Bitmap picture) {
        this.picture = picture;
        Log.i("HERE HERE HERE", DATA_PATH);
        baseAPI.init(DATA_PATH, language);
        baseAPI.setImage(picture);

        text = clean(createText());
        baseAPI.end();
     }

    public String createText() {
        return baseAPI.getUTF8Text();
    }

    public String getText() {
        return text;
    }

    /**
     * Cleans the string for easier parsing
     *
     * @param text Input string
     * @return A string stripped of invalid characters
     */
    public String clean(String text) {
        // Allowed characters...
        return text.trim().replaceAll(regexAllowed, "");
    }

    public List<Event> getAllEvents(String text) {
        List<Event> events = new ArrayList<>();

        Event curEvent = new Event(text);

        GregorianCalendar time = new GregorianCalendar();

        // Look for several things
        // The Date
        // Format would be
        // MM/DD/YYYY (For now, stick to US)
        //
        GregorianCalendar date = getDate(text);
        // ([insert month])(.?)(?<Day>\d+)( ?)(?<year>\d{2}|\d{4})?
        if(date != null) {
            time = date;
        }

        int[] hourMin = getTime(text);
        if(hourMin != null) {
            int hour = hourMin[0];
            if(hourMin[2] == 12) {
                time.set(Calendar.AM_PM, Calendar.PM);
            } else {
                time.set(Calendar.AM_PM, Calendar.AM);
            }
            time.set(Calendar.HOUR, hour);

            time.set(Calendar.MINUTE, hourMin[1]);
        }


        // The Time
        // Format would be
        // Hour  Minutes AMorPM   OR Hour AMorPM
        // (\d+)(:\d{2})(AM|PM)?|(\d+)( +)(AM|PM)


        // Building and Location

        curEvent.setEventInfo(time);
        events.add(curEvent);

        return events;
    }

    private int[] getTime(String text) {
        text = text.toLowerCase();
        int[] hourMin = new int[3];

        // Includes hours and minutes
        Pattern pattern = Pattern.compile(".*?(\\d{1,2}):(\\d{2})( )?(am|pm)(.*)");

        Matcher m = pattern.matcher(text);
        if(m.matches()) {
            hourMin[0] = Integer.parseInt(m.group(1));
            hourMin[1] = Integer.parseInt(m.group(2));
            try {
                String AMorPM = m.group(4);
                hourMin[2] = AMorPM.equals("am") ? 0 : 12;
            } catch (Exception e) {
                hourMin[2] = 0;
            }
            return hourMin;
        }

        // Only the hours
        pattern = Pattern.compile(".*?(\\d{1,2})( )?(am|pm).*");

        m = pattern.matcher(text);
        if(m.matches()) {
            hourMin[0] = Integer.parseInt(m.group(1));
            hourMin[1] = 0;
            try {
                hourMin[2] = m.group(3).equals("am") ? 0 : 12;
            } catch (Exception e) {
                hourMin[2] = 0;
            }
            return hourMin;
        }

        return null;
    }

    private GregorianCalendar getDate(String text) {
        String manipulate = text.toLowerCase();
        manipulate = manipulate.replaceAll("-", "/");

        final int YEAR = new GregorianCalendar().get(Calendar.YEAR);

        // Numbers only
        Pattern pattern = Pattern.compile(".*?(\\d{1,2})/(\\d{1,2})(/(\\d{4}|\\d{2}))?(.*)");

        Matcher m = pattern.matcher(manipulate);
        if(m.matches()) {
//            String[] matched = m.group().split("/");
            int month = Integer.parseInt(m.group(1));
            int day = Integer.parseInt(m.group(2));
            int year = YEAR; // by default, if no year is provided, it's this year
            String yearStr = m.group(4);
            if(yearStr != null) { // a year is supplied
                if(yearStr.length() == 2) { // short hand for year
                    year = Integer.parseInt(yearStr) + (year / 100 * 100);
                } else { // the whole year is provided
                    year = Integer.parseInt(yearStr);
                }
            }
            return new GregorianCalendar(year, month, day);
        }

        // Includes the month name
        String regex = String.format(".*?(%s) ((\\d{1,2})(st|nd|rd|th)?)(,? (\\d{4}|\\d{2}))?(.*)", allMonths);
        pattern = Pattern.compile(regex);

        m = pattern.matcher(manipulate);
        if(m.matches()) {
            String month = m.group(1);
            int day = Integer.parseInt(m.group(3));
            int year;
            try {
                year = Integer.parseInt(m.group(6));
            } catch (Exception e) {
                year = YEAR;
            }
            return new GregorianCalendar(year, getMonthNumber(month), day);

        }

        return null;
    }

    public int getMonthNumber(String str) {
        return monthsNumber.get(str);
    }

    public static void main(String[] args) {
        new OCROperations();
    }

}
