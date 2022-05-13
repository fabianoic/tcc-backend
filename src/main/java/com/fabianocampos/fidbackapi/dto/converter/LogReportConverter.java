package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.log.LogReport;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.LogReportDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.LogReportRepository;
import com.fabianocampos.fidbackapi.services.LogReportService;
import com.fabianocampos.fidbackapi.services.UserService;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogReportConverter implements Converter<LogReport, LogReportDTO> {

    @Autowired
    private LogReportService logReportService;


    @Override
    public LogReportDTO encode(LogReport logReport) {
        return LogReportDTO.builder().id(logReport.getId()).descrition(logReport.getDescription()).build();
    }

    @Override
    public LogReport decode(LogReportDTO logReportDTO, Operation operation) {
        LogReport logReport = operation == Operation.UPDATE ? logReportService.findById(logReportDTO.getId()) : new LogReport();
        logReport.setDescription(logReportDTO.getDescrition());
        return logReport;
    }
}
