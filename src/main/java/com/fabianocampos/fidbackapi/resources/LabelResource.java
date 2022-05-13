package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.LabelDTO;
import com.fabianocampos.fidbackapi.dto.converter.LabelConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/Labels")
public class LabelResource {

    @Autowired
    private LabelService LabelService;

    @Autowired
    private LabelConverter labelConverter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<LabelDTO>> findAll() {
        return ResponseEntity.ok(labelConverter.encode(LabelService.findAll()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<LabelDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(labelConverter.encode(LabelService.findById(id)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LabelDTO> create(@Validated @RequestBody LabelDTO LabelDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labelConverter.encode(LabelService.createOrUpdate(LabelDto, Operation.CREATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<LabelDTO> update(@Validated @RequestBody LabelDTO LabelDto, @PathVariable Integer id) {
        LabelDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(labelConverter.encode(LabelService.createOrUpdate(LabelDto, Operation.UPDATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        LabelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
