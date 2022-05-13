package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.domain.CommentReport;
import com.fabianocampos.fidbackapi.dto.CommentReportDTO;
import com.fabianocampos.fidbackapi.dto.ParticipantDTO;
import com.fabianocampos.fidbackapi.dto.ReportDTO;
import com.fabianocampos.fidbackapi.dto.UserDTO;
import com.fabianocampos.fidbackapi.dto.converter.CommentReportConverter;
import com.fabianocampos.fidbackapi.dto.converter.ReportConverter;
import com.fabianocampos.fidbackapi.dto.converter.UserConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/reports")
public class ReportResource {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportConverter reportConverter;

    @Autowired
    private CommentReportConverter commentReportConverter;

    @Autowired
    private UserConverter userConverter;

    @RequestMapping(value = "/myreports", method = RequestMethod.GET)
    public ResponseEntity<List<ReportDTO>> findAllReportsByConnectedUser(@RequestParam String orderBy, Principal principal) {
        return ResponseEntity.ok(reportConverter.encode(reportService.findAllReportsByConnectedUser(orderBy, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ReportDTO> findById(@PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(reportConverter.encode(reportService.findById(id, principal.getName())));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReportDTO> create(@Validated @RequestBody ReportDTO reportDTO, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportConverter.encode(reportService.createOrUpdate(reportDTO, Operation.CREATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ReportDTO> update(@Validated @RequestBody ReportDTO reportDTO, @PathVariable Integer id, Principal principal) {
        reportDTO.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(reportConverter.encode(reportService.createOrUpdate(reportDTO, Operation.UPDATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id, Principal principal) {
        reportService.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<CommentReportDTO>> findComments(@PathVariable Integer id, Principal principal) {
        List<CommentReport> comments = reportService.findComments(id, principal.getName());
        List<CommentReportDTO> commentReportDTOS = commentReportConverter.encode(comments);
        return ResponseEntity.ok(commentReportDTOS);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST)
    public ResponseEntity<List<CommentReportDTO>> newComment(@PathVariable Integer id, @RequestBody CommentReportDTO commentReportDTO, Principal principal) {
        return ResponseEntity.ok(commentReportConverter.encode(reportService.newComment(id, commentReportDTO, principal.getName())));
    }

    @RequestMapping(value = "/{id}/comments/{commentId}", method = RequestMethod.PUT)
    public ResponseEntity<List<CommentReportDTO>> editComment(@PathVariable Integer id, @RequestBody CommentReportDTO commentReportDTO, Principal principal) {
        return ResponseEntity.ok(commentReportConverter.encode(reportService.editComment(id, commentReportDTO, principal.getName())));
    }

    @RequestMapping(value = "/{id}/comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<List<CommentReportDTO>> deleteComment(@PathVariable Integer id, @PathVariable Integer commentId, Principal principal) {
        return ResponseEntity.ok(commentReportConverter.encode(reportService.deleteComment(id, commentId, principal.getName())));
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<List<ParticipantDTO>> findParticipants(@PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(reportService.findParticipants(id, principal.getName()));
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<List<ParticipantDTO>> newParticipant(@PathVariable Integer id, @RequestBody ParticipantDTO user, Principal principal) {
        return ResponseEntity.ok(reportService.newParticipant(id, user, principal.getName()));
    }

    @RequestMapping(value = "/{id}/participants/{participantId}", method = RequestMethod.DELETE)
    public ResponseEntity<List<ParticipantDTO>> newParticipant(@PathVariable Integer id, @PathVariable Integer participantId, Principal principal) {
        return ResponseEntity.ok(reportService.deleteParticipant(id, participantId, principal.getName()));
    }

}
