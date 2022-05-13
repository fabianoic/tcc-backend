package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByNickname(String nickname);

    User findByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

}
