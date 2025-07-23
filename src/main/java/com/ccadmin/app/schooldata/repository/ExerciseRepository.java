package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.ExerciseEntity;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Integer>, CcAdminRepository<ExerciseEntity, Integer> {

    @Override
    @Query(value = """
           select count(1) from data_exercises e
           where e.ExerciseCod = :id
           or e.ExerciseCod like %:query%
           """, nativeQuery = true)
    int countByQueryText(@Param("id") String id, @Param("query") String query);

    @Override
    @Query(value = """
           select * from data_exercises e
           where e.ExerciseCod = :id
           or e.ExerciseCod like %:query%
           order by e.CreationDate desc
           limit :init, :limit
           """, nativeQuery = true)
    List<ExerciseEntity> findByQueryText(@Param("id") String id, @Param("query") String query, @Param("init") int init, @Param("limit") int limit);

    @Query(value = """
           select * from data_exercises e
           where e.TopicID = :topicID and e.Status = 'A'
           """, nativeQuery = true)
    List<ExerciseEntity> findByTopicID(@Param("topicID") int topicID);

    @Query(value = """
            CALL db_sys_expert.get_cod_seq_lib(2)
            """,nativeQuery = true)
    public String getExerciseCod();
}