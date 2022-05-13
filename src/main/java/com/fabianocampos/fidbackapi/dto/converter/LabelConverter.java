package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.Label;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.LabelDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.LabelRepository;
import com.fabianocampos.fidbackapi.services.LabelService;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabelConverter implements Converter<Label, LabelDTO> {

    @Autowired
    private LabelService labelService;

    @Override
    public LabelDTO encode(Label label) {
        return LabelDTO.builder().id(label.getId()).title(label.getTitle()).build();
    }

    @Override
    public Label decode(LabelDTO labelDTO, Operation operation) {
        Label label = operation == Operation.UPDATE ? labelService.findById(labelDTO.getId()) : new Label();
        label.setTitle(labelDTO.getTitle());
        return label;
    }
}
