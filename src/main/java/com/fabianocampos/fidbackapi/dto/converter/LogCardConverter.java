package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.log.LogCard;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.LogCardDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.LogCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogCardConverter implements Converter<LogCard, LogCardDTO> {

    @Autowired
    private LogCardService logCardService;


    @Override
    public LogCardDTO encode(LogCard logCard) {
        return LogCardDTO.builder().id(logCard.getId()).descrition(logCard.getDescrition()).build();
    }

    @Override
    public LogCard decode(LogCardDTO logCardDTO, Operation operation) {
        LogCard logCard = operation == Operation.UPDATE ? logCardService.findById(logCardDTO.getId()) : new LogCard();
        logCard.setDescrition(logCardDTO.getDescrition());
        return logCard;
    }
}
