package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.UserProject;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Integer> {

    @Query(value = "select up.* from user_project up where user_id = ?1 and project_id = ?2", nativeQuery = true)
    UserProject findUserProjectById(Integer userId, Integer projectId);

    boolean existsByIdProjectIdAndIdUserIdAndUserType(Integer projectId, Integer userId, UserType userType);

}
