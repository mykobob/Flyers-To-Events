package com.mykobob.flyerstoevents;

import junit.framework.TestCase;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class OCROperationsTest extends TestCase {

    private OCROperations operation;

    public void setUp() throws Exception {
        operation = new OCROperations();
    }

    public void testEventsWithDigitMonth() throws Exception {

        // Standard date
        String dateStr = "7/4/1996";
        List<Event> eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        GregorianCalendar date = eventList.get(0).getDate();
        assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 1996, date.get(Calendar.YEAR));

        // Leading 0 on month
        dateStr = "07/4/1996";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 1996, date.get(Calendar.YEAR));

        // Leading 0 on day
        dateStr = "7/04/1996";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 1996, date.get(Calendar.YEAR));

        // Two digit date (meaning 20s)
        // WILL BE DEPRECATED as years go on b/c using "logic" to determine which century
        dateStr = "7/4/14";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

        // Lack of a year -> means it's this year
        dateStr = "7/4";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));

        // Leading 0 on month
        dateStr = "07/4";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));


        // Leading 0 on day
        dateStr = "7/04";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 7, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));
    }

    public void testDatesWithNonDigitMonths() throws Exception {
        // Now non-digit months
        String dateStr = "jan 4";
        List<Event> eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        GregorianCalendar date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));


        // non-digit months with year and comma
        dateStr = "jAn 4, 2014";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

        // non-digit months with year and without comma
        dateStr = "Jan 4 2014";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

        // suffix with the date and abbreviated name
        dateStr = "jan 4th 2014";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

        // suffix with the date and abbreviated name with comma
        dateStr = "jan 4th, 2014";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

        // full name month
        dateStr = "january 4";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));


        // full name month plus year and comma
        dateStr = "january 4, 2015";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));


        // full name month plus year and no comma
        dateStr = "january 4 2014";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));
        
        // suffix with the date and full name
        dateStr = "january 4th 2014";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));

        // suffix with the date and full name with comma
        dateStr = "january 4th, 2014";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 1, date.get(Calendar.MONTH));
        assertEquals("Wrong date", 4, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));
    }

    public void testTimeHoursAndMinutes() throws Exception {

        String dateStr = "7:30 AM";
        List<Event> eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        GregorianCalendar date = eventList.get(0).getDate();
        assertEquals("Wrong hour", 7, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 30, date.get(Calendar.MINUTE));

        dateStr = "7:30 PM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong hour", 19, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 30, date.get(Calendar.MINUTE));

        dateStr = "7 PM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong hour", 19, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 0, date.get(Calendar.MINUTE));

        dateStr = "7 AM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong hour", 7, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 0, date.get(Calendar.MINUTE));
    }

    public void testBothTimeAndDate() throws Exception {

        // no year and all info time
        String dateStr = "4/23 7:30 PM";
        List<Event> eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        GregorianCalendar date = eventList.get(0).getDate();
        assertEquals("Wrong month", 4, date.get(Calendar.MONTH));
        assertEquals("Wrong day", 23, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));
        assertEquals("Wrong hour", 19, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 30, date.get(Calendar.MINUTE));

        // all info date all info time
        dateStr = "4/23/2016 7:30 PM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 4, date.get(Calendar.MONTH));
        assertEquals("Wrong day", 23, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2016, date.get(Calendar.YEAR));
        assertEquals("Wrong hour", 19, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 30, date.get(Calendar.MINUTE));

        // all info date (in the past) all info time
        dateStr = "4/23/2014 5:30 PM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 4, date.get(Calendar.MONTH));
        assertEquals("Wrong day", 23, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2014, date.get(Calendar.YEAR));
        assertEquals("Wrong hour", 17, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 30, date.get(Calendar.MINUTE));

        // all info date   lack minutes
        dateStr = "4/23 5 PM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 4, date.get(Calendar.MONTH));
        assertEquals("Wrong day", 23, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));
        assertEquals("Wrong hour", 17, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 0, date.get(Calendar.MINUTE));

        // word month day lack minutes
        dateStr = "feb 28th 5 PM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 2, date.get(Calendar.MONTH));
        assertEquals("Wrong day", 28, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));
        assertEquals("Wrong hour", 17, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 0, date.get(Calendar.MINUTE));

        // word month day    all info time
        dateStr = "february 28th 2:27 PM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 2, date.get(Calendar.MONTH));
        assertEquals("Wrong day", 28, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2015, date.get(Calendar.YEAR));
        assertEquals("Wrong hour", 14, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 27, date.get(Calendar.MINUTE));

        // word month day     lack minutes
        dateStr = "March 17th, 2016 12 AM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 3, date.get(Calendar.MONTH));
        assertEquals("Wrong day", 17, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2016, date.get(Calendar.YEAR));
        assertEquals("Wrong hour", 12, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 0, date.get(Calendar.MINUTE));

        // word month day     lack minutes
        dateStr = "March 17th, 2016 11 AM";
        eventList = operation.getAllEvents(dateStr);
        assertEquals("Only one event", 1, eventList.size());
        date = eventList.get(0).getDate();
        assertEquals("Wrong month", 3, date.get(Calendar.MONTH));
        assertEquals("Wrong day", 17, date.get(Calendar.DATE));
        assertEquals("Wrong year", 2016, date.get(Calendar.YEAR));
        assertEquals("Wrong hour", 11, date.get(Calendar.HOUR_OF_DAY));
        assertEquals("Wrong minutes", 0, date.get(Calendar.MINUTE));
    }

    public void testAllowedCharacters() throws Exception {
        assertEquals("Wrong regex", "[^a-zA-Z0-9\\p{Punct}]", operation.regexAllowed);
    }
}