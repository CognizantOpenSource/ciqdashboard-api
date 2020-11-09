package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.errors.ResourceExistsException;
import com.cognizant.idashboardapi.errors.ResourceNotFoundException;
import com.cognizant.idashboardapi.models.IDashboardProject;
import com.cognizant.idashboardapi.repos.IDashboardProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IDashboardProjectService {

    @Autowired
    private IDashboardProjectRepository repository;

    public List<IDashboardProject> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
    }

    public List<IDashboardProject> get(List<String> ids) {
        return repository.findByIds(ids, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
    }

    public List<String> getNamesByIds(List<String> ids) {
        return repository.findByIds(ids, Sort.by(Sort.Direction.DESC, "lastModifiedDate"))
                .stream().map(IDashboardProject::getName).collect(Collectors.toList());
    }

    public Optional<IDashboardProject> get(String id) {
        return repository.findById(id);
    }

    public IDashboardProject update(IDashboardProject project) {
        assertUpdate(project);
        return repository.save(project);
    }

    public IDashboardProject insert(IDashboardProject project) {
        return repository.insert(project);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private void assertUpdate(IDashboardProject project) {
        getOrAssert(project.getId());
        Optional<IDashboardProject> optional = getByNameIgnoreCase(project.getName());
        if (optional.isPresent() && !optional.get().getId().equals(project.getId())) {
            throw new ResourceExistsException("Project", "name", project.getName());
        }
    }

    public IDashboardProject getOrAssert(String id){
        Optional<IDashboardProject> optional = get(id);
        return optional.orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    }

    public void assertNotExists(IDashboardProject project) {
        Optional<IDashboardProject> byName = getByNameIgnoreCase(project.getName());
        if (byName.isPresent()) throw new ResourceExistsException("Project", "name", project.getName());
        if (!StringUtils.isEmpty(project.getId())) throw new InvalidDetailsException("id", project.getId(), "Id should be empty/null");
    }

    public Optional<IDashboardProject> getByNameNotId(String name, String id) {
        return repository.findFirstByNameAndIdNot(name, id);
    }

    public Optional<IDashboardProject> getByName(String name){
        return repository.findByName(name);
    }

    public Optional<IDashboardProject> getByNameIgnoreCase(String name){
        return repository.findByNameIgnoreCase(name);
    }

    public IDashboardProject assertAndGetByName(String name){
        Optional<IDashboardProject> byName = getByName(name);
        if (byName.isPresent()) {
            return byName.get();
        }else {
            throw new ResourceNotFoundException("Project", "name", name);
        }
    }

}
