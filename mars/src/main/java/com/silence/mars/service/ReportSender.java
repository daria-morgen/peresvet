package com.silence.mars.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silence.dto.Report;
import com.silence.dto.Status;
import com.silence.mars.repository.ReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.*;


@Service
@EnableScheduling
public class ReportSender {

    private final Logger LOG = LoggerFactory.getLogger(ReportSender.class);

    private final ReportRepository reportRepository;
    private final DateFormatter dateFormatter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ExecutorService executorService = Executors.newFixedThreadPool(20);
    private ConcurrentMap<Long, Callable<String>> reportConcurrentMap = new ConcurrentHashMap<>();

    List<Callable<String>> callableTasks = new ArrayList<>();

    public ReportSender(ReportRepository reportRepository, DateFormatter dateFormatter) {
        this.reportRepository = reportRepository;
        this.dateFormatter = dateFormatter;
    }

    @Scheduled(fixedDelay = 6000)
    public void startSendReportsCreated() throws JsonProcessingException {
        LOG.info("startSendReportsCreated started.");
        final var reportsForCreated = reportRepository.findFirst10ByStatus(Status.CREATED);

        LOG.info("reportsForRetry size: " + reportsForCreated.size());
        for (Report report : reportsForCreated) {

            if (!reportConcurrentMap.containsKey(report.getId())) {
                Callable<String> callableTask = () -> {
                    createReports(report);
                    return "Task's execution";
                };

                reportConcurrentMap.put(report.getId(), callableTask);
                callableTasks.add(callableTask);

                Future<String> future =
                        executorService.submit(callableTask);

                if (future.isDone()) {
                    reportConcurrentMap.remove(report.getId());
                }
            }

        }
    }

    private void createReports(Report report) throws JsonProcessingException {

        final var restTemplate = new RestTemplate();


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
        String personResultAsJsonStr = null;
        try {
            personResultAsJsonStr =
                    restTemplate.postForObject("http://localhost:8084/api/reports", request, String.class);

            if (objectMapper.readValue(personResultAsJsonStr, Report.class).getStatus().equals(Status.SUCCESS)) {
                report.setStatus(Status.SUCCESS);
                reportRepository.save(report);
            }
        } catch (Exception e) {
            if (personResultAsJsonStr != null) {
                if (!objectMapper.readValue(personResultAsJsonStr, Report.class).getStatus().equals(Status.SUCCESS)) {
                    report.setStatus(Status.ERROR);
                    reportRepository.save(report);
                }
            }
            report.setStatus(Status.ERROR);
            reportRepository.save(report);
        }

    }


    @Scheduled(fixedDelay = 15000)
    public void sendReportsRetry() {
        LOG.info("sendReportsRetry started.");

        List<Report> reportsForRetry = new ArrayList<>();
//         reportsForRetry = reportRepository.findFirst10ByStatus(Status.SENDING);

        if (reportsForRetry.isEmpty()) {
            reportsForRetry = reportRepository.findFirst10ByStatus(Status.READY_TO_SEND);
        }

        if (reportsForRetry.isEmpty()) {
            reportsForRetry = reportRepository.findFirst10ByStatus(Status.ERROR);
        }
        for (Report report : reportsForRetry) {
            if (!reportConcurrentMap.containsKey(report.getId())) {

                Callable<String> callableTask = () -> {
                    retryReports(report);
                    return "Task's execution";
                };

                reportConcurrentMap.put(report.getId(), callableTask);
                callableTasks.add(callableTask);

                Future<String> future =
                        executorService.submit(callableTask);

                if (future.isDone()) {
                    reportConcurrentMap.remove(report.getId());
                }
            }
        }

    }

    private void retryReports(Report report) throws JsonProcessingException {
        final var restTemplate = new RestTemplate();

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
        String personResultAsJsonStr = null;

        try {
            personResultAsJsonStr =
                    restTemplate.postForObject("http://localhost:8084/api/reports", request, String.class);

            if (objectMapper.readValue(personResultAsJsonStr, Report.class).getStatus().equals(Status.SUCCESS)) {
                report.setStatus(Status.SUCCESS);
                reportRepository.save(report);
            }
        } catch (Exception e) {
            if (personResultAsJsonStr != null) {
                if (!objectMapper.readValue(personResultAsJsonStr, Report.class).getStatus().equals(Status.SUCCESS)) {
                    report.setStatus(Status.ERROR);
                    reportRepository.save(report);
                }
            }
            report.setStatus(Status.ERROR);
            reportRepository.save(report);
        }
    }
}
