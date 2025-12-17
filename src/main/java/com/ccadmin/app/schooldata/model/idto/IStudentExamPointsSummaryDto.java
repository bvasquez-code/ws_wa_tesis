package com.ccadmin.app.schooldata.model.idto;

import java.util.Date;

public interface IStudentExamPointsSummaryDto {
    String getExamID();
    Date getCreationDate();
    Integer getPointsOnExam();
}
