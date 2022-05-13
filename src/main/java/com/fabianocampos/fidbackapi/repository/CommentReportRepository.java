package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Integer>{

}
