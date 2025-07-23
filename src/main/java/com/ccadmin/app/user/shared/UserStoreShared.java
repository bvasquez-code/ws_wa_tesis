package com.ccadmin.app.user.shared;

import com.ccadmin.app.user.service.UserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStoreShared {

    @Autowired
    private UserStoreService userStoreService;

    public String getMainStore(String userCod)
    {
        return this.userStoreService.getMainStore(userCod);
    }
}
