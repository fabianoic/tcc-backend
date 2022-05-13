package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.CommentCard;
import com.fabianocampos.fidbackapi.dto.CommentCardDTO;
import com.fabianocampos.fidbackapi.dto.converter.CommentCardConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.CommentCardRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentCardService {

    @Autowired
    private CommentCardRepository repo;

    @Autowired
    private CommentCardConverter commentCardConverter;

    public List<CommentCard> findAll() {
        return repo.findAll();
    }

    public CommentCard findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Comentário não encontrada!"));
    }

    public CommentCard createOrUpdate(CommentCardDTO commentCardDTO, Operation operation) {
        CommentCard commentCard = commentCardConverter.decode(commentCardDTO, operation);
        return repo.save(commentCard);
    }

    public void delete(Integer id) {
        CommentCard commentCard = this.findById(id);
        repo.delete(commentCard);
    }
}