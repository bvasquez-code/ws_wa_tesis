package com.ccadmin.app.user.service;

import com.ccadmin.app.user.repository.UserStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStoreService {

    @Autowired
    private UserStoreRepository userStoreRepository;

    public String getMainStore(String userCod)
    {
        return this.userStoreRepository.getMainStore(userCod);
    }
}
