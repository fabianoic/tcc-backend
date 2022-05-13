package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.Category;
import com.fabianocampos.fidbackapi.domain.Project;
import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import com.fabianocampos.fidbackapi.dto.CategoryDTO;
import com.fabianocampos.fidbackapi.dto.converter.CategoryConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.CategoryRepository;
import com.fabianocampos.fidbackapi.repository.UserProjectRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectAlreadyExistsException;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import com.fabianocampos.fidbackapi.services.exception.PermissionInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProjectRepository userProjectRepository;

    public Category findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrada!"));
    }

    public List<Category> findByProjectId(Integer projectId, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        Project project = projectService.findById(projectId);
        User isUserParticipant = projectService.findParticipantById(projectId, user.getId(), connectedUserEmail);

        if (!project.isVisibility() && isUserParticipant == null) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }

        return repo.findByProjectId(projectId);
    }

    public Category createOrUpdate(CategoryDTO categoryDto, Operation operation, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        Project project = projectService.findById(categoryDto.getProjectId());

        if (!userProjectRepository.existsByIdProjectIdAndIdUserIdAndUserType(categoryDto.getProjectId(), user.getId(), UserType.ADMIN)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        } else if (repo.existsByNameAndProjectId(categoryDto.getName(), project.getId())) {
            throw new ObjectAlreadyExistsException("Esta categoria já existe");
        }

        Category category = categoryConverter.decode(categoryDto, operation);
        return repo.save(category);

    }

    public void delete(Integer id, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        Category category = this.findById(id);
        Project project = projectService.findById(category.getProject().getId());

        if (!userProjectRepository.existsByIdProjectIdAndIdUserIdAndUserType(project.getId(), user.getId(), UserType.ADMIN)) {
            throw new PermissionInvalidException("Usuário não tem permissão para está ação.");
        }

        repo.delete(category);
    }
}
