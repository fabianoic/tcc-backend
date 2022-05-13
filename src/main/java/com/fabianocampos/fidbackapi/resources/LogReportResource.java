package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.LogReportDTO;
import com.fabianocampos.fidbackapi.dto.converter.LogReportConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.LogReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/LogReports")
public class LogReportResource {

    @Autowired
    private LogReportService LogReportService;

    @Autowired
    private LogReportConverter logReportConverter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<LogReportDTO>> findAll() {
        return ResponseEntity.ok(logReportConverter.encode(LogReportService.findAll()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<LogReportDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(logReportConverter.encode(LogReportService.findById(id)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LogReportDTO> create(@Validated @RequestBody LogReportDTO LogReportDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logReportConverter.encode(LogReportService.createOrUpdate(LogReportDto, Operation.CREATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<LogReportDTO> update(@Validated @RequestBody LogReportDTO LogReportDto, @PathVariable Integer id) {
        LogReportDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(logReportConverter.encode(LogReportService.createOrUpdate(LogReportDto, Operation.UPDATE)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        LogReportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
