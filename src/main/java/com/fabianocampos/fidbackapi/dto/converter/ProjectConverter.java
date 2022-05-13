package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.Project;
import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.ProjectDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProjectConverter implements Converter<Project, ProjectDTO> {

    @Autowired
    private ProjectService projectService;

    @Override
    public ProjectDTO encode(Project project) {
        User user = projectService.findOwnerProjectByProjectId(project.getId());
        return ProjectDTO.builder().id(project.getId()).description(project.getDescription()).name(project.getName()).visibility(project.isVisibility()).createdAt(project.getCreatedAt()).projectOwnerId(user.getId()).build();
    }

    @Override
    public Project decode(ProjectDTO projectDto, Operation operation) {
        Project project = null;
        if (operation.equals(Operation.FIND)) {
            return projectService.findById(projectDto.getId());
        } else if (operation.equals(Operation.CREATE)) {
            project = new Project();
            project.setCreatedAt(new Date());
        } else {
            project = projectService.findById(projectDto.getId());
        }
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setVisibility(projectDto.isVisibility());
        return project;
    }

}
