package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.errors.ResourceExistsException;
import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import com.cognizant.ciqdashboardapi.repos.CIQDashboardRepository;
import com.cognizant.ciqdashboardapi.repos.impl.CIQDashboardRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CIQDashboardService {

    @Autowired
    CIQDashboardRepository repository;
    @Autowired
    CIQDashboardRepositoryImpl implRepo;

    public List<CIQDashboard> getAllByPermissions(){
        return implRepo.findAll();
    }

    public List<CIQDashboard> getAll(){
        return repository.findAll();
    }

    public Optional<CIQDashboard> get(String id){
        return implRepo.findById(id);
    }

    public Optional<CIQDashboard> getByNameIgnoreCase(String name){
        return repository.findByNameIgnoreCase(name);
    }

    public CIQDashboard assertAndGet(String id){
        Optional<CIQDashboard> optional = get(id);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ResourceNotFoundException("Dashboard", "id", id);
        }
    }

    public List<CIQDashboard> getByProjectName(String projectName) {
        return implRepo.getByProjectName(projectName);
    }

    public List<CIQDashboard> getByProjectNameList(List<String> projectNames) {
        return repository.findByProjectNameIn(projectNames);
    }

    public CIQDashboard add(CIQDashboard dashboard){
        assertInsert(dashboard);
        dashboard.setOpenAccess(false);
        return repository.insert(dashboard);
    }

    public CIQDashboard update(CIQDashboard dashboard){
        assertUpdate(dashboard);
        CIQDashboard exist = assertAndGet(dashboard.getId());
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

    private void assertUpdate(CIQDashboard dashboard) {
        Optional<CIQDashboard> optional = getByNameIgnoreCase(dashboard.getName());
        if (optional.isPresent() && !optional.get().getId().equals(dashboard.getId())){
            throw new ResourceExistsException("Dashboard","name", dashboard.getName());
        }
    }

    private void assertInsert(CIQDashboard dashboard) {
        if (!StringUtils.isEmpty(dashboard.getId())){
            throw new InvalidDetailsException("Id should be null/empty");
        }
        Optional<CIQDashboard> optional = getByNameIgnoreCase(dashboard.getName());
        if (optional.isPresent()){
            throw new ResourceExistsException("Dashboard","name", dashboard.getName());
        }
    }

    public void deleteByProjectName(String projectName) {
        repository.deleteByProjectName(projectName);
    }
}
