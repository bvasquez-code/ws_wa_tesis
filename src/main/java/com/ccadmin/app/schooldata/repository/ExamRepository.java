package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.ExamEntity;
import com.ccadmin.app.schooldata.model.idto.IExamResultStatsDto;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExamRepository extends JpaRepository<ExamEntity, String>, CcAdminRepository<ExamEntity, String> {

    @Override
    @Query(value = """
            select count(1) from data_exams e
            where e.ExamID = :id
            or e.ExamName like %:query%
            or e.Subject like %:query%
            """, nativeQuery = true)
    int countByQueryText(String id, String query);

    @Override
    @Query(value = """
            select * from data_exams e
            where e.ExamID = :id
            or e.ExamName like %:query%
            or e.Subject like %:query%
            order by e.CreationDate desc
            limit :init, :limit
            """, nativeQuery = true)
    List<ExamEntity> findByQueryText(@Param("id") String id, @Param("query") String query, @Param("init") int init, @Param("limit") int limit);

    @Query(value = """
            select * from data_exams e
            where e.Subject = :subject and e.Status = 'A'
            """, nativeQuery = true)
    List<ExamEntity> findBySubject(@Param("subject") String subject);

    @Query(value = "SELECT COUNT(*) as totalAttempts, " +
            "SUM(CASE WHEN SolvedCorrectly = 0 THEN 1 ELSE 0 END) as failedAttempts " +
            "FROM data_exam_results " +
            "WHERE StudentID = :studentId AND TopicID = :topicId", nativeQuery = true)
    IExamResultStatsDto getStatsByStudentAndTopic(@Param("studentId") String studentId, @Param("topicId") int topicId);


}