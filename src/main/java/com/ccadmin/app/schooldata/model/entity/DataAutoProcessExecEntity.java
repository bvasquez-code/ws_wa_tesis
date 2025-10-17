package com.ccadmin.app.schooldata.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "data_auto_process_exec")
public class DataAutoProcessExecEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ExecID")
    public Long ExecID;

    @Column(name = "ProcessCode", length = 32, nullable = false)
    public String ProcessCode;

    @Column(name = "TriggeredBy", length = 1, nullable = false)
    public String TriggeredBy; // A/M

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "StartDate", nullable = false)
    public Date StartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EndDate")
    public Date EndDate;

    @Column(name = "DurationMs")
    public Long DurationMs;

    @Column(name = "RunStatus", length = 1, nullable = false)
    public String RunStatus; // R/S/E

    @Column(name = "OutputFileSizeBytes")
    public Long OutputFileSizeBytes;

    @Column(name = "RecordsProcessed")
    public Integer RecordsProcessed;

    @Column(name = "Message", length = 500)
    public String Message;

    @Lob
    @Column(name = "ErrorStack")
    public String ErrorStack;

    @Column(name = "Hostname", length = 64)
    public String Hostname;
}
