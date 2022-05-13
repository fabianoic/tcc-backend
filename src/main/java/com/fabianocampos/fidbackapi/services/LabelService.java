package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.Label;
import com.fabianocampos.fidbackapi.dto.LabelDTO;
import com.fabianocampos.fidbackapi.dto.converter.LabelConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.LabelRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectAlreadyExistsException;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LabelService {

    @Autowired
    private LabelRepository repo;

    @Autowired
    private LabelConverter labelConverter;

    public List<Label> findAll() {
        return repo.findAll();
    }

    public Label findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Label não encontrada!"));
    }

    public Label findByTitle(String title) {
        Label label = repo.findByTitle(title);
        return Optional.ofNullable(label).orElseThrow(() -> new ObjectNotFoundException("Label não encontrada"));
    }

    public Label createOrUpdate(LabelDTO labelDTO, Operation operation) {
        if (repo.existsByTitle(labelDTO.getTitle())) {
            throw new ObjectAlreadyExistsException("Já existe label com este titulo.");
        }
        Label label = labelConverter.decode(labelDTO, operation);
        return repo.save(label);

    }

    public void delete(Integer id) {
        Label label = this.findById(id);
        repo.delete(label);
    }

    public Label findLabelByProject(Integer projectId, String title) {
        return repo.findByProjectIdAndTitle(projectId, title);
    }
}