package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.CommentCardDTO;
import com.fabianocampos.fidbackapi.dto.converter.CommentCardConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.CommentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/CommentCards")
public class CommentCardResource {

    @Autowired
    private CommentCardService CommentCardService;

    @Autowired
    private CommentCardConverter CommentCardConverter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CommentCardDTO>> findAll() {
        return ResponseEntity.ok(CommentCardConverter.encode(CommentCardService.findAll()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CommentCardDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(CommentCardConverter.encode(CommentCardService.findById(id)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CommentCardDTO> create(@Validated @RequestBody CommentCardDTO CommentCardDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentCardConverter.encode(CommentCardService.createOrUpdate(CommentCardDto, Operation.CREATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CommentCardDTO> update(@Validated @RequestBody CommentCardDTO CommentCardDto, @PathVariable Integer id) {
        CommentCardDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommentCardConverter.encode(CommentCardService.createOrUpdate(CommentCardDto, Operation.UPDATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        CommentCardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
