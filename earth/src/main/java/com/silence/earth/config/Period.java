package com.silence.earth.config;

import lombok.Data;

@Data
public class Period {
    /**
     * Десятичное число (разделитель — точка), показывающее
     * среднюю скорость соединения с Землёй в заданный период в мегабитах
     * в секунду
     */
    private double speed;
    /**
     * Дата и время в формате yyyy-MM-dd HH:mm:ss, показывающая
     * начало периода, в течение которого связь будет доступна
     */
    private String from;
    /**
     * дата и время в формате yyyy-MM-dd HH:mm:ss, показывающая
     * конец периода, в течение которого связь будет доступна
     */
    private String to;
}
