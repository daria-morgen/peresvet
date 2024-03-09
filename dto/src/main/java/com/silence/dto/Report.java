package com.silence.dto;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "Reports")
public class Report {

    /**
     * Уникальный идентификатор очета в бд.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Имя автора
     */
    @Column(name = "authorName")
    private String authorName;

    /**
     * Текст отчета
     */
    @Column(name = "reportText")
    private String reportText;

    /**
     * Статус
     */
    @Column(name = "status")
    private Status status;

    /**
     * Дата создания
     */
    @Column(name = "dateCreation")
    private String dateCreation;

    /**
     * Дата отправки
     */
    @Column(name = "dateSending")
    private String dateSending;

    /**
     * Размер отчета
     */
    @Column(name = "reportSize")
    private double reportSize;

    public Report() {

    }

    public Report(String authorName,
                  String reportText,
                  Status status,
                  String dateCreation,
                  String dateSending,
                  double reportSize) {
        this.authorName = authorName;
        this.reportText = reportText;
        this.status = status;
        this.dateCreation = dateCreation;
        this.dateSending = dateSending;
        this.reportSize = reportSize;
    }

    public long getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDateSending() {
        return dateSending;
    }

    public void setDateSending(String dateSending) {
        this.dateSending = dateSending;
    }

    public double getReportSize() {
        return reportSize;
    }

    public void setReportSize(double reportSize) {
        this.reportSize = reportSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id == report.id && Double.compare(report.reportSize, reportSize) == 0 && Objects.equals(authorName, report.authorName) && Objects.equals(reportText, report.reportText) && status == report.status && Objects.equals(dateCreation, report.dateCreation) && Objects.equals(dateSending, report.dateSending);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorName, reportText, status, dateCreation, dateSending, reportSize);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", authorName='" + authorName + '\'' +
                ", reportText='" + reportText + '\'' +
                ", status=" + status +
                ", dateCreation='" + dateCreation + '\'' +
                ", dateSending='" + dateSending + '\'' +
                ", reportSize=" + reportSize +
                '}';
    }
}