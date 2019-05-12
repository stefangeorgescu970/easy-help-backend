package com.easyhelp.application.service.stored_blood;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.repository.StoredBloodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoredBloodServiceImpl implements StoredBloodServiceInterface {

    @Autowired
    private StoredBloodRepository storedBloodRepository;

    @Override
    public void storeBlood(StoredBlood storedBlood) {
        storedBloodRepository.save(storedBlood);
    }
}
