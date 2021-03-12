package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.errors.ResourceExistsException;
import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.CIQDashboardDataSource;
import com.cognizant.ciqdashboardapi.repos.CIQDashboardDataSourceRepository;
import com.cognizant.ciqdashboardapi.repos.impl.CollectorRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CIQDashboardDataSourceService {
    @Autowired
    CIQDashboardDataSourceRepository repository;
    @Autowired
    MongoTemplate template;
    @Autowired
    CollectorRepositoryImpl collectorRepository;

    public List<CIQDashboardDataSource> getAll() {
        return repository.findAll();
    }

    public Optional<CIQDashboardDataSource> get(String id) {
        return repository.findById(id);
    }

    public CIQDashboardDataSource assertAndGet(String id) {
        Optional<CIQDashboardDataSource> optional = get(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("DataSource", "id", id);
        }
    }

    public CIQDashboardDataSource add(CIQDashboardDataSource iDashboardDataSource) {
        assertInsert(iDashboardDataSource);
        return repository.insert(iDashboardDataSource);
    }

    public CIQDashboardDataSource update(CIQDashboardDataSource iDashboardDataSource) {
        assertUpdate(iDashboardDataSource);
        return repository.save(iDashboardDataSource);
    }

    public void deleteById(String id) {
        assertAndGet(id);
        repository.deleteById(id);
    }

    public Optional<CIQDashboardDataSource> getByName(String name) {
        return repository.findByName(name);
    }

    public Optional<CIQDashboardDataSource> getByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    public CIQDashboardDataSource assertAndGetByName(String name) {
        Optional<CIQDashboardDataSource> optional = getByName(name);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("DataSource", "name", name);
        }
    }

    public void deleteByIdIn(List<String> ids) {
        repository.deleteByIdIn(ids);
    }

    private void assertInsert(CIQDashboardDataSource iDashboardDataSource) {
        if (!StringUtils.isEmpty(iDashboardDataSource.getId())) {
            throw new InvalidDetailsException("Id field should be empty/null");
        }
        assertCollectionName(iDashboardDataSource.getCollectionName());
        Optional<CIQDashboardDataSource> byName = getByNameIgnoreCase(iDashboardDataSource.getName());
        if (byName.isPresent()) {
            throw new ResourceExistsException("DataSource", "name", iDashboardDataSource.getName());
        }
    }

    private void assertUpdate(CIQDashboardDataSource iDashboardDataSource) {
        assertAndGet(iDashboardDataSource.getId());
        assertCollectionName(iDashboardDataSource.getCollectionName());
        Optional<CIQDashboardDataSource> optional = getByNameIgnoreCase(iDashboardDataSource.getName());
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
