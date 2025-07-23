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
            select
            dt.Course as Course,(sum(der.SolvedCorrectly)/count(1))*100 as AveragePerformance
            from data_exam_results der\s
            inner join data_exercises de on de.ExerciseID = der.ExerciseID\s
            inner join data_topics dt on dt.TopicID = de.TopicID\s
            where der.StudentID = :studentId
            and dt.status = 'A'
            group by dt.Course\s
            union all
            select
            'velocidadResolucion' as Tag,(sum(if(t.minutos_diff<t.DurationMinutes,1,0))/count(1))*100 as Percent
            from (
            	select
            	eh.ExamID ,\s
            	TIMESTAMPDIFF(
            	    MINUTE,         \s
            	    eh.CreationDate ,
            	    eh.FinishDate\s
            	  ) AS minutos_diff
            	  ,de.DurationMinutes\s
            	from data_student_exam_history eh\s
            	inner join data_exams de on de.ExamID = eh.ExamID\s
            	where eh.StudentID = :studentId
            	and eh.IsCompleted = '1'
            ) t\s
            union all
            select 'precisionResolucion' as Tag,avg(d.Percent) as Percent from (
            select
            	dt.Course as Tag,(sum(der.SolvedCorrectly)/count(1))*100 as Percent
            	from data_exam_results der\s
            	inner join data_exercises de on de.ExerciseID = der.ExerciseID\s
            	inner join data_topics dt on dt.TopicID = de.TopicID\s
            	where der.StudentID = :studentId
            	and dt.status = 'A'
            	group by dt.Course\s
            ) d
           """, nativeQuery = true)
    List<ICourseWeaknessDTO> findCourseWeakness(@Param("studentId") String studentId);


}
