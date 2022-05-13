package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.CommentCard;
import com.fabianocampos.fidbackapi.dto.CommentCardDTO;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.CommentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentCardConverter implements Converter<CommentCard, CommentCardDTO> {

    @Autowired
    private UserConverter userConverter;
    @Autowired
    private CommentCardService commentCardService;

    @Override
    public CommentCardDTO encode(CommentCard commentCard) {
        return CommentCardDTO.builder().id(commentCard.getId()).comment(commentCard.getComment()).build();
    }

    @Override
    public CommentCard decode(CommentCardDTO commentCardDTO, Operation operation) {
        CommentCard commentCard = operation == Operation.UPDATE ? commentCardService.findById(commentCardDTO.getId()) : new CommentCard();
        commentCard.setComment(commentCardDTO.getComment());
        return commentCard;
    }
}
