package com.silence.mars.config;

import com.silence.dto.Report;
import com.silence.dto.Status;
import com.silence.mars.repository.ReportRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Configuration
public class Config {

    final
    ReportRepository reportRepository;

    public Config(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Bean
    public String initialData() {
        GregorianCalendar gcal = new GregorianCalendar();
        SimpleDateFormat formattedDate
                = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // Use format() method to change the format
        // Using getTime() method,
        // this required date is passed
        // to format() method
        String dateFormatted
                = formattedDate.format(
                gcal.getTime());

        gcal.add(Calendar.MINUTE,1);

        Report[] reports = new Report[]{
                //"Wed Mar 06 2024 21:32:37 GMT+0300 (Москва, стандартное время)"
                //"Wed Mar 06 23:44:22 MSK 2024"
                new Report("Olga", "Отчет о результатах исследования марсианской почвы. Проведены анализы, выявлены интересные особенности и состав почвы на Марсе. Полученные данные могут быть ключевыми для будущих экспедиций.", Status.CREATED, dateFormatted, null),
                new Report("Alex", "Анализ атмосферы Марса и предварительные выводы. Изучены химические компоненты атмосферы, а также выявлены особенности климата на планете. Результаты представляют интерес для дальнейших исследований.", Status.CREATED,dateFormatted, null),
                new Report("Elena", "Сравнение климатических условий на Земле и Марсе. Проанализированы различия в климате, температуре и атмосферном давлении между двумя планетами. Найдены сходства и различия.", Status.CREATED,dateFormatted, null),
                new Report("Ivan", "Определение возможности посадки на Марсе. Исследованы потенциальные зоны для посадки космических кораблей на Марсе. Предложены оптимальные места для будущих миссий.", Status.CREATED,dateFormatted, null),
                new Report("Sophia", "Отчет о работе системы жизнеобеспечения на Марсе. Протестированы и анализированы системы для обеспечения жизни и безопасности на Марсе. Результаты подтверждают работоспособность систем.", Status.CREATED,dateFormatted, null),
                new Report("Daniel", "Анализ ресурсов для будущей колонии на Марсе. Оценены наличие воды, минералов и других ресурсов, необходимых для обеспечения жизнедеятельности на марсианской поверхности.", Status.CREATED,dateFormatted, null),
                new Report("Mia", "Оценка экологических аспектов исследования Марса. Изучены воздействие человеческой деятельности на экосистему Марса и возможные меры по ее сохранению.", Status.CREATED,dateFormatted, null),
                new Report("William", "Подготовка к запуску ракеты на Марс. Проанализированы технические характеристики и подготовка к запуску ракеты. Все системы функционируют в норме.", Status.CREATED,dateFormatted, null),
                new Report("Ava", "Обзор оборудования для марсианской базы. Проведен обзор необходимого оборудования для создания базы на Марсе. Предложены оптимальные варианты исходя из современных технологий.", Status.CREATED,dateFormatted, null),
                new Report("James", "Анализ радиационной обстановки на Марсе. Изучены параметры радиации на поверхности Марса и разработаны меры по защите от воздействия радиации во время миссий.", Status.CREATED,dateFormatted, null),
                new Report("Emma", "План экспериментов на марсианской поверхности. Разработан план научных экспериментов и исследований для проведения на марсианской поверхности. Подробные инструкции и методики.", Status.CREATED,dateFormatted, null),
                new Report("Benjamin", "Оценка потенциала для сельского хозяйства на Марсе. Исследованы возможности создания сельского хозяйства на Марсе, включая анализ почвы и воздуха.", Status.CREATED,dateFormatted, null),
                new Report("Aria", "Исследование марсианских горных образований. Проведены исследования структуры и состава марсианских гор. Результаты представляют интерес для геологической науки.", Status.CREATED,dateFormatted, null),
                new Report("Henry", "Проект строительства жилых модулей на Марсе", Status.CREATED,dateFormatted, null),
                new Report("Lily", "Разработка программы обучения для будущих марсиан", Status.CREATED,dateFormatted, null)
        };

        reportRepository.saveAll(Arrays.asList(reports));

        return Strings.EMPTY;
    }
}
