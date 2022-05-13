package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.UserProject;
import com.fabianocampos.fidbackapi.domain.UserProjectPK;
import com.fabianocampos.fidbackapi.domain.enums.StatusReport;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.UserProjectDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.ProjectService;
import com.fabianocampos.fidbackapi.services.UserProjectService;
import com.fabianocampos.fidbackapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Stream;

@Component
public class UserProjectConverter implements Converter<UserProject, UserProjectDTO> {

    @Autowired
    private UserProjectService userProjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Override
    public UserProjectDTO encode(UserProject userProject) {
        return UserProjectDTO.builder().projectId(userProject.getId().getProject().getId()).userId(userProject.getId().getUser().getId()).userTypeId(userProject.getUserType().getCod()).build();
    }

    @Override
    public UserProject decode(UserProjectDTO userProjectDTO, Operation operation) {
        UserProject userProject = null;

        if (operation.equals(Operation.FIND)) {
            return userProjectService.findById(userProjectDTO.getUserId(), userProjectDTO.getProjectId());

        } else if (operation == Operation.UPDATE) {
            userProject = userProjectService.findById(userProjectDTO.getUserId(), userProjectDTO.getProjectId());

        } else {
            userProject = new UserProject();
            userProject.setId(UserProjectPK.builder().project(projectService.findById(userProjectDTO.getProjectId())).user(userService.findById(userProjectDTO.getUserId())).build());
            userProject.setCreatedAt(new Date());
        }
        Stream.of(UserType.values())
                .filter(ut -> userProjectDTO.getUserTypeId().equals(ut.ordinal()))
                .findFirst().ifPresent(userProject::setUserType);

        return userProject;
    }

}
