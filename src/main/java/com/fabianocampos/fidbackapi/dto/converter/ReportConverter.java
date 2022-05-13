package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.Report;
import com.fabianocampos.fidbackapi.domain.enums.StatusReport;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.ReportDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.CategoryService;
import com.fabianocampos.fidbackapi.services.ProjectService;
import com.fabianocampos.fidbackapi.services.ReportService;
import com.fabianocampos.fidbackapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

@Component
public class ReportConverter implements Converter<Report, ReportDTO> {

    @Autowired
    private ReportService reportService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private ProjectConverter projectConverter;

    @Autowired
    private UserConverter userConverter;

    @Override
    public ReportDTO encode(Report report) {
        return ReportDTO.builder().id(report.getId()).title(report.getTitle())
                .description(report.getDescription()).status(report.getStatus().getCod()).statusDescription(report.getStatus().getDescription()).categoryId(report.getCategory() != null ? report.getCategory().getId() : null).projectId(report.getProject().getId()).userId(report.getUser().getId()).createdAt(report.getCreatedAt()).build();

    }

    @Override
    public Report decode(ReportDTO reportDto, Operation operation) {
        Report report = null;
        if (operation.equals(Operation.FIND)) {
            return reportService.findById(reportDto.getId());
        } else if (operation.equals(Operation.CREATE)) {
            report = new Report();
            report.setCreatedAt(new Date());
            report.setUser(userService.findById(reportDto.getUserId()));
            report.setProject(projectService.findById(reportDto.getProjectId()));
        } else {
            report = reportService.findById(reportDto.getId());
        }
        report.setTitle(reportDto.getTitle());
        report.setDescription(reportDto.getDescription());
        Stream.of(StatusReport.values())
                .filter(st -> st.getCod().equals(reportDto.getStatus()))
                .findFirst().ifPresent(report::setStatus);
        if (reportDto.getCategoryId() != null) {
            report.setCategory(categoryService.findById(reportDto.getCategoryId()));
        }
        return report;
    }

}
