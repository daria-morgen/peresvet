package com.silence.earth.service;


import com.silence.earth.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Emulator {
    Logger LOG = LoggerFactory.getLogger(Emulator.class);

    private final Config config;

    public Emulator(Config config) {
        this.config = config;
    }
    //Писать в очередь сообщения
    //каждое сообщение задерживать на величину из файла


}
