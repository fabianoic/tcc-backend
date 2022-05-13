package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.Category;
import com.fabianocampos.fidbackapi.domain.Project;
import com.fabianocampos.fidbackapi.dto.CategoryDTO;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.CategoryRepository;
import com.fabianocampos.fidbackapi.services.CategoryService;
import com.fabianocampos.fidbackapi.services.ProjectService;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<Category, CategoryDTO> {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProjectConverter projectConverter;

    @Autowired
    private ProjectService projectService;

    @Override
    public CategoryDTO encode(Category category) {
        return CategoryDTO.builder().name(category.getName()).id(category.getId()).projectId(category.getProject().getId()).build();
    }

    @Override
    public Category decode(CategoryDTO categoryDto, Operation operation) {
        Category category = null;
        if (operation.equals(Operation.FIND)) {
            return categoryService.findById(categoryDto.getId());
        } else if (operation.equals(Operation.CREATE)) {
            category = new Category();
            Project project = projectService.findById(categoryDto.getProjectId());
            category.setProject(project);
        } else {
            category = categoryService.findById(categoryDto.getId());
        }
        category.setName(categoryDto.getName());
        return category;
    }

}
