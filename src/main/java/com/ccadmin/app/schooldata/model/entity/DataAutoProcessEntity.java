package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "data_auto_process")
public class DataAutoProcessEntity extends AuditTableEntity implements Serializable {

    @Id
    @Column(name = "ProcessCode", length = 32)
    public String ProcessCode;

    @Column(name = "ProcessName", length = 64, nullable = false)
    public String ProcessName;

    @Column(name = "Description", length = 255)
    public String Description;

    @Column(name = "CronExpr", length = 64)
    public String CronExpr;

    @Column(name = "IntervalMin")
    public Integer IntervalMin;

    // Status, Creation/Modify vienen de AuditTableEntity

    public DataAutoProcessEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }

    @Override
    public String toString() {
        return "DataAutoProcessEntity{" +
                "ProcessCode='" + ProcessCode + '\'' +
                ", ProcessName='" + ProcessName + '\'' +
                ", CronExpr='" + CronExpr + '\'' +
                ", IntervalMin=" + IntervalMin +
                ", Status='" + Status + '\'' +
                '}';
    }
}
