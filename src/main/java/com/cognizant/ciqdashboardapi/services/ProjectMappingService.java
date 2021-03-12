package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.base.models.UserPrincipal;
import com.cognizant.ciqdashboardapi.errors.ResourceExistsException;
import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.ProjectMapping;
import com.cognizant.ciqdashboardapi.repos.ProjectMappingRepository;
import com.cognizant.ciqdashboardapi.repos.impl.ProjectMappingRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectMappingService {
    @Autowired
    ProjectMappingRepository repository;
    @Autowired
    ProjectMappingRepositoryImpl implRepo;

    public Optional<ProjectMapping> get(String projectId) {
        return repository.findById(projectId);
    }

    public ProjectMapping assertOrGet(String projectId){
        Optional<ProjectMapping> projectMapping = get(projectId);
        if (!projectMapping.isPresent()){
            throw new ResourceNotFoundException("ProjectMapping", "projectId", projectId);
        }
        return projectMapping.get();
    }

    public List<ProjectMapping> getAll() {
        return repository.findAll();
    }

    public ProjectMapping insert(String projectId) {
        Optional<ProjectMapping> optional = get(projectId);
        if (optional.isPresent()){
            throw new ResourceExistsException("ProjectMapping", "projectId", projectId);
        }
        String userId = getCurrentUserId();
        ProjectMapping projectMapping = new ProjectMapping(projectId, userId, Arrays.asList(userId), new ArrayList<>());
        return repository.insert(projectMapping);
    }

    public ProjectMapping update(String projectId) {
        ProjectMapping projectMapping = assertOrGet(projectId);
        String currentUserId = getCurrentUserId();
        if (CollectionUtils.isEmpty(projectMapping.getUserIds())){
            projectMapping.setUserIds(new ArrayList<>());
        }
        projectMapping.getUserIds().add(currentUserId);
        return repository.save(projectMapping);
    }

    public void delete(String projectId) {
        repository.deleteById(projectId);
    }

    public List<String> getUserProjectIds(String userId) {
        List<ProjectMapping> projectMappingList = repository.findByUserIdsContaining(userId);
        return projectMappingList.stream().map(ProjectMapping::getProjectId).collect(Collectors.toList());
    }

    public List<String> getUserProjectIds(String userId, List<String> teams) {
        List<ProjectMapping> projectMappingList = repository.findByUserIdsContainingOrTeamIdsIn(userId, teams);
        return projectMappingList.stream().map(ProjectMapping::getProjectId).collect(Collectors.toList());
    }

    public List<String> getUserOwnProjectIds(String userId) {
        List<ProjectMapping> projectMappingList = repository.findByOwnerId(userId);
        return projectMappingList.stream().map(ProjectMapping::getProjectId).collect(Collectors.toList());
    }

    public List<String> getCurrentUserProjectIds() {
        String currentUserId = getCurrentUserId();
        return getUserProjectIds(currentUserId);
    }

    public List<ProjectMapping> updateWithUser(String userId, List<String> projectIds) {
        removeUserFormExistingProjects(userId);
        List<ProjectMapping> allById = new ArrayList<>((Collection<? extends ProjectMapping>) repository.findAllById(projectIds));
        allById.forEach(projectMapping -> {
            if (CollectionUtils.isEmpty(projectMapping.getUserIds())){
                projectMapping.setUserIds(new ArrayList<>());
            }
            projectMapping.getUserIds().add(userId);
        });
        return repository.saveAll(allById);
    }

    private void removeUserFormExistingProjects(String userId) {
        List<ProjectMapping> list = repository.findByUserIdsContaining(userId);
        list.stream().forEach(projectMapping -> {
            projectMapping.getUserIds().remove(userId);
        });
        repository.saveAll(list);
    }

    public String getCurrentUserId(){
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getId();
    }

    public List<ProjectMapping> updateWithTeams(String teamId, List<String> projectIds) {
        implRepo.updateWithTeams(teamId, projectIds);
        return new ArrayList<>((Collection<? extends ProjectMapping>) repository.findAllById(projectIds));
    }

    public List<String> getByTeamName(String teamId) {
        List<ProjectMapping> projectMappingList = repository.findByTeamIdsContaining(teamId);
        return projectMappingList.stream().map(ProjectMapping::getProjectId).collect(Collectors.toList());
    }

    public void deleteTeamIdFromProject(String projectId, String teamId) {
        implRepo.removeWithTeams(projectId, teamId);
    }
}
