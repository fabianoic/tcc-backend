package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.*;
import com.fabianocampos.fidbackapi.dto.converter.*;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/projects")
public class ProjectResource {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectConverter projectConverter;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private TagConverter tagConverter;

    @Autowired
    private ReportConverter reportConverter;

    @Autowired
    private ParticipantConverter participantConverter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDTO>> findAll() {
        return ResponseEntity.ok(projectConverter.encode(projectService.findAll()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProjectDTO> findById(@PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(projectConverter.encode(projectService.findById(id, principal.getName())));
    }

    @RequestMapping(value = "/myprojects", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDTO>> findProjectsByUserId(@RequestParam String orderBy, Principal principal) {
        log.info(principal.getName());
        return ResponseEntity.ok(projectConverter.encode(projectService.findProjectsByUserId(principal, orderBy)));
    }

    @RequestMapping(value = "/projectname", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDTO>> findByNome(@RequestParam String name, Principal principal) {
        return ResponseEntity.ok().body(projectConverter.encode(projectService.findByName(name, principal.getName())));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ProjectDTO> create(@Validated @RequestBody ProjectDTO projectDto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectConverter.encode(projectService.createOrUpdate(projectDto, Operation.CREATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProjectDTO> update(@Validated @RequestBody ProjectDTO projectDTO, @PathVariable Integer id, Principal principal) {
        projectDTO.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(projectConverter.encode(projectService.createOrUpdate(projectDTO, Operation.UPDATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id, Principal principal) {
        projectService.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<ParticipantDTO> newParticipant(@PathVariable Integer id, @RequestBody UserProjectDTO userProjectDTO, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(participantConverter.encode(projectService.newParticipant(id, userProjectDTO, principal.getName()), id));
    }

    @RequestMapping(value = "/{id}/participants/{participantId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeParticipant(@PathVariable Integer id, @PathVariable Integer participantId, @RequestBody UserProjectDTO userProjectDTO, Principal principal) {
        projectService.changeParticipant(id, participantId, userProjectDTO, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<List<ParticipantDTO>> findParticipants(@PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(projectService.findParticipants(id, principal.getName()));
    }

    @RequestMapping(value = "/{id}/participants/{participantId}", method = RequestMethod.GET)
    public ResponseEntity<ParticipantDTO> findParticipantById(@PathVariable Integer id, @PathVariable Integer participantId, Principal principal) {
        return ResponseEntity.ok(participantConverter.encode(projectService.findParticipantById(id, participantId, principal.getName()), id));
    }

    @RequestMapping(value = "/{id}/participants/{participantId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteParticipantById(@PathVariable Integer id, @PathVariable Integer participantId, Principal principal) {
        projectService.deleteParticipantById(id, participantId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/participants/invitation", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> findNewParticipants(@PathVariable Integer id, @RequestParam String nameValue, Principal principal) {
        return ResponseEntity.ok(userConverter.encode(projectService.findNewParticipants(id, nameValue, principal.getName())));
    }

    @RequestMapping(value = "/{id}/tags", method = RequestMethod.GET)
    public ResponseEntity<List<TagDTO>> findTags(@PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(tagConverter.encode(projectService.findTags(id, principal.getName())));
    }

    @RequestMapping(value = "/{id}/reports", method = RequestMethod.GET)
    public ResponseEntity<List<ReportDTO>> findReports(@RequestParam String orderBy, @PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(reportConverter.encode(projectService.findReports(id, principal.getName(), orderBy)));
    }

    @RequestMapping(value = "/openprojects", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDTO>> findOpenProjects(@RequestParam String orderBy, Principal principal) {
        return ResponseEntity.ok(projectConverter.encode(projectService.findOpenProjects(principal.getName(), orderBy)));
    }

    @RequestMapping(value = "/{id}/boardData", method = RequestMethod.GET)
    public ResponseEntity<List<BoardListDTO>> findBoardListByProject(@PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(projectService.findBoardListByProject(id, principal.getName()));
    }

    @RequestMapping(value = "/{id}/boardData", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateBoardListByProject(@PathVariable Integer id, @RequestBody UpdateBoardListDTO updateBoardListDTO, Principal principal) {
        projectService.updateBoardListByProject(id, updateBoardListDTO, principal.getName());
        return ResponseEntity.noContent().build();
    }

}
