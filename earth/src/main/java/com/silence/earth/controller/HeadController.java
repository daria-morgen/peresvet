package com.silence.earth.controller;

import com.silence.dto.LostEarthConnection;
import com.silence.dto.Report;
import com.silence.dto.Status;
import com.silence.earth.repository.ReportRepository;
import com.silence.earth.service.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOG = LoggerFactory.getLogger(HeadController.class);


    private final ReportRepository reportRepository;

    private final Emulator emulator;

    public HeadController(ReportRepository reportRepository, Emulator emulator) {
        this.reportRepository = reportRepository;
        this.emulator = emulator;
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports(@RequestParam(required = false) String authorName) {
        try {
            List<Report> reports = new ArrayList<Report>();
            final var all = reportRepository.findAll();
            all.forEach(e -> System.out.println(e.toString()));
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
            LOG.info("createReport: "+report.toString());
            emulator.emulateReportDelay(report);

            Report _Report = reportRepository
                    .save(new Report(
                            report.getAuthorName(),
                            report.getReportText(),
                            report.getStatus(),
                            report.getDateCreation(),
                            report.getDateSending(),
                            report.getReportSize())
                    );

            _Report.setStatus(Status.SUCCESS);
            reportRepository.save(_Report);
            return new ResponseEntity<>(_Report, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (LostEarthConnection lostEarthConnection) {
            lostEarthConnection.printStackTrace();
            return new ResponseEntity<>(report, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reports/status/success")
    public ResponseEntity<List<Report>> findByStatusSUCCESS() {
        try {
            List<Report> reports = reportRepository.findByStatus(Status.SUCCESS);

            if (reports.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}