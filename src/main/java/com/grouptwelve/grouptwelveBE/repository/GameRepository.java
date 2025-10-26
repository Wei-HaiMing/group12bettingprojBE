package com.grouptwelve.grouptwelveBE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.grouptwelve.grouptwelveBE.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByStatus(String status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("delete from Game g where g.status = :status")
    int deleteByStatus(@Param("status") String status);
}
