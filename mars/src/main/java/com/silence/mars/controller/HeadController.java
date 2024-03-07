package com.silence.mars.controller;

import com.silence.dto.Report;
import com.silence.dto.Status;
import com.silence.mars.repository.ReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class HeadController {

    private final ReportRepository reportRepository;

    public HeadController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports(@RequestParam(required = false) String authorName) {
        try {
            List<Report> reports = new ArrayList<Report>();

            if (authorName == null)
                reports.addAll(reportRepository.findAll());
            else
                reports.addAll(reportRepository.findByAuthorNameContaining(authorName));

            if (reports.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable("id") long id) {
        Optional<Report> reportData = reportRepository.findById(id);

        if (reportData.isPresent()) {
            return new ResponseEntity<>(reportData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reports")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        try {
            Report _Report = reportRepository
                    .save(new Report(
                            report.getAuthorName(),
                            report.getReportText(),
                            Status.CREATED,
                            report.getDateCreation(),
                            report.getDateSending(),
                            report.getReportSize())
                    );
            return new ResponseEntity<>(_Report, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable("id") long id, @RequestBody Report report) {
        Optional<Report> ReportData = reportRepository.findById(id);

        if (ReportData.isPresent()) {
            Report _Report = ReportData.get();
            _Report.setAuthorName(report.getAuthorName());
            _Report.setReportText(report.getReportText());
            _Report.setStatus(report.getStatus());
            return new ResponseEntity<>(reportRepository.save(_Report), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<HttpStatus> deleteReport(@PathVariable("id") long id) {
        try {
            reportRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/reports")
    public ResponseEntity<HttpStatus> deleteAllReports() {
        try {
            reportRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/reports/status/success")
    public ResponseEntity<List<Report>> findByStatusSUCCESS() {
        try {
            List<Report> Reports = reportRepository.findByStatus(Status.SUCCESS);

            if (Reports.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(Reports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}