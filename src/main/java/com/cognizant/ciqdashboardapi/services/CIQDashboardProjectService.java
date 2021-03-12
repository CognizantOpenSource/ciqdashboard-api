package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.errors.ResourceExistsException;
import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.CIQDashboardProject;
import com.cognizant.ciqdashboardapi.repos.CIQDashboardProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CIQDashboardProjectService {

    @Autowired
    private CIQDashboardProjectRepository repository;

    public List<CIQDashboardProject> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
    }

    public List<CIQDashboardProject> get(List<String> ids) {
        return repository.findByIds(ids, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
    }

    public List<String> getNamesByIds(List<String> ids) {
        return repository.findByIds(ids, Sort.by(Sort.Direction.DESC, "lastModifiedDate"))
                .stream().map(CIQDashboardProject::getName).collect(Collectors.toList());
    }

    public Optional<CIQDashboardProject> get(String id) {
        return repository.findById(id);
    }

    public CIQDashboardProject update(CIQDashboardProject project) {
        assertUpdate(project);
        return repository.save(project);
    }

    public CIQDashboardProject insert(CIQDashboardProject project) {
        return repository.insert(project);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private void assertUpdate(CIQDashboardProject project) {
        getOrAssert(project.getId());
        Optional<CIQDashboardProject> optional = getByNameIgnoreCase(project.getName());
        if (optional.isPresent() && !optional.get().getId().equals(project.getId())) {
            throw new ResourceExistsException("Project", "name", project.getName());
        }
    }

    public CIQDashboardProject getOrAssert(String id){
        Optional<CIQDashboardProject> optional = get(id);
        return optional.orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    }

    public void assertNotExists(CIQDashboardProject project) {
        Optional<CIQDashboardProject> byName = getByNameIgnoreCase(project.getName());
        if (byName.isPresent()) throw new ResourceExistsException("Project", "name", project.getName());
        if (!StringUtils.isEmpty(project.getId())) throw new InvalidDetailsException("id", project.getId(), "Id should be empty/null");
    }

    public Optional<CIQDashboardProject> getByNameNotId(String name, String id) {
        return repository.findFirstByNameAndIdNot(name, id);
    }

    public Optional<CIQDashboardProject> getByName(String name){
        return repository.findByName(name);
    }

    public Optional<CIQDashboardProject> getByNameIgnoreCase(String name){
        return repository.findByNameIgnoreCase(name);
    }

    public CIQDashboardProject assertAndGetByName(String name){
        Optional<CIQDashboardProject> byName = getByName(name);
        if (byName.isPresent()) {
            return byName.get();
        }else {
            throw new ResourceNotFoundException("Project", "name", name);
        }
    }

}
