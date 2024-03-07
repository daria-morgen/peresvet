package com.silence.earth.service;


import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class DateFormatter {

    private final SimpleDateFormat formattedDate
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //2024-03-08 21:19:10

    public String toStringFormat(Calendar calendar) {
        return formattedDate.format(calendar.getTime());
    }

    public Calendar toCalendarFormat(String dateString) throws ParseException {

        Date date = formattedDate.parse(dateString);

        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        return cal;
    }
}
