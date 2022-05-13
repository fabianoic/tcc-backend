package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.Project;
import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.domain.UserProject;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.UserProjectRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import com.fabianocampos.fidbackapi.services.exception.PermissionInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProjectService {

    @Autowired
    private UserProjectRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    public UserProject findById(Integer userId, Integer projectId) {
        UserProject userProject = repo.findUserProjectById(userId, projectId);
        return userProject;
    }

    public UserProject createOrUpdate(UserProject userProject, Operation operation, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        UserProject userProjectAdmin = findById(user.getId(), userProject.getId().getProject().getId());
        Project project = projectService.findById(userProject.getId().getProject().getId(), connectedUserEmail);

        if ((userProjectAdmin != null && userProjectAdmin.getUserType().equals(UserType.ADMIN)) || (operation.equals(Operation.CREATE) && project.isVisibility() && userProject.getUserType().equals(UserType.USER))) {
            if (operation.equals(Operation.UPDATE)) {
                UserProject userProject1 = repo.findUserProjectById(userProject.getId().getUser().getId(), userProject.getId().getProject().getId());
                userProject1.setUserType(userProject.getUserType());
                userProject = userProject1;
            }
            return repo.save(userProject);
        } else {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }


    }

    public void delete(Integer userId, Integer projectId, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        UserProject userProject = findById(userId, projectId);
        UserProject userProjectAdmin = findById(user.getId(), projectId);

        if (user.getId() == userProject.getId().getUser().getId() || userProjectAdmin.getUserType().equals(UserType.ADMIN)) {
            repo.delete(userProject);
        }


    }
}