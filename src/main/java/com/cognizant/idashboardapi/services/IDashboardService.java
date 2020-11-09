package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.errors.ResourceExistsException;
import com.cognizant.idashboardapi.errors.ResourceNotFoundException;
import com.cognizant.idashboardapi.models.IDashboard;
import com.cognizant.idashboardapi.repos.IDashboardRepository;
import com.cognizant.idashboardapi.repos.impl.IDashboardRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class IDashboardService {

    @Autowired
    IDashboardRepository repository;
    @Autowired
    IDashboardRepositoryImpl implRepo;

    public List<IDashboard> getAllByPermissions(){
        return implRepo.findAll();
    }

    public List<IDashboard> getAll(){
        return repository.findAll();
    }

    public Optional<IDashboard> get(String id){
        return implRepo.findById(id);
    }

    public Optional<IDashboard> getByNameIgnoreCase(String name){
        return repository.findByNameIgnoreCase(name);
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
        return implRepo.getByProjectName(projectName);
    }

    public List<IDashboard> getByProjectNameList(List<String> projectNames) {
        return repository.findByProjectNameIn(projectNames);
    }

    public IDashboard add(IDashboard dashboard){
        assertInsert(dashboard);
        dashboard.setOpenAccess(false);
        return repository.insert(dashboard);
    }

    public IDashboard update(IDashboard dashboard){
        assertUpdate(dashboard);
        IDashboard exist = assertAndGet(dashboard.getId());
        dashboard.setCreatedUser(exist.getCreatedUser());
        dashboard.setCreatedDate(exist.getCreatedDate());
        return repository.save(dashboard);
    }

    public void deleteById(String id){
        assertAndGet(id);
        implRepo.deleteByIds(Arrays.asList(id));
    }

    public void deleteByIdIn(List<String> ids){
        implRepo.deleteByIds(ids);
    }

    private void assertUpdate(IDashboard dashboard) {
        Optional<IDashboard> optional = getByNameIgnoreCase(dashboard.getName());
        if (optional.isPresent() && !optional.get().getId().equals(dashboard.getId())){
            throw new ResourceExistsException("Dashboard","name", dashboard.getName());
        }
    }

    private void assertInsert(IDashboard dashboard) {
        if (!StringUtils.isEmpty(dashboard.getId())){
            throw new InvalidDetailsException("Id should be null/empty");
        }
        Optional<IDashboard> optional = getByNameIgnoreCase(dashboard.getName());
        if (optional.isPresent()){
            throw new ResourceExistsException("Dashboard","name", dashboard.getName());
        }
    }

    public void deleteByProjectName(String projectName) {
        repository.deleteByProjectName(projectName);
    }
}
