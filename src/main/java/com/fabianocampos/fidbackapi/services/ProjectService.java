package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.*;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import com.fabianocampos.fidbackapi.dto.*;
import com.fabianocampos.fidbackapi.dto.converter.*;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.*;
import com.fabianocampos.fidbackapi.services.exception.ObjectAlreadyExistsException;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import com.fabianocampos.fidbackapi.services.exception.PermissionInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository repo;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private ProjectConverter projectConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserProjectConverter userProjectConverter;

    @Autowired
    private UserProjectService userProjectService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ParticipantConverter participantConverter;

    @Autowired
    private LabelConverter labelConverter;

    @Autowired
    private CardConverter cardConverter;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Project> findAll() {
        return repo.findAll();
    }

    public Project findById(Integer id, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        UserProject userProject = userProjectRepository.findUserProjectById(user.getId(), id);
        Project project = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Projeto não encontrado!"));
        if (userProject == null && !project.isVisibility()) {
            throw new PermissionInvalidException("Usuário não tem permissão esta ação.");
        }
        return project;
    }

    public Project findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Projeto não encontrado!"));
    }

    public List<Project> findProjectsByUserId(Principal principal, String orderBy) {
        User user = userService.findByEmail(principal.getName());
        List<Project> projects = null;
        if ("name".equals(orderBy)) {
            projects = repo.findProjectsByUserIdOrderByName(user.getId());
        } else if ("createdAt".equals(orderBy)) {
            projects = repo.findProjectsByUserIdOrderByCreatedAt(user.getId());
        } else if ("visibility".equals(orderBy)) {
            projects = repo.findProjectsByUserIdOrderByVisibility(user.getId());
        }
        return projects;
    }

    public List<Project> findByName(String nome, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        List<Project> projects = repo.findByName(nome);
        List<Project> newProjects = projects.stream().filter(project -> userProjectRepository.findUserProjectById(user.getId(), project.getId()) != null || project.isVisibility()
        ).collect(Collectors.toList());

        if (newProjects.isEmpty()) {
            throw new ObjectNotFoundException("Projeto não encontrado!");
        }

        return newProjects;
    }

    public Project createOrUpdate(ProjectDTO projectDTO, Operation operation, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        Project proj = repo.findProjectByNameAndByUserId(user.getId(), projectDTO.getName());
        UserProject userProjectAdmin = userProjectRepository.findUserProjectById(user.getId(), projectDTO.getId());

        if ((operation == Operation.CREATE && proj != null) || (operation == Operation.UPDATE && proj != null && proj.getId() != projectDTO.getId())) {
            throw new ObjectAlreadyExistsException("Já existe projeto com este nome");
        } else if ((userProjectAdmin == null || userProjectAdmin != null && !userProjectAdmin.getUserType().equals(UserType.ADMIN)) && operation.equals(Operation.UPDATE)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação");
        }

        Project project = projectConverter.decode(projectDTO, operation);
        project = repo.save(project);

        if (operation.equals(Operation.CREATE)) {
            UserProject userProject = UserProject.builder().id(UserProjectPK.builder().project(project).user(user).build()).userType(UserType.ADMIN).createdAt(new Date()).build();
            userProjectRepository.save(userProject);

            Label openLabel = Label.builder().title("Tarefas").project(project).build();
            Label closeLabel = Label.builder().title("Fazendo").project(project).build();
            Label stopedLabel = Label.builder().title("Pausado").project(project).build();
            Label finishedLabel = Label.builder().title("Concluído").project(project).build();

            labelRepository.save(openLabel);
            labelRepository.save(closeLabel);
            labelRepository.save(stopedLabel);
            labelRepository.save(finishedLabel);

            Category category1 = Category.builder().name("Categoria 1").project(project).build();
            Category category2 = Category.builder().name("Categoria 2").project(project).build();
            Category category3 = Category.builder().name("Categoria 3").project(project).build();
            Category category4 = Category.builder().name("Categoria 4").project(project).build();

            categoryRepository.save(category1);
            categoryRepository.save(category2);
            categoryRepository.save(category3);
            categoryRepository.save(category4);
        }

        return project;
    }

    public void delete(Integer id, String connectedUserEmail) {
        validateAdminType(connectedUserEmail, id, false);
        Project project = this.findById(id, connectedUserEmail);

        //repo.deleteAllCategoriesByProjectId(id);
        //repo.deleteAllLabelsByProjectId(id);
        //repo.deleteAllParticipantsByProjectId(id);
        //repo.deleteAllReportsByProjectId(id);
        //repo.deleteAllTagsByProjectId(id);

        repo.delete(project);

    }

    public User newParticipant(Integer projectId, UserProjectDTO userProjectDTO, String connectedUserEmail) {
        validateAdminType(connectedUserEmail, projectId, true);
        UserProject userProject = userProjectRepository.findUserProjectById(userProjectDTO.getUserId(), projectId);
        if (userProject != null) {
            throw new ObjectAlreadyExistsException("Usuário já faz parte deste projeto.");
        }

        userProjectDTO.setProjectId(projectId);
        UserProject newUserProject = userProjectRepository.save(userProjectConverter.decode(userProjectDTO, Operation.CREATE));
        Project project = findById(projectId);
        project.getParticipants().add(newUserProject);
        repo.save(project);
        return this.findParticipantById(projectId, newUserProject.getId().getUser().getId(), connectedUserEmail);
    }

    public void changeParticipant(Integer projectId, Integer participantId, UserProjectDTO userProjectDTO, String connectedUserEmail) {
        validateAdminType(connectedUserEmail, projectId, false);

        userProjectDTO.setProjectId(projectId);
        userProjectDTO.setUserId(participantId);
        userProjectRepository.save(userProjectConverter.decode(userProjectDTO, Operation.UPDATE));
    }

    public List<ParticipantDTO> findParticipants(Integer projectId, String connectedUserEmail) {
        connectedUserHasPermission(projectId, connectedUserEmail);

        List<User> users = repo.findParticipantsByProject(projectId);
        List<ParticipantDTO> participants = participantConverter.encode(users, projectId);
        return participants;
    }

    public List<User> findNewParticipants(Integer projectId, String nameValue, String connectedUserEmail) {
        connectedUserHasPermission(projectId, connectedUserEmail);

        List<User> users = repo.findNewParticipants(projectId, nameValue);

        return users;
    }

    public User findParticipantById(Integer projectId, Integer userId, String connectedUserEmail) {
        return repo.findParticipantById(projectId, userId);
    }

    public void deleteParticipantById(Integer projectId, Integer userId, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        if (!user.getId().equals(userId) && !validateAdminType(connectedUserEmail, projectId, false)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }

        userProjectRepository.delete(userProjectService.findById(userId, projectId));
        reportService.deleteParticipantAllReports(userId, projectId);
    }

    private boolean validateAdminType(String connectedUserEmail, Integer projectId, boolean testOpenProject) {
        User user = userService.findByEmail(connectedUserEmail);
        UserProject userProject = userProjectRepository.findUserProjectById(user.getId(), projectId);
        Project project = findById(projectId);

        if ((userProject == null || !userProject.getUserType().equals(UserType.ADMIN)) && (testOpenProject ? !project.isVisibility() : true)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }
        return true;
    }

    public List<Tag> findTags(Integer projectId, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        UserProject userProject = userProjectRepository.findUserProjectById(user.getId(), projectId);

        if ((userProject == null || userProject.getUserType().equals(UserType.USER))) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }

        return repo.findTags(projectId);

    }

    public List<Report> findReports(Integer projectId, String connectedUserEmail, String orderBy) {
        connectedUserHasPermission(projectId, connectedUserEmail);
        List<Report> reports = null;
        if ("title".equals(orderBy)) {
            reports = repo.findReportsOrderByTitle(projectId);
        } else if ("status".equals(orderBy)) {
            reports = repo.findReportsOrderByStatus(projectId);
        } else if ("createdAt".equals(orderBy)) {
            reports = repo.findReportsOrderByCreatedAt(projectId);
        }
        return reports;
    }

    public void connectedUserHasPermission(Integer projectId, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        UserProject userProject = userProjectRepository.findUserProjectById(user.getId(), projectId);
        Project project = findById(projectId);

        if (!project.isVisibility() && userProject == null) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }
    }

    public void connectedUserHasDevPermission(Integer projectId, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        UserProject userProject = userProjectRepository.findUserProjectById(user.getId(), projectId);

        if (userProject == null || userProject.getUserType().equals(UserType.USER)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }
    }

    public User findOwnerProjectByProjectId(Integer projectId) {
        return repo.findOwnerProjectByProjectId(projectId, UserType.ADMIN);
    }

    public List<Project> findOpenProjects(String connectedUserEmail, String orderBy) {
        User user = userService.findByEmail(connectedUserEmail);
        List<Project> projects = null;
        if ("name".equals(orderBy)) {
            projects = repo.findOpenProjectsOrderByName(user.getId());
        } else if ("createdAt".equals(orderBy)) {
            projects = repo.findOpenProjectsOrderByCreatedAt(user.getId());
        } else if ("visibility".equals(orderBy)) {
            projects = repo.findOpenProjectsOrderByVisibility(user.getId());
        }
        return projects;
    }

    public List<BoardListDTO> findBoardListByProject(Integer projectId, String connectedUserEmail) {
        List<BoardListDTO> boardList = new ArrayList<>();
        List<Label> labels = repo.findLabelByProject(projectId);
        for (Label label : labels) {
            boardList.add(BoardListDTO.builder().id(label.getId()).title(label.getTitle()).cards(cardConverter.encode(label.getCards())).done(label.getTitle().equals("Concluído")).build());
        }
        return boardList;
    }

    public void updateBoardListByProject(Integer projectId, UpdateBoardListDTO updateBoardListDTO, String connectedUserEmail) {
        List<Label> labels = repo.findLabelByProject(projectId);
        Label label = labels.get(updateBoardListDTO.getFromList());
        List<Card> cards = label.getCards();

        Card card = cards.get(updateBoardListDTO.getFrom());
        cards.remove(updateBoardListDTO.getFrom());

        label.setCards(cards);
        labelRepository.save((label));

        label = labels.get(updateBoardListDTO.getToList());
        label.getCards().add(updateBoardListDTO.getTo(), card);
        labelRepository.save(label);
        card.setLabel(label);
        cardRepository.save(card);

    }
}