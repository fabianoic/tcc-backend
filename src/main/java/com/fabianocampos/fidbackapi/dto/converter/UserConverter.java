package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.UserDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.UserRepository;
import com.fabianocampos.fidbackapi.services.UserService;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class UserConverter implements Converter<User, UserDTO> {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder pe;


    @Override
    public UserDTO encode(User user) {
        return UserDTO.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName())
                .nickname(user.getNickname()).email(user.getEmail()).createdAt(user.getCreatedAt()).build();
    }

    @Override
    public User decode(UserDTO userDto, Operation operation) {
        User user = null;
        if (operation == Operation.UPDATE) {
            user = userService.findById(userDto.getId());
        } else {
            user = new User();
            user.setCreatedAt(new Date());
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        if (userDto.getPassword() != null && userDto.getConfirmPassword() != null) {
            user.setPassword(pe.encode(userDto.getPassword()));
        }
        return user;
    }

}
