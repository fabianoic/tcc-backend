package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.domain.UserProject;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.ParticipantDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.UserProjectService;
import com.fabianocampos.fidbackapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParticipantConverter implements Converter<User, ParticipantDTO> {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProjectService userProjectService;

    @Override
    public ParticipantDTO encode(User user) {
        return null;
    }

    @Override
    public User decode(ParticipantDTO participantDTO, Operation operation) {
        User user = null;
        if (operation == Operation.FIND) {
            user = userService.findById(participantDTO.getId());
        }
        return user;
    }

    public ParticipantDTO encode(User user, Integer projectId) {
        UserProject userProject = userProjectService.findById(user.getId(), projectId);
        return ParticipantDTO.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).nickname(user.getNickname()).userType(userProject != null ? userProject.getUserType().getDescription(): "Não é mais membro").userTypeId(userProject != null ? userProject.getUserType().ordinal() : 1).build();
    }

    public List<ParticipantDTO> encode(List<User> users, Integer projectId) {
        List<ParticipantDTO> participants = new ArrayList<>();
        users.stream().forEach(u -> participants.add(encode(u, projectId)));
        return participants;
    }

}
