package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.StudentExamHistoryEntity;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentExamHistoryRepository extends JpaRepository<StudentExamHistoryEntity, Integer>, CcAdminRepository<StudentExamHistoryEntity, Integer> {

    @Override
    @Query(value = """
           select count(1) from data_student_exam_history h
           where h.HistoryID = :id
           or h.StudentID like %:query%
           or h.ExamID like %:query%
           """, nativeQuery = true)
    int countByQueryText(@Param("id") String id, @Param("query") String query);

    @Override
    @Query(value = """
           select * from data_student_exam_history h
           where h.HistoryID = :id
           or h.StudentID like %:query%
           or h.ExamID like %:query%
           order by h.CreationDate desc
           limit :init, :limit
           """, nativeQuery = true)
    List<StudentExamHistoryEntity> findByQueryText(@Param("id") String id, @Param("query") String query, @Param("init") int init, @Param("limit") int limit);


    @Override
    @Query(value = """
           select count(1) from data_student_exam_history h
           where h.StudentID = :userCod
           AND ( 
                h.ExamID = :id
                or h.ExamID like %:query%
           )
           """, nativeQuery = true)
    int countByQueryTextUserCod(@Param("id") String id, @Param("query") String query, @Param("userCod") String userCod);

    @Override
    @Query(value = """
           select * from data_student_exam_history h
           where h.StudentID = :userCod
           AND (
               h.ExamID = :id
               or h.ExamID like %:query%
           )
           order by h.CreationDate desc
           limit :init, :limit
           """, nativeQuery = true)
    List<StudentExamHistoryEntity> findByQueryTextUserCod(@Param("id") String id, @Param("query") String query, @Param("userCod") String userCod, @Param("init") int init, @Param("limit") int limit);

    @Query(value = """
           select * from data_student_exam_history h
           where h.StudentID = :StudentID
           order by h.CreationDate desc
           """, nativeQuery = true)
    List<StudentExamHistoryEntity> findByStudentID(@Param("StudentID") String StudentID);

    @Query(value = """
           select hs.* from data_student_exam_history hs
           where
           hs.HistoryID in ( select max(h.HistoryID) from data_student_exam_history
           h where h.ExamID = :ExamID and h.StudentID = :StudentID)
           """, nativeQuery = true)
    StudentExamHistoryEntity findLastExamHistoryByStudentIDExamID(@Param("ExamID") String ExamID,@Param("StudentID") String StudentID);


    @Query(value = """
        select count(1)
        from data_student_exam_history h
        where h.StudentID = :studentId
        """, nativeQuery = true)
    int countByStudentId(@Param("studentId") String studentId);

    @Query(value = """
        select * 
        from data_student_exam_history h
        where h.StudentID = :studentId
        order by h.CreationDate desc
        limit 1
        """, nativeQuery = true)
    StudentExamHistoryEntity findLastByStudentId(@Param("studentId") String studentId);

    @Query(value = """
        select count(1)
        from data_student_exam_history h
        where h.StudentID = :studentId
          and h.IsCompleted = 1
        """, nativeQuery = true)
    int countCompletedByStudentId(@Param("studentId") String studentId);
}
