package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.CommentReport;
import com.fabianocampos.fidbackapi.domain.UserProject;
import com.fabianocampos.fidbackapi.dto.CommentReportDTO;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.ParticipantDTO;
import com.fabianocampos.fidbackapi.dto.ReportDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.CommentReportRepository;
import com.fabianocampos.fidbackapi.repository.UserProjectRepository;
import com.fabianocampos.fidbackapi.services.CommentReportService;
import com.fabianocampos.fidbackapi.services.ReportService;
import com.fabianocampos.fidbackapi.services.UserService;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CommentReportConverter implements Converter<CommentReport, CommentReportDTO> {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private CommentReportService commentReportService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipantConverter participantConverter;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Override
    public CommentReportDTO encode(CommentReport commentReport) {
        UserProject userProject = userProjectRepository.findUserProjectById(commentReport.getUser().getId(), commentReport.getReport().getProject().getId());
        ParticipantDTO participantDTO = participantConverter.encode(commentReport.getUser(), commentReport.getReport().getProject().getId());
        participantDTO.setUserType(userProject == null ? "Não é mais membro" : userProject.getUserType().getDescription());
        CommentReportDTO commentReportDTO = CommentReportDTO.builder().id(commentReport.getId()).participant(participantDTO).comment(commentReport.getComment()).createdAt(commentReport.getCreatedAt()).updatedAt(commentReport.getUpdatedAt()).build();

        return commentReportDTO;
    }

    @Override
    public CommentReport decode(CommentReportDTO commentReportDTO, Operation operation) {
        CommentReport commentReport = null;

        if (operation.equals(Operation.FIND)) {
            return commentReportService.findById(commentReportDTO.getId());
        } else if (operation.equals(Operation.CREATE)) {
            commentReport = new CommentReport();
            commentReport.setCreatedAt(new Date());
            commentReport.setReport(reportService.findById(commentReportDTO.getReportId()));
            commentReport.setUser(userService.findById(commentReportDTO.getUserId()));
        } else if (operation.equals(Operation.UPDATE)) {
            commentReport = commentReportService.findById(commentReportDTO.getId());
            commentReport.setUpdatedAt(new Date());
        }
        commentReport.setComment(commentReportDTO.getComment());
        return commentReport;
    }
}
