package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {

    Label findByTitle(String title);

    boolean existsByTitle(String title);

    Label findByProjectIdAndTitle(Integer id, String title);
}
