package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.LogCardDTO;
import com.fabianocampos.fidbackapi.dto.converter.LogCardConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.LogCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/LogCards")
public class LogCardResource {

    @Autowired
    private LogCardService LogCardService;

    @Autowired
    private LogCardConverter logCardConverter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<LogCardDTO>> findAll() {
        return ResponseEntity.ok(logCardConverter.encode(LogCardService.findAll()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<LogCardDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(logCardConverter.encode(LogCardService.findById(id)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LogCardDTO> create(@Validated @RequestBody LogCardDTO LogCardDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logCardConverter.encode(LogCardService.createOrUpdate(LogCardDto, Operation.CREATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<LogCardDTO> update(@Validated @RequestBody LogCardDTO LogCardDto, @PathVariable Integer id) {
        LogCardDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(logCardConverter.encode(LogCardService.createOrUpdate(LogCardDto, Operation.UPDATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        LogCardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
