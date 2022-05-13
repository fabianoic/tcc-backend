package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.Tag;
import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.TagDTO;
import com.fabianocampos.fidbackapi.dto.TagDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.TagService;
import com.fabianocampos.fidbackapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagConverter implements Converter<Tag, TagDTO> {

    @Autowired
    private TagService tagService;

    @Override
    public TagDTO encode(Tag tag) {
        return TagDTO.builder().id(tag.getId()).name(tag.getName()).color(tag.getColor()).build();
    }

    @Override
    public Tag decode(TagDTO tagDTO, Operation operation) {
        Tag tag = operation == Operation.UPDATE ? tagService.findById(tagDTO.getId()) : new Tag();
        tag.setName(tagDTO.getName());
        tag.setColor(tagDTO.getColor());
        return tag;
    }
}
