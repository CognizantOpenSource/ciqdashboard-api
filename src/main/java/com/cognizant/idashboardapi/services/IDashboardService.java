package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.errors.ResourceNotFoundException;
import com.cognizant.idashboardapi.models.IDashboard;
import com.cognizant.idashboardapi.repos.IDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class IDashboardService {

    @Autowired
    IDashboardRepository repository;

    public List<IDashboard> getAll(){
        return repository.findAll();
    }

    public Optional<IDashboard> get(String id){
        return repository.findById(id);
    }

    public IDashboard assertAndGet(String id){
        Optional<IDashboard> optional = get(id);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ResourceNotFoundException("Dashboard", "id", id);
        }
    }

    public List<IDashboard> getByProjectName(String projectName) {
        return repository.findByProjectName(projectName);
    }

    public IDashboard add(IDashboard dashboard){
        assertInsert(dashboard);
        return repository.insert(dashboard);
    }

    public IDashboard update(IDashboard dashboard){
        assertAndGet(dashboard.getId());
        return repository.save(dashboard);
    }

    public void deleteById(String id){
        assertAndGet(id);
        repository.deleteById(id);
    }

    public void deleteByIdIn(List<String> ids){
        repository.deleteByIdIn(ids);
    }

    private void assertInsert(IDashboard dashboard) {
        if (!StringUtils.isEmpty(dashboard.getId())){
            throw new InvalidDetailsException("Id should be null/empty");
        }
    }

    public void deleteByProjectName(String projectName) {
        repository.deleteByProjectName(projectName);
    }
}
