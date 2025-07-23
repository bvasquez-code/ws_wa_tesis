package com.ccadmin.app.store.service;

import com.ccadmin.app.store.model.entity.StoreEntity;
import com.ccadmin.app.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public StoreEntity findById(String StoreCod)
    {
        return this.storeRepository.findById(StoreCod).get();
    }

    public List<StoreEntity> findAll()
    {
        return this.storeRepository.findAll();
    }


}
