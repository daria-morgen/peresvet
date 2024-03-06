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


@Service
@EnableScheduling
public class ReportSender {

    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReportSender(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Scheduled(fixedDelay = 1000)
    public void sendReports() throws JsonProcessingException {
        final var reportsByStatusSUCCESS = reportRepository.findByStatus(Status.CREATED);

        final var restTemplate = new RestTemplate();

        for (Report report : reportsByStatusSUCCESS) {
            report.setStatus(Status.READY_TO_SEND);
            reportRepository.save(report);

            final var httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request =
                    new HttpEntity<String>(objectMapper.writeValueAsString(report).toString(), httpHeaders);
            String personResultAsJsonStr =
                    restTemplate.postForObject("http://localhost:8084/api/reports", request, String.class);

            report.setStatus(Status.SENDING);
            reportRepository.save(report);



            if (objectMapper.readValue(personResultAsJsonStr, Report.class).getStatus().equals(Status.SUCCESS)) {
                report.setStatus(Status.SUCCESS);
                reportRepository.save(report);
            }
        }

    }
}
