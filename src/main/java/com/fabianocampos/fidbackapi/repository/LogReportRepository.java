package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.log.LogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogReportRepository extends JpaRepository<LogReport, Integer>{

}
