package com.grouptwelve.grouptwelveBE.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.grouptwelve.grouptwelveBE.model.FootballTeam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class FootballTeamRepositoryTest {

    @Autowired
    private FootballTeamRepository footballTeamRepository;

    private FootballTeam makeTeam(String home, String away) {
        FootballTeam f = new FootballTeam();
        f.setHomeTeam(home);
        f.setAwayTeam(away);
        
        return f;
    }

    @Test
    void testSaveAndFindByHomeTeamIgnoreCase() {
        FootballTeam t = makeTeam("San Francisco 49ers", "Dallas Cowboys");
        FootballTeam saved = footballTeamRepository.save(t);

        assertNotNull(saved); 

        List<FootballTeam> found = footballTeamRepository.findByHomeTeamIgnoreCase("SAN FRANCISCO 49ERS");
        assertEquals(1, found.size());
        assertEquals("Dallas Cowboys", found.get(0).getAwayTeam());
        footballTeamRepository.deleteAll(found);
        assertTrue(footballTeamRepository.findByHomeTeamIgnoreCase("san francisco 49ers").isEmpty());
    }

    @Test
    void testFindByAwayTeamIgnoreCase() {
        footballTeamRepository.save(makeTeam("Kansas City Chiefs", "Las Vegas Raiders"));

        List<FootballTeam> found = footballTeamRepository.findByAwayTeamIgnoreCase("las vegas raiders");
        assertEquals(1, found.size());
        assertEquals("Kansas City Chiefs", found.get(0).getHomeTeam());
    }

    @Test
    void testFindByHomeAndAwayTeamIgnoreCase() {
        footballTeamRepository.save(makeTeam("Los Angeles Chargers", "Seattle Seahawks"));
        Optional<FootballTeam> found = footballTeamRepository.findByHomeTeamIgnoreCaseAndAwayTeamIgnoreCase("los angeles chargers", "SEATTLE SEAHAWKS");
        assertTrue(found.isPresent());
        assertEquals("Los Angeles Chargers", found.get().getHomeTeam());
        assertEquals("Seattle Seahawks", found.get().getAwayTeam());
    }

    @Test
    void testFindByHomeTeamContainingIgnoreCase() {
        footballTeamRepository.save(makeTeam("Green Bay Packers", "Chicago Bears"));
        footballTeamRepository.save(makeTeam("New England Patriots", "New York Jets"));
        List<FootballTeam> partial = footballTeamRepository.findByHomeTeamContainingIgnoreCase("new");
        assertEquals(1, partial.size());
        assertEquals("New England Patriots", partial.get(0).getHomeTeam());
    }

    @Test
    void testFindByAwayTeamContainingIgnoreCase() {
        footballTeamRepository.save(makeTeam("Miami Dolphins", "Buffalo Bills"));
        footballTeamRepository.save(makeTeam("Philadelphia Eagles", "New York Giants"));
        List<FootballTeam> partial = footballTeamRepository.findByAwayTeamContainingIgnoreCase("new");
        assertEquals(1, partial.size());
        assertEquals("New York Giants", partial.get(0).getAwayTeam());
    }

    @Test
    void testSearchByHomeOrAway() {
        footballTeamRepository.save(makeTeam("Baltimore Ravens", "Cincinnati Bengals"));
        footballTeamRepository.save(makeTeam("Pittsburgh Steelers", "Cleveland Browns"));
        List<FootballTeam> q1 = footballTeamRepository.search("bengals");
        assertEquals(1, q1.size());
        assertEquals("Cincinnati Bengals", q1.get(0).getAwayTeam());
        List<FootballTeam> q2 = footballTeamRepository.search("pittsburgh");
        assertEquals(1, q2.size());
        assertEquals("Pittsburgh Steelers", q2.get(0).getHomeTeam());
    }

    @Test
    void testFindAllAndDelete() {
        FootballTeam t1 = footballTeamRepository.save(makeTeam("Atlanta Falcons", "Carolina Panthers"));
        FootballTeam t2 = footballTeamRepository.save(makeTeam("Tampa Bay Buccaneers", "New Orleans Saints"));
        assertEquals(2, footballTeamRepository.findAll().size());
        footballTeamRepository.delete(t1);
        assertEquals(1, footballTeamRepository.findAll().size());
        footballTeamRepository.delete(t2);
        assertEquals(0, footballTeamRepository.findAll().size());
    }
}
