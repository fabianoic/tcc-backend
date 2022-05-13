package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.CommentReportDTO;
import com.fabianocampos.fidbackapi.dto.converter.CommentReportConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.CommentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/CommentReports")
public class CommentReportResource {

    @Autowired
    private CommentReportService CommentReportService;

    @Autowired
    private CommentReportConverter CommentReportConverter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CommentReportDTO>> findAll() {
        return ResponseEntity.ok(CommentReportConverter.encode(CommentReportService.findAll()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CommentReportDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(CommentReportConverter.encode(CommentReportService.findById(id)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CommentReportDTO> create(@Validated @RequestBody CommentReportDTO CommentReportDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentReportConverter.encode(CommentReportService.createOrUpdate(CommentReportDto, Operation.CREATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CommentReportDTO> update(@Validated @RequestBody CommentReportDTO CommentReportDto, @PathVariable Integer id) {
        CommentReportDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommentReportConverter.encode(CommentReportService.createOrUpdate(CommentReportDto, Operation.UPDATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        CommentReportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
