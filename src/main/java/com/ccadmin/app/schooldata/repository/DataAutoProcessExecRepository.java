package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.DataAutoProcessExecEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface DataAutoProcessExecRepository extends JpaRepository<DataAutoProcessExecEntity, Long> {

    @Query(value = """
            select * from data_auto_process_exec e
            where e.ProcessCode = :processCode
            order by e.StartDate desc
            limit 1
            """, nativeQuery = true)
    DataAutoProcessExecEntity findLastExec(@Param("processCode") String processCode);

    @Query(value = """
            select count(1) from data_auto_process_exec e
            where e.ProcessCode = :processCode
              and e.StartDate between :fromDate and :toDate
            """, nativeQuery = true)
    int countExecutionsInWindow(@Param("processCode") String processCode,
                                @Param("fromDate") Date fromDate,
                                @Param("toDate") Date toDate);
}
