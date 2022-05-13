package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.TagDTO;
import com.fabianocampos.fidbackapi.dto.converter.TagConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/Tags")
public class TagResource {

    @Autowired
    private TagService TagService;

    @Autowired
    private TagConverter TagConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TagDTO> create(@Validated @RequestBody TagDTO TagDto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(TagConverter.encode(TagService.createOrUpdate(TagDto, Operation.CREATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TagDTO> update(@Validated @RequestBody TagDTO TagDto, @PathVariable Integer id, Principal principal) {
        TagDto.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(TagConverter.encode(TagService.createOrUpdate(TagDto, Operation.UPDATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id, Principal principal) {
        TagService.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
