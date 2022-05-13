package com.fabianocampos.fidbackapi.resources;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import com.fabianocampos.fidbackapi.dto.converter.CategoryConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fabianocampos.fidbackapi.domain.Category;
import com.fabianocampos.fidbackapi.dto.CategoryDTO;
import com.fabianocampos.fidbackapi.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryConverter categoryConverter;

    @ApiOperation(value = "Retorna lista de categorias buscada por id do projeto")
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDTO>> findByProject(@PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(categoryConverter.encode(categoryService.findByProjectId(id, principal.getName())));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> create(@Validated @RequestBody CategoryDTO categoryDto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryConverter.encode(categoryService.createOrUpdate(categoryDto, Operation.CREATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> update(@Validated @RequestBody CategoryDTO categoryDto, @PathVariable Integer id, Principal principal) {
        categoryDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryConverter.encode(categoryService.createOrUpdate(categoryDto, Operation.UPDATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id, Principal principal) {
        categoryService.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
