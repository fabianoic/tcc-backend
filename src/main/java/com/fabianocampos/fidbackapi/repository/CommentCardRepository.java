package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.CommentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentCardRepository extends JpaRepository<CommentCard, Integer>{

}
