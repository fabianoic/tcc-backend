package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.Category;
import com.fabianocampos.fidbackapi.domain.Project;
import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);

    @Query(value = "select c.* from category c where c.project_id = ?1", nativeQuery = true)
    List<Category> findByProjectId(Integer projectId);

    boolean existsByNameAndProjectId(String name, Integer projectId);

}
