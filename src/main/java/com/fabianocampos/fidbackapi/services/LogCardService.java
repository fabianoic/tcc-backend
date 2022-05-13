package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.log.LogCard;
import com.fabianocampos.fidbackapi.dto.LogCardDTO;
import com.fabianocampos.fidbackapi.dto.converter.LogCardConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.LogCardRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogCardService {

    @Autowired
    private LogCardRepository repo;

    @Autowired
    private LogCardConverter logCardConverter;

    public List<LogCard> findAll() {
        return repo.findAll();
    }

    public LogCard findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Log não encontrado!"));
    }

    public LogCard createOrUpdate(LogCardDTO logCardDTO, Operation operation) {
        LogCard logCard = logCardConverter.decode(logCardDTO, operation);
        return repo.save(logCard);
    }

    public void delete(Integer id) {
        LogCard logCard = this.findById(id);
        repo.delete(logCard);
    }
}
