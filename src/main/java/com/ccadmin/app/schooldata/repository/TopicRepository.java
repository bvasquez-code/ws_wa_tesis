package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.TopicEntity;
import com.ccadmin.app.schooldata.model.idto.ICourseWeaknessDTO;
import com.ccadmin.app.schooldata.model.idto.IStudentExamAttemptInfoDto;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<TopicEntity, Integer>, CcAdminRepository<TopicEntity, Integer> {

    @Override
    @Query(value = """
           select count(1) from data_topics t
           where (t.TopicCod = :id
           or t.Name like %:query%
           or t.Course like %:query%) and t.status = 'A'
           """, nativeQuery = true)
    int countByQueryText(@Param("id") String id, @Param("query") String query);

    @Override
    @Query(value = """
           select * from data_topics t
           where (t.TopicCod = :id
           or t.Name like %:query%
           or t.Course like %:query%) and t.status = 'A'
           order by t.CreationDate desc
           limit :init, :limit
           """, nativeQuery = true)
    List<TopicEntity> findByQueryText(@Param("id") String id, @Param("query") String query, @Param("init") int init, @Param("limit") int limit);

    @Query(value = """
           select dt.Course as course, dt.TopicID as topicID, dt.Name as topicName, 
                  count(rs.ResultID) as numExercisesAttempted, 
                  sum(rs.SolvedCorrectly) as numSolvedCorrectly
           from data_topics dt 
           inner join data_exercises de on de.TopicID = dt.TopicID 
           left join (select * from data_exam_results er where er.StudentID = :studentId) rs on rs.ExerciseID = de.ExerciseID
           where dt.status = 'A'
           group by dt.Course, dt.TopicID, dt.Name
           """, nativeQuery = true)
    List<IStudentExamAttemptInfoDto> findStudentExamAttemptInfo(@Param("studentId") String studentId);

    @Query(value = """
            WITH
               per_course AS (
                 SELECT
                   dt.Course AS Tag,
                   SUM(der.SolvedCorrectly) * 100.0 / COUNT(*) AS Percent
                 FROM data_exam_results der
                 JOIN data_exercises    de ON de.ExerciseID = der.ExerciseID
                 JOIN data_topics       dt ON dt.TopicID    = de.TopicID
                 WHERE der.StudentID = :studentId
                   AND dt.Status     = 'A'
                 GROUP BY dt.Course
               ),
               speed AS (
                 SELECT
                   'VelocidadResolucion' AS Tag,
                   SUM(
                     CASE
                       WHEN TIMESTAMPDIFF(
                              MINUTE,
                              eh.CreationDate,
                              eh.FinishDate
                            ) < de.DurationMinutes
                       THEN 1 ELSE 0
                     END
                   ) * 100.0 / COUNT(*) AS Percent
                 FROM data_student_exam_history eh
                 JOIN data_exams de ON de.ExamID = eh.ExamID
                 WHERE eh.StudentID   = :studentId
                   AND eh.IsCompleted = 1
               ),
               precision_res AS (
                 SELECT
                   'PrecisionResolucion' AS Tag,
                   AVG(Percent) AS Percent
                 FROM per_course
               ),
               consistency AS (
                 SELECT
                   'ConsistenciaResolucion' AS Tag,
                   STDDEV_POP(session_percent) AS Percent
                 FROM (
                   SELECT
                     eh.HistoryID,
                     SUM(der.SolvedCorrectly) * 100.0 / COUNT(*) AS session_percent
                   FROM data_exam_results          der
                   JOIN data_student_exam_history eh  ON eh.ExamID = der.ExamID
                   WHERE der.StudentID = :studentId
                     AND eh.IsCompleted = 1
                   GROUP BY eh.HistoryID
                 ) t
               ),
               coverage AS (
                 SELECT
                   'CoberturaTematica' AS Tag,
                   COUNT(DISTINCT dt.TopicID) * 100.0
                     / (SELECT COUNT(*) FROM data_topics WHERE Status = 'A') AS Percent
                 FROM data_exam_results der
                 JOIN data_exercises    de ON de.ExerciseID = der.ExerciseID
                 JOIN data_topics       dt ON dt.TopicID    = de.TopicID
                 WHERE der.StudentID = :studentId
                   AND dt.Status     = 'A'
               ),
               improvement AS (
                 SELECT
                   'TasaDeMejora' AS Tag,
                   MAX(session_percent) - MIN(session_percent) AS Percent
                 FROM (
                   SELECT
                     eh.HistoryID,
                     SUM(der.SolvedCorrectly) * 100.0 / COUNT(*) AS session_percent
                   FROM data_exam_results          der
                   JOIN data_student_exam_history eh  ON eh.ExamID = der.ExamID
                   WHERE der.StudentID = :studentId
                     AND eh.IsCompleted = 1
                   GROUP BY eh.HistoryID
                 ) t
               ),
               retention AS (
                 SELECT
                   'RetencionDeConocimiento' AS Tag,
                   SUM(
                     CASE
                       WHEN later.SolvedCorrectly = 1 THEN 1 ELSE 0
                     END
                   ) * 100.0 / COUNT(*) AS Percent
                 FROM data_exam_results early
                 JOIN data_exam_results later
                   ON early.StudentID  = later.StudentID
                  AND early.ExerciseID = later.ExerciseID
                  AND later.CreationDate > early.CreationDate + INTERVAL 7 DAY
                 WHERE early.StudentID = :studentId
               )
              SELECT Tag as Course, IFNULL(Percent,0) as AveragePerformance, 'Course' as Description,'curso' as Type FROM per_course
              UNION ALL
              SELECT Tag as Course, IFNULL(Percent,0) as AveragePerformance, 'Velocidad resolución' as Description,'habilidad' as Type FROM speed
              UNION ALL
              SELECT Tag as Course, IFNULL(Percent,0) as AveragePerformance, 'Precision resolución' as Description,'habilidad' as Type FROM precision_res
              UNION ALL
              SELECT Tag as Course, IFNULL(Percent,0) as AveragePerformance, 'Consistencia resolución' as Description,'habilidad' as Type FROM consistency
              UNION ALL
              SELECT Tag as Course, IFNULL(Percent,0) as AveragePerformance, 'Cobertura temática' as Description,'habilidad' as Type FROM coverage
              UNION ALL
              SELECT Tag as Course, IFNULL(Percent,0) as AveragePerformance, 'Tasa de mejora' as Description,'habilidad' as Type FROM improvement
              UNION ALL
              SELECT Tag as Course, IFNULL(Percent,0) as AveragePerformance, 'Retención de conocimiento' as Description,'habilidad' as Type FROM retention
           """, nativeQuery = true)
    List<ICourseWeaknessDTO> findCourseWeakness(@Param("studentId") String studentId);


}
