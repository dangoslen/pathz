package me.dangoslen.pathz.repository;

import me.dangoslen.pathz.models.Project;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProjectRepository {

    ConcurrentHashMap<Integer, Project> projectMap;

    ProjectRepository() {
        projectMap = new ConcurrentHashMap<>();
    }

    public void saveProject(Project project) {
        projectMap.merge(project.getId(), project, (k, v) -> project);
    }

    public Project getProject(Integer id) {
        return projectMap.get(id);
    }
}
