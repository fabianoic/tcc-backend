package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.log.LogCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogCardRepository extends JpaRepository<LogCard, Integer>{

}
