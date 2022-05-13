package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.log.LogReport;
import com.fabianocampos.fidbackapi.dto.LogReportDTO;
import com.fabianocampos.fidbackapi.dto.converter.LogReportConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.LogReportRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogReportService {

    @Autowired
    private LogReportRepository repo;

    @Autowired
    private LogReportConverter logReportConverter;

    public List<LogReport> findAll() {
        return repo.findAll();
    }

    public LogReport findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Log não encontrado!"));
    }

    public LogReport createOrUpdate(LogReportDTO logReportDTO, Operation operation) {
        LogReport logReport = logReportConverter.decode(logReportDTO, operation);
        return repo.save(logReport);
    }

    public void delete(Integer id) {
        LogReport logReport = this.findById(id);
        repo.delete(logReport);
    }
}
