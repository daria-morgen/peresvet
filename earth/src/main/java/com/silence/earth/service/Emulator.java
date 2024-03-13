package com.silence.earth.service;


import com.silence.dto.LostEarthConnection;
import com.silence.dto.Report;
import com.silence.earth.config.Config;
import com.silence.earth.config.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

@Service
public class Emulator {
    Logger LOG = LoggerFactory.getLogger(Emulator.class);

    Set<Thread> syncSet = Collections.synchronizedSet(new HashSet<>());

    private final Config config;
    private final DateFormatter dateFormatter;

    public Emulator(Config config, DateFormatter dateFormatter) {
        this.config = config;
        this.dateFormatter = dateFormatter;
    }

    //Писать в очередь сообщения
    //каждое сообщение задерживать на величину из файла
    public synchronized void emulateReportDelay(Report report) throws ParseException, InterruptedException, LostEarthConnection {

        //получаем дату отправки сообщения на землю
        final var dateSending = dateFormatter.toCalendarFormat(report.getDateSending());
        //проверяем попали ли мы в какой нибудь из заданных периодов
        final var activePeriod = getActivePeriod(dateSending);
        LOG.info("activePeriod: "+activePeriod);
        if (!Objects.isNull(activePeriod)) {
            //создаем случайный размер сообщения в мегабитах в пределах 100мбит
            double reportSize = report.getReportSize();
            double currentSpeed = activePeriod.getSpeed();

            int milliToSleep;

            //Если пропускная скорость больше файла, тогда удерживаем поток на скорость пропуска
            if (reportSize < currentSpeed) {
                milliToSleep = 1000;
            } else {//Иначе, получаем

                milliToSleep = (int) Math.ceil((reportSize * 1000) / currentSpeed);
            }
            LOG.info("Tread starting report: " + report.getId() + ", reportSize: " + reportSize + ", milliToSleep: " + milliToSleep);

            Thread.sleep(milliToSleep);

            if (Objects.isNull(getActivePeriod(dateSending))) {
                throw new LostEarthConnection("LostEarthConnection" + report.toString() + ", reportSize: " + reportSize);
            }
        }else {
            throw new LostEarthConnection("LostEarthConnection" + report);
        }
    }

    private Period getActivePeriod(Calendar dateSending) {
        //Получаем текущую дату
        Calendar currentDate = new GregorianCalendar();

        Calendar from;
        Calendar to;
        Period activePeriod = null;
        for (Period period : config.periods) {
            try {
                from = dateFormatter.toCalendarFormat(period.getFrom());
                to = dateFormatter.toCalendarFormat(period.getTo());
                //если сейчас земля доступна
                if (currentDate.after(from) && currentDate.before(to)){
                    activePeriod = period;
                }
            } catch (ParseException e) {
                LOG.error(e.getMessage());
            }
        }
        return activePeriod;
    }


}
