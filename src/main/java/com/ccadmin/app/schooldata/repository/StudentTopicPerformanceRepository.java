package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.StudentTopicPerformanceEntity;
import com.ccadmin.app.schooldata.model.entity.id.StudentTopicPerformanceId;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTopicPerformanceRepository extends JpaRepository<StudentTopicPerformanceEntity, StudentTopicPerformanceId>, CcAdminRepository<StudentTopicPerformanceEntity, StudentTopicPerformanceId> {

    @Override
    @Query(value = """
           select count(1) from student_topic_performance stp
           where stp.StudentID = :id
           or stp.PerformanceClassification like %:query%
           """, nativeQuery = true)
    int countByQueryText(@Param("id") String id, @Param("query") String query);

    @Override
    @Query(value = """
           select * from student_topic_performance stp
           where stp.StudentID = :id
           or stp.PerformanceClassification like %:query%
           order by stp.StudentID
           limit :init, :limit
           """, nativeQuery = true)
    List<StudentTopicPerformanceEntity> findByQueryText(@Param("id") String id, @Param("query") String query, @Param("init") int init, @Param("limit") int limit);

    @Override
    @Query(value = """
           select count(1) from student_topic_performance stp
           where stp.StudentID = :userCod
           and :query = :query
           and :id = :id
           """, nativeQuery = true)
    int countByQueryTextUserCod(@Param("id") String id, @Param("query") String query, @Param("userCod") String userCod);

    @Override
    @Query(value = """
           select * from student_topic_performance stp
           where stp.StudentID = :userCod
           and :query = :query
           and :id = :id
           order by stp.StudentID
           limit :init, :limit
           """, nativeQuery = true)
    List<StudentTopicPerformanceEntity> findByQueryTextUserCod(@Param("id") String id, @Param("query") String query, @Param("userCod") String userCod, @Param("init") int init, @Param("limit") int limit);

    @Query(value = """
       select * from student_topic_performance stp
       where stp.StudentID = :studentId
       """, nativeQuery = true)
    List<StudentTopicPerformanceEntity> findByStudentID(@Param("studentId") String studentId);
}
