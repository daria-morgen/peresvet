package com.silence.earth.service;


import com.silence.dto.Report;
import com.silence.earth.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Emulator {
    Logger LOG = LoggerFactory.getLogger(Emulator.class);

    Set<Thread> syncSet = Collections.synchronizedSet(new HashSet<>());

    private final Config config;

    public Emulator(Config config) {
        this.config = config;
    }
    //Писать в очередь сообщения
    //каждое сообщение задерживать на величину из файла
    public synchronized void emulateReportDelay(Report report){
        //получаем текущую дату
        Calendar calendar = new GregorianCalendar();
        //сравниваем с периодом
        //если в периоде, то выполняем задержку
        final var dateSending = report.getDateSending();

//        if(!historyDate.after(todayDate) && !futureDate.before(todayDate)) {
//                /* historyDate <= todayDate <= futureDate */
//        }
        //иначе отваливаем ошибку земля недоступна


    }

}
