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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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

    private List<Month> months;

    public OCROperations() {
        baseAPI = new TessBaseAPI();
        months = new ArrayList<>();


        // TODO make reading in default values a thread
        readInDefaultValues();
    }

    private void readInDefaultValues() {
        for(String tmpMonth : MONTHS) {
            months.add(new Month(tmpMonth));
        }
    }

    public void setInfo(Bitmap picture) {
        this.picture = picture;
        Log.i("HERE HERE HERE", DATA_PATH);
        baseAPI.init(DATA_PATH, language);
        baseAPI.setImage(picture);

        text = createText();
        baseAPI.end();
     }

    public String createText() {
        return baseAPI.getUTF8Text();
    }

    public String getText() {
        return text;
    }

    public List<Event> getAllEvents(String text) {
        List<Event> events = new ArrayList<>();

        Event curEvent = new Event(text);

        GregorianCalendar time = new GregorianCalendar();
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DATE);

        // Look for several things
        // The Date
        // Format would be
        // MM/DD/YYYY (For now, stick to US)
        //
        Date date = getDate(text);
        // ([insert month])(.?)(?<Day>\d+)( ?)(?<year>\d{2}|\d{4})?



        // The Time
        // Format would be
        // Hour  Minutes AMorPM   OR Hour AMorPM
        // (\d+)(:\d{2})(AM|PM)?|(\d+)( +)(AM|PM)

        // Building and Location

        events.add(curEvent);

        return events;
    }

    private Date getDate(String text) {
        String manipulate = text.toLowerCase();
        manipulate = manipulate.replaceAll("-", "/");

        String regex = "()";
        Pattern pattern = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{2}|\\d{4})?(.*)");

        Matcher m = pattern.matcher(text);
        if(m.matches()) {

        }

        return null;
    }

    private class Month {
        private String[] names;
        private String[] toPrint = new String[2];

        public Month(String data) {
            names = data.split(" ");
            toPrint[0] = names[0];
            toPrint[1] = names[1];
        }

        public boolean isThisMonth(String potentialMonth) {
            for(String compare : names) {
                if(compare.equals(potentialMonth))
                    return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        new OCROperations();
    }

}
