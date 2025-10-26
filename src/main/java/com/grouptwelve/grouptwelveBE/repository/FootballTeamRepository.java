package com.grouptwelve.grouptwelveBE.repository;

import com.grouptwelve.grouptwelveBE.model.FootballTeam; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FootballTeamRepository extends JpaRepository<FootballTeam, Long> {

    
    List<FootballTeam> findByHomeTeamIgnoreCase(String homeTeam);
    List<FootballTeam> findByAwayTeamIgnoreCase(String awayTeam);
    Optional<FootballTeam> findByHomeTeamIgnoreCaseAndAwayTeamIgnoreCase(String homeTeam, String awayTeam);
    List<FootballTeam> findByHomeTeamContainingIgnoreCase(String partial);
    List<FootballTeam> findByAwayTeamContainingIgnoreCase(String partial);

    @Query("""
           SELECT f FROM FootballTeam f
           WHERE LOWER(f.homeTeam) LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(f.awayTeam)  LIKE LOWER(CONCAT('%', :q, '%'))
           """)
    List<FootballTeam> search(@Param("q") String q);
}