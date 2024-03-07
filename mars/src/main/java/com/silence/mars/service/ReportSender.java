package com.silence.mars.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silence.dto.Report;
import com.silence.dto.Status;
import com.silence.mars.repository.ReportRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


@Service
@EnableScheduling
public class ReportSender {

    private final ReportRepository reportRepository;
    private final DateFormatter dateFormatter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReportSender(ReportRepository reportRepository, DateFormatter dateFormatter) {
        this.reportRepository = reportRepository;
        this.dateFormatter = dateFormatter;
    }

    @Scheduled(fixedDelay = 6000)
    public void sendReports() throws JsonProcessingException {
        final var reportsByStatusSUCCESS = reportRepository.findByStatus(Status.CREATED);

        final var restTemplate = new RestTemplate();

        for (Report report : reportsByStatusSUCCESS) {
            report.setStatus(Status.READY_TO_SEND);
            reportRepository.save(report);

            final var httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            report.setStatus(Status.SENDING);

            GregorianCalendar gcal = new GregorianCalendar();
            report.setDateSending(dateFormatter.toStringFormat(gcal));

            reportRepository.save(report);

            HttpEntity<String> request =
                    new HttpEntity<String>(objectMapper.writeValueAsString(report).toString(), httpHeaders);

            String personResultAsJsonStr =
                    restTemplate.postForObject("http://localhost:8084/api/reports", request, String.class);


            if (objectMapper.readValue(personResultAsJsonStr, Report.class).getStatus().equals(Status.SUCCESS)) {
                report.setStatus(Status.SUCCESS);
                reportRepository.save(report);
            }
        }

    }
}
