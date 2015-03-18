package com.mykobob.flyerstoevents;

import junit.framework.TestCase;
import junit.framework.Assert;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class OCROperationsTest extends TestCase {

    private OCROperations operation;

    public void setUp() throws Exception {
        operation = new OCROperations();
    }

    public void testEventsWithDigitDate() throws Exception {

        // Standard date
        String dateStr = "7/4/1996";
        List<Event> eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        GregorianCalendar date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 1996, date.get(Calendar.YEAR));

        // Leading 0 on month
        dateStr = "07/4/1996";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 1996, date.get(Calendar.YEAR));

        // Leading 0 on day
        dateStr = "7/04/1996";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 1996, date.get(Calendar.YEAR));

        // Two digit date (meaning 20s)
        // WILL BE DEPRECATED as years go on b/c using "logic" to determine which century
        dateStr = "7/4/14";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

        // Lack of a year -> means it's this year
        dateStr = "7/4";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));

        // Leading 0 on month
        dateStr = "07/4";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));


        // Leading 0 on day
        dateStr = "7/04";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));
    }

    public void testDatesWithNonDigitDates() throws Exception {
        // Now non-digit months
        String dateStr = "jan 4";
        List<Event> eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        GregorianCalendar date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));


        // non-digit months with year and comma
        dateStr = "jAn 4, 2014";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

        // non-digit months with year and without comma
        dateStr = "Jan 4 2014";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));


        // full name month
        dateStr = "january 4";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));


        // full name month plus year and comma
        dateStr = "january 4, 2015";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));


        // full name month plus year and no comma
        dateStr = "january 4 2014";
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        Assert.assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        Assert.assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

    }

    public void testAllowedCharacters() throws Exception {
        assertEquals("Wrong regex", "[a-zA-Z0-9\\p{Punct}]", operation.regexAllowed);
    }
}