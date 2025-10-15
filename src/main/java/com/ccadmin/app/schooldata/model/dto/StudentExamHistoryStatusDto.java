package com.ccadmin.app.schooldata.model.dto;

public class StudentExamHistoryStatusDto {

    public String StudentID;
    public boolean HasHistory;
    public Integer TotalAttempts;
    public Integer CompletedAttempts;
    public Integer InProgressAttempts;
    public Integer LastHistoryID; // último intento
    public String LastExamID;
}
