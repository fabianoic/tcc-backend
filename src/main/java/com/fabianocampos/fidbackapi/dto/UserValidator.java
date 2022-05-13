package com.fabianocampos.fidbackapi.dto;

import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.repository.ProjectRepository;
import com.fabianocampos.fidbackapi.resources.exception.ValidationException;
import com.fabianocampos.fidbackapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserValidator implements Validator<UserDTO> {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;


    @Override
    public void validate(UserDTO input, Principal principal) throws ValidationException {
        User user = userService.findByPrincipal(principal);


    }

}
