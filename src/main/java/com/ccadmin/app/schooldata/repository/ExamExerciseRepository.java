package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.ExamExerciseEntity;
import com.ccadmin.app.schooldata.model.entity.id.ExamExerciseID;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExamExerciseRepository extends JpaRepository<ExamExerciseEntity, ExamExerciseID>, CcAdminRepository<ExamExerciseEntity, ExamExerciseID> {

    @Override
    @Query(value = """
           select count(1) from data_exam_exercises ee
           where ee.ExamID = :id
           or ee.DifficultyLevel like %:query%
           """, nativeQuery = true)
    int countByQueryText(@Param("id") String id, @Param("query") String query);

    @Override
    @Query(value = """
           select * from data_exam_exercises ee
           where ee.ExamID = :id
           or ee.DifficultyLevel like %:query%
           order by ee.CreationDate desc
           limit :init, :limit
           """, nativeQuery = true)
    List<ExamExerciseEntity> findByQueryText(@Param("id") String id, @Param("query") String query, @Param("init") int init, @Param("limit") int limit);

    @Query(value = """
           select * from data_exam_exercises ee
           where ee.ExamID = :examID and ee.Status = 'A'
           """, nativeQuery = true)
    List<ExamExerciseEntity> findByExamID(@Param("examID") String examID);
}