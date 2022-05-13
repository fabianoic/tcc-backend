package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.*;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import com.fabianocampos.fidbackapi.dto.*;
import com.fabianocampos.fidbackapi.dto.converter.CommentReportConverter;
import com.fabianocampos.fidbackapi.dto.converter.ParticipantConverter;
import com.fabianocampos.fidbackapi.dto.converter.ReportConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.*;
import com.fabianocampos.fidbackapi.services.exception.ObjectAlreadyExistsException;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import com.fabianocampos.fidbackapi.services.exception.PermissionInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repo;

    @Autowired
    private ReportConverter reportConverter;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private LabelService labelService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private CommentReportConverter commentReportConverter;

    @Autowired
    private CommentReportRepository commentReportRepository;

    @Autowired
    private ParticipantConverter participantConverter;

    @Autowired
    private CommentReportService commentReportService;

    @Autowired
    private LabelRepository labelRepository;

    public List<Report> findAllReportsByConnectedUser(String orderBy, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        List<Report> reports = new ArrayList<>();
        if ("title".equals(orderBy)) {
            reports = repo.findAllReportsByUserIdOrderByTitle(user.getId());
        } else if ("status".equals(orderBy)) {
            reports = repo.findAllReportsByUserIdOrderByStatus(user.getId());
        } else if ("createdAt".equals(orderBy)) {
            reports = repo.findAllReportsByUserIdOrderByCreatedAt(user.getId());
        }
        return reports;
    }

    public Report findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Report não encontrado!"));
    }

    public Report findById(Integer id, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        Report report = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Report não encontrado!"));
        Project project = projectService.findById(report.getProject().getId());
        User isUserParticipant = projectService.findParticipantById(project.getId(), user.getId(), connectedUserEmail);

        if (!report.getUser().equals(user) && !report.getUsers().contains(user) && !project.isVisibility() && isUserParticipant == null) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }
        return report;
    }

    public Report createOrUpdate(ReportDTO reportDTO, Operation operation, String connectedUserEmail) {
        Project project = projectService.findById(reportDTO.getProjectId());
        User user = userService.findByEmail(connectedUserEmail);
        User isUserParticipant = projectService.findParticipantById(reportDTO.getProjectId(), user.getId(), connectedUserEmail);

        if ((!project.isVisibility() && isUserParticipant == null) || (!reportDTO.getUserId().equals(user.getId()) && operation.equals(Operation.UPDATE))) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }

        Report report = reportConverter.decode(reportDTO, operation);
        report = repo.save(report);

        if (operation.equals(Operation.CREATE)) {
            if (isUserParticipant == null) {
                projectService.newParticipant(project.getId(), UserProjectDTO.builder().userId(user.getId()).userTypeId(UserType.USER.ordinal()).build(), connectedUserEmail);
            }
            Random random = new Random();
            int randomWintNextIntWithinARange = random.nextInt(4 - 1) + 1;
            String title = "";
            switch (randomWintNextIntWithinARange) {
                case 1:
                    title = "Tarefas";
                    break;
                case 2:
                    title = "Fazendo";
                    break;
                case 3:
                    title = "Pausado";
                    break;
                case 4:
                    title = "Concluído";
                    break;
            }
            Label label = labelService.findLabelByProject(report.getProject().getId(), title);
            Card card = cardRepository.save(Card.builder().report(report).label(label).build());
            label.getCards().add(card);
            labelRepository.save(label);

        }

        return report;
    }

    public void delete(Integer id, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        Report report = this.findById(id, connectedUserEmail);

        if (!user.equals(report.getUser()) && !userProjectRepository.existsByIdProjectIdAndIdUserIdAndUserType(report.getProject().getId(), user.getId(), UserType.ADMIN)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }

        repo.delete(report);
    }

    public List<CommentReport> findComments(Integer reportId, String connectedUserEmail) {
        Report report = this.findById(reportId);
        projectService.connectedUserHasPermission(report.getProject().getId(), connectedUserEmail);
        List<CommentReport> comments = repo.findComments(reportId);
        return comments;
    }

    public List<CommentReport> newComment(Integer reportId, CommentReportDTO commentReportDTO, String connectedUserEmail) {
        Report report = this.findById(reportId);
        projectService.connectedUserHasPermission(report.getProject().getId(), connectedUserEmail);

        CommentReport commentReport = commentReportConverter.decode(commentReportDTO, Operation.CREATE);

        commentReportRepository.save(commentReport);

        return repo.findComments(reportId);
    }

    public List<CommentReport> deleteComment(Integer reportId, Integer commentId, String connectedUserEmail) {
        Report report = this.findById(reportId);
        projectService.connectedUserHasPermission(report.getProject().getId(), connectedUserEmail);

        CommentReport commentReport = commentReportService.findById(commentId);
        User user = userService.findByEmail(connectedUserEmail);

        if (!commentReport.getUser().equals(user)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }

        report.getComments().remove(commentReport);
        commentReportRepository.delete(commentReport);

        return repo.findComments(reportId);
    }

    public List<CommentReport> editComment(Integer reportId, CommentReportDTO commentReportDTO, String connectedUserEmail) {
        Report report = this.findById(reportId);
        projectService.connectedUserHasPermission(report.getProject().getId(), connectedUserEmail);

        CommentReport commentReport = commentReportConverter.decode(commentReportDTO, Operation.UPDATE);
        User user = userService.findByEmail(connectedUserEmail);

        if (!commentReport.getUser().equals(user)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }

        commentReportRepository.save(commentReport);

        return findComments(reportId, connectedUserEmail);
    }

    public List<ParticipantDTO> findParticipants(Integer reportId, String connectedUserEmail) {
        Report report = this.findById(reportId);
        projectService.connectedUserHasPermission(report.getProject().getId(), connectedUserEmail);

        List<User> users = repo.findParticipants(reportId);
        users.add(repo.findOwner(reportId));

        List<ParticipantDTO> participants = participantConverter.encode(users, report.getProject().getId());


        for (ParticipantDTO participantDTO : participants) {
            UserProject userProject = userProjectRepository.findUserProjectById(participantDTO.getId(), report.getProject().getId());
            participantDTO.setUserType(userProject.getUserType().getDescription());
        }
        return participants;
    }

    public List<ParticipantDTO> newParticipant(Integer reportId, ParticipantDTO userDTO, String connectedUserEmail) {
        Report report = this.findById(reportId);
        projectService.connectedUserHasPermission(report.getProject().getId(), connectedUserEmail);
        User user = userService.findById(userDTO.getId());

        Project project = report.getProject();
        UserProject userProject = userProjectRepository.findUserProjectById(user.getId(), project.getId());
        if (userProject == null) {
            projectService.newParticipant(project.getId(), UserProjectDTO.builder().userId(user.getId()).userTypeId(userDTO.getUserTypeId()).build(), connectedUserEmail);
        }

        report.getUsers().add(user);
        repo.save(report);

        return findParticipants(reportId, connectedUserEmail);
    }

    public List<ParticipantDTO> deleteParticipant(Integer reportId, Integer participantId, String connectedUserEmail) {
        Report report = this.findById(reportId);
        projectService.connectedUserHasPermission(report.getProject().getId(), connectedUserEmail);
        User userConnected = userService.findByEmail(connectedUserEmail);
        User user = userService.findById(participantId);

        if (userConnected != user && report.getUser() != userConnected) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }

        report.getUsers().remove(user);
        repo.save(report);

        return findParticipants(reportId, connectedUserEmail);
    }

    public void deleteParticipantAllReports(Integer participantId, Integer projectId) {
        repo.deleteParticipantAllReports(participantId, projectId);
    }

}