package com.mykobob.flyerstoevents;

import junit.framework.TestCase;
import junit.framework.Assert;


import java.util.Date;
import java.util.List;


public class OCROperationsTest extends TestCase {

    private OCROperations operation;

    public void setUp() throws Exception {
        operation = new OCROperations();
    }

    public void testGetAllEvents() throws Exception {

        // Standard date
        StringBuffer dateStr = new StringBuffer("7/4/1996");
        List<Event> eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        Date date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 1996, date.getYear());

        // Leading 0 on month
        dateStr = new StringBuffer("07/4/1996");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 1996, date.getYear());

        // Leading 0 on day
        dateStr = new StringBuffer("7/04/1996");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 1996, date.getYear());

        // Two digit date (meaning 20s)
        // WILL BE DEPRECATED as years go on b/c using "logic" to determine which century
        dateStr = new StringBuffer("7/4/14");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2014, date.getYear());

        // Lack of a year -> means it's this year
        dateStr = new StringBuffer("7/4");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2015, date.getYear());

        // Leading 0 on month
        dateStr = new StringBuffer("07/4");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2015, date.getYear());


        // Leading 0 on day
        dateStr = new StringBuffer("7/04");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 7, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2015, date.getYear());


        // Now non-digit months
        dateStr = new StringBuffer("jan 4");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2015, date.getYear());


        // non-digit months with year and comma
        dateStr = new StringBuffer("jAn 4, 2014");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2014, date.getYear());

        // non-digit months with year and without comma
        dateStr = new StringBuffer("Jan 4 2014");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2014, date.getYear());


        // full name month
        dateStr = new StringBuffer("january 4");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2015, date.getYear());


        // full name month plus year and comma
        dateStr = new StringBuffer("january 4, 2015");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2015, date.getYear());


        // full name month plus year and no comma
        dateStr = new StringBuffer("january 4 2014");
        eventList = operation.getAllEvents(dateStr);
        Assert.assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        Assert.assertEquals("Wrong month", 1, date.getMonth());
        Assert.assertEquals("Wrong date", 4, date.getDay());
        Assert.assertEquals("Wrong year", 2014, date.getYear());

    }
}