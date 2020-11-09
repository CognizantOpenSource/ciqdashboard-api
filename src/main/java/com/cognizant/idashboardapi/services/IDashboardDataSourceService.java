package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.errors.ResourceExistsException;
import com.cognizant.idashboardapi.errors.ResourceNotFoundException;
import com.cognizant.idashboardapi.models.IDashboardDataSource;
import com.cognizant.idashboardapi.repos.IDashboardDataSourceRepository;
import com.cognizant.idashboardapi.repos.impl.CollectorRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class IDashboardDataSourceService {
    @Autowired
    IDashboardDataSourceRepository repository;
    @Autowired
    MongoTemplate template;
    @Autowired
    CollectorRepositoryImpl collectorRepository;

    public List<IDashboardDataSource> getAll() {
        return repository.findAll();
    }

    public Optional<IDashboardDataSource> get(String id) {
        return repository.findById(id);
    }

    public IDashboardDataSource assertAndGet(String id) {
        Optional<IDashboardDataSource> optional = get(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("DataSource", "id", id);
        }
    }

    public IDashboardDataSource add(IDashboardDataSource iDashboardDataSource) {
        assertInsert(iDashboardDataSource);
        return repository.insert(iDashboardDataSource);
    }

    public IDashboardDataSource update(IDashboardDataSource iDashboardDataSource) {
        assertUpdate(iDashboardDataSource);
        return repository.save(iDashboardDataSource);
    }

    public void deleteById(String id) {
        assertAndGet(id);
        repository.deleteById(id);
    }

    public Optional<IDashboardDataSource> getByName(String name) {
        return repository.findByName(name);
    }

    public Optional<IDashboardDataSource> getByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    public IDashboardDataSource assertAndGetByName(String name) {
        Optional<IDashboardDataSource> optional = getByName(name);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("DataSource", "name", name);
        }
    }

    public void deleteByIdIn(List<String> ids) {
        repository.deleteByIdIn(ids);
    }

    private void assertInsert(IDashboardDataSource iDashboardDataSource) {
        if (!StringUtils.isEmpty(iDashboardDataSource.getId())) {
            throw new InvalidDetailsException("Id field should be empty/null");
        }
        assertCollectionName(iDashboardDataSource.getCollectionName());
        Optional<IDashboardDataSource> byName = getByNameIgnoreCase(iDashboardDataSource.getName());
        if (byName.isPresent()) {
            throw new ResourceExistsException("DataSource", "name", iDashboardDataSource.getName());
        }
    }

    private void assertUpdate(IDashboardDataSource iDashboardDataSource) {
        assertAndGet(iDashboardDataSource.getId());
        assertCollectionName(iDashboardDataSource.getCollectionName());
        Optional<IDashboardDataSource> optional = getByNameIgnoreCase(iDashboardDataSource.getName());
        if (optional.isPresent() && !optional.get().getId().equals(iDashboardDataSource.getId())) {
            throw new ResourceExistsException("DataSource", "name", iDashboardDataSource.getName());
        }
    }

    private void assertCollectionName(String name) {
        Set<String> collectionNames = collectorRepository.getCollectionNames();
        if (!collectionNames.contains(name)) {
            throw new InvalidDetailsException("Collection Name not Found in Database with name : " + name);
        }
    }
}
