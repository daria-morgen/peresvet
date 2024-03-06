package com.silence.dto;

import jakarta.persistence.*;


@Entity
@Table(name = "Reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "authorName")
    private String authorName;

    @Column(name = "reportText")
    private String reportText;

    @Column(name = "status")
    private Status status;

    @Column(name = "dateCreation")
    private String dateCreation;

    public Report() {

    }

    public Report(String authorName, String reportText, Status status, String dateCreation) {
        this.authorName = authorName;
        this.reportText = reportText;
        this.status = status;
        this.dateCreation = dateCreation;
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

    @Override
    public String toString() {
        return "Report [id=" + id + ", authorName=" + authorName + ", desc=" + reportText + ", status=" + status + "]";
    }

}