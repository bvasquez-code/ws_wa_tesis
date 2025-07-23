package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "data_student_exam_history")
public class StudentExamHistoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int HistoryID;

    @Column(nullable = false, length = 10)
    public String StudentID;

    @Column(nullable = false, length = 10)
    public String ExamID;

    @Column(nullable = false)
    public int AttemptNumber;

    @Column(nullable = false)
    public int IsCompleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date StartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    public Date FinishDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    public Date CreationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date ModifyDate;

    public String JsonDataExam;

    public StudentExamHistoryEntity() {
        Date now = new Date();
        this.CreationDate = now;
        this.ModifyDate = now;
    }

    /**
     * Valida los campos obligatorios del historial.
     */
    public StudentExamHistoryEntity validate() {
        if (StudentID == null || StudentID.isEmpty()) {
            throw new EntityBuildException("StudentID no puede ser nulo o vacío");
        }
        if (ExamID == null || ExamID.isEmpty()) {
            throw new EntityBuildException("ExamID no puede ser nulo o vacío");
        }
        if (AttemptNumber <= 0) {
            throw new EntityBuildException("AttemptNumber debe ser mayor a 0");
        }
        if (StartDate == null) {
            throw new EntityBuildException("StartDate es obligatorio");
        }
        return this;
    }

    /**
     * Actualiza la fecha de modificación para simular la sesión.
     */
    public StudentExamHistoryEntity session(String userCod) {
        this.ModifyDate = new Date();
        return this;
    }

    @Override
    public String toString() {
        return "StudentExamHistoryEntity{" +
                "HistoryID=" + HistoryID +
                ", StudentID='" + StudentID + '\'' +
                ", ExamID='" + ExamID + '\'' +
                ", AttemptNumber=" + AttemptNumber +
                ", IsCompleted=" + IsCompleted +
                ", StartDate=" + StartDate +
                ", FinishDate=" + FinishDate +
                ", CreationDate=" + CreationDate +
                ", ModifyDate=" + ModifyDate +
                '}';
    }
}