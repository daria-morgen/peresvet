package com.silence.mars.service;


import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class DateFormatter {

    private final SimpleDateFormat formattedDate
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String toStringFormat(Calendar calendar) {
        return formattedDate.format(calendar.getTime());
    }

    public Calendar toCalendarFormat(String dateString){
        return null;
    }
}
