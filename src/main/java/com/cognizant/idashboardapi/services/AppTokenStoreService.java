package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.errors.ResourceNotFoundException;
import com.cognizant.idashboardapi.models.AppTokenStore;
import com.cognizant.idashboardapi.repos.AppTokenStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppTokenStoreService {
    @Autowired
    AppTokenStoreRepository repository;

    public Optional<AppTokenStore> get(String id){
        return repository.findById(id);
    }

    public AppTokenStore assertOrGet(String id){
        Optional<AppTokenStore> optional = get(id);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ResourceNotFoundException("AppToken", "id", id);
        }
    }

    public Optional<AppTokenStore> getByRefId(String refId){
        return repository.findByRefId(refId);
    }

    public AppTokenStore assertOrGetByRefId(String refId){
        Optional<AppTokenStore> optional = repository.findByRefId(refId);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ResourceNotFoundException("AppToken", "ReferenceId", refId);
        }
    }

    public AppTokenStore add(AppTokenStore appTokenStore){
        return repository.insert(appTokenStore);
    }

    public void delete(String id){
        repository.deleteById(id);
    }

    public void deleteByRefId(String id) {
        repository.deleteByRefId(id);
    }
}
