package me.dangoslen.pathz.repository;

import me.dangoslen.pathz.models.Project;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProjectRepository {

    private final ConcurrentHashMap<Integer, Project> projectMap;
    private final ConcurrentHashMap<String, Project> projectNumberMap;

    ProjectRepository() {
        projectMap = new ConcurrentHashMap<>();
        projectNumberMap = new ConcurrentHashMap<>();
    }

    public void saveProject(Project project) {
        projectMap.merge(project.getId(), project, (k, v) -> project);
        projectNumberMap.merge(project.getPhoneNumber(), project, (k,v) -> project);
    }

    public Project getProject(Integer id) {
        return projectMap.get(id);
    }

    public Project getProjectFromPhoneNumber(String phoneNumber) {
        return projectNumberMap.get(phoneNumber);
    }
}
