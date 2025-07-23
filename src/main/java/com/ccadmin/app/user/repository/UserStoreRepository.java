package com.ccadmin.app.user.repository;

import com.ccadmin.app.user.model.entity.UserStoreEntity;
import com.ccadmin.app.user.model.entity.id.UserStoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserStoreRepository extends JpaRepository<UserStoreEntity, UserStoreId> {


    @Query(value = """
            select us.StoreCod  from user_store us where us.UserCod = :userCod and us.IsMainStore = 'S'
            """, nativeQuery = true)
    public String getMainStore(@Param("userCod") String userCod);

}
