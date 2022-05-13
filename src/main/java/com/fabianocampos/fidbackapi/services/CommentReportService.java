package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.CommentReport;
import com.fabianocampos.fidbackapi.domain.Label;
import com.fabianocampos.fidbackapi.dto.CommentReportDTO;
import com.fabianocampos.fidbackapi.dto.LabelDTO;
import com.fabianocampos.fidbackapi.dto.converter.CommentReportConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.CommentReportRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentReportService {

    @Autowired
    private CommentReportRepository repo;

    @Autowired
    private CommentReportConverter commentReportConverter;

    public List<CommentReport> findAll() {
        return repo.findAll();
    }

    public CommentReport findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Comentário não encontrado agora!"));
    }

    public CommentReport createOrUpdate(CommentReportDTO commentReportDTO, Operation operation) {
        CommentReport commentReport = commentReportConverter.decode(commentReportDTO, operation);
        return repo.save(commentReport);
    }

    public void delete(Integer id) {
        CommentReport commentReport = this.findById(id);
        repo.delete(commentReport);
    }
}