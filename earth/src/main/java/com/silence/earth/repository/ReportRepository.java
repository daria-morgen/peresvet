package com.silence.earth.repository;

import com.silence.dto.Report;
import com.silence.dto.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByStatus(Status status);

    List<Report> findByAuthorNameContaining(String authorName);
}