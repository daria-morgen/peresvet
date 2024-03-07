package com.silence.mars.repository;

import com.silence.dto.Report;
import com.silence.dto.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByStatus(Status status);

    List<Report> findFirst10ByStatus(Status status);

    List<Report> findByAuthorNameContaining(String authorName);
}