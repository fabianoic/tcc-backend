package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.Project;
import com.fabianocampos.fidbackapi.domain.Tag;
import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.domain.UserProject;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import com.fabianocampos.fidbackapi.dto.TagDTO;
import com.fabianocampos.fidbackapi.dto.converter.TagConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.ProjectRepository;
import com.fabianocampos.fidbackapi.repository.TagRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectAlreadyExistsException;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import com.fabianocampos.fidbackapi.services.exception.PermissionInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository repo;

    @Autowired
    private TagConverter TagConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProjectService userProjectService;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;

    public Tag findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Tag não encontrada!"));
    }

    public Tag createOrUpdate(TagDTO tagDTO, Operation operation, String connectedUserEmail) {
        Tag tag = TagConverter.decode(tagDTO, operation);
        validate(tag, connectedUserEmail);

        tag = repo.save(tag);

        if (operation.equals(Operation.CREATE)) {
            Project project = projectService.findById(tag.getProject().getId());
            project.getTags().add(tag);
            projectRepository.save(project);
        }

        return tag;
    }

    public void delete(Integer id, String connectedUserEmail) {
        Tag tag = findById(id);
        validate(tag, connectedUserEmail);

        repo.delete(tag);
    }

    private void validate(Tag tag, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        UserProject userProject = userProjectService.findById(user.getId(), tag.getProject().getId());
        Project project = projectService.findById(tag.getProject().getId());

        if (userProject == null || userProject.getUserType().equals(UserType.USER)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }
    }
}