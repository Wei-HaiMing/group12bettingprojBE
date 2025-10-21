package com.grouptwelve.grouptwelveBE.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grouptwelve.grouptwelveBE.model.FavoriteTeam;
import com.grouptwelve.grouptwelveBE.model.Game;
import com.grouptwelve.grouptwelveBE.model.User;
import com.grouptwelve.grouptwelveBE.repository.FavoriteTeamRepository;
import com.grouptwelve.grouptwelveBE.repository.GameRepository;
import com.grouptwelve.grouptwelveBE.repository.UserRepository;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteTeamRepository favoriteTeamRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void testGetAllUsers() throws Exception {
        User user = new User("Test User", "test@example.com", "password");
        userRepository.save(user);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void testCreateUser() throws Exception {
        User newUser = new User("New User", "new@example.com", "newpassword");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User("Test User", "test@example.com", "password");
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/users/" + savedUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }



        // Tests for Games 
        // Backend routes (Games) - helper method to create a new game object
        private Game newGame(String home, String away) {
        Game g = new Game();
        g.setLeague("NFL");
        g.setHomeTeam(home);
        g.setAwayTeam(away);
        g.setStatus("scheduled");
        g.setStartTime(java.time.LocalDateTime.parse("2025-11-10T13:00:00"));
        g.setOddsHome(new java.math.BigDecimal("1.80"));
        g.setOddsAway(new java.math.BigDecimal("2.10"));
        return g;
    }

        @Test
    void testCreateGame() throws Exception {
        Game body = newGame("Kansas City Privates", "New England Partisans");

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.league").value("NFL"))
            .andExpect(jsonPath("$.homeTeam").value("Kansas City Privates"))
            .andExpect(jsonPath("$.awayTeam").value("New England Partisans"))
            .andExpect(jsonPath("$.status").value("scheduled"))
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testListGamesAndFilterByStatus() throws Exception {
        // seed
        Game g1 = gameRepository.save(newGame("Buffalo Beels", "Miami Dullfins"));            // scheduled
        Game g2 = newGame("Chicago Beers", "Green Bay Smackers"); g2.setStatus("completed");
        gameRepository.save(g2);

        // list all
        mockMvc.perform(get("/api/games"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2));

        // filter by status=completed
        mockMvc.perform(get("/api/games").param("status", "completed"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].status").value("completed"));
    }

    @Test
    void testGetGameById() throws Exception {
        Game saved = gameRepository.save(newGame("San Francisco Forty-Viners", "Los Angeles Rammers"));

        mockMvc.perform(get("/api/games/" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.homeTeam").value("San Francisco Forty-Viners"))
            .andExpect(jsonPath("$.awayTeam").value("Los Angeles Rammers"));
    }

    @Test
    void testUpdateGameCoreFields() throws Exception {
        Game saved = gameRepository.save(newGame("Dallas Cowpokes", "New York Dines"));

        Game update = new Game();
        update.setStatus("in_progress");
        update.setOddsHome(new java.math.BigDecimal("1.75"));

        mockMvc.perform(put("/api/games/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("in_progress"))
            .andExpect(jsonPath("$.oddsHome").value(1.75));
    }

    @Test
    void testUpdateOddsOnly() throws Exception {
        Game saved = gameRepository.save(newGame("Quahog Griffins", "Springfield Isotopes"));

        Game update = new Game();
        update.setOddsHome(new java.math.BigDecimal("1.70"));
        update.setOddsAway(new java.math.BigDecimal("2.15"));

        mockMvc.perform(put("/api/games/{id}/odds", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.oddsHome").value(1.70))
            .andExpect(jsonPath("$.oddsAway").value(2.15));
    }

    @Test
    void testDeleteGameById() throws Exception {
        Game saved = gameRepository.save(newGame("Gotham Bats", "Metropolis Men"));

        mockMvc.perform(delete("/api/games/{id}", saved.getId()))
            .andExpect(status().isOk());

        // verify gone (controller returns null -> empty response)
        mockMvc.perform(get("/api/games/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void testBulkCreateAndBulkDeleteByStatus() throws Exception {
        // bulk create via controller
        java.util.List<Game> payload = java.util.Arrays.asList(
            newGame("Oakland Raid-ers", "San Diego Super-Chargers"),
            newGame("Cleveland Brownies", "Pittsburgh Stillers")
        );

        mockMvc.perform(post("/api/games/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2));

        // bulk delete by status=scheduled (both are scheduled)
        mockMvc.perform(delete("/api/games").param("status", "scheduled"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.anyOf(
                org.hamcrest.Matchers.equalTo("2"),
                org.hamcrest.Matchers.equalTo("2L")
            )));
    }

 // end of Games tests



    @Test
    void testGetAllFavoriteTeams() throws Exception {
        FavoriteTeam favoriteTeam = new FavoriteTeam(1L, 2L);
        favoriteTeamRepository.save(favoriteTeam);

        mockMvc.perform(get("/api/favoriteteams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].teamId").value(2L));
    }

    @Test
    void testGetFavoriteTeamsByUserId() throws Exception {
        FavoriteTeam favoriteTeam1 = new FavoriteTeam(1L, 2L);
        FavoriteTeam favoriteTeam2 = new FavoriteTeam(1L, 3L);
        favoriteTeamRepository.save(favoriteTeam1);
        favoriteTeamRepository.save(favoriteTeam2);

        mockMvc.perform(get("/api/favoriteteams/user/" + favoriteTeam1.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].teamId").value(2L))
                .andExpect(jsonPath("$[1].userId").value(1L))
                .andExpect(jsonPath("$[1].teamId").value(3L));
    }

    @Test
    void testGetFavoriteTeamByUserIdAndTeamId() throws Exception {
        FavoriteTeam favoriteTeam = new FavoriteTeam(1L, 2L);
        favoriteTeamRepository.save(favoriteTeam);

        mockMvc.perform(get("/api/favoriteteams/user/" + favoriteTeam.getUserId() + "/team/" + favoriteTeam.getTeamId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.teamId").value(2L));
    }

    @Test
    void testAddFavoriteTeamByUserIdAndTeamId() throws Exception {
        Long userId = 1L;
        Long teamId = 2L;

        mockMvc.perform(post("/api/favoriteteams/user/" + userId + "/team/" + teamId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.teamId").value(teamId));
    }

    @Test
    void testUpdateFavoriteTeam() throws Exception {
        FavoriteTeam favoriteTeam = new FavoriteTeam(1L, 2L);
        FavoriteTeam savedFavoriteTeam = favoriteTeamRepository.save(favoriteTeam);

        FavoriteTeam updatedFavoriteTeam = new FavoriteTeam(3L, 4L);

        mockMvc.perform(put("/api/favoriteteams/user/" + savedFavoriteTeam.getUserId() + "/team/" + savedFavoriteTeam.getTeamId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFavoriteTeam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(3L))
                .andExpect(jsonPath("$.teamId").value(4L));
    }

    @Test
    void testDeleteFavoriteTeamByUserIdAndTeamId() throws Exception {
        FavoriteTeam favoriteTeam = new FavoriteTeam(1L, 2L);
        favoriteTeamRepository.save(favoriteTeam);

        // Verify it exists first
        mockMvc.perform(get("/api/favoriteteams/user/" + favoriteTeam.getUserId() + "/team/" + favoriteTeam.getTeamId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.teamId").value(2L));

        // Delete it
        mockMvc.perform(delete("/api/favoriteteams/user/" + favoriteTeam.getUserId() + "/team/" + favoriteTeam.getTeamId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("deleted")));

        // Verify it's gone (should return null or 404)
        mockMvc.perform(get("/api/favoriteteams/user/" + favoriteTeam.getUserId() + "/team/" + favoriteTeam.getTeamId()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testDeleteFavoriteTeamById() throws Exception {
        FavoriteTeam favoriteTeam = new FavoriteTeam(1L, 2L);
        FavoriteTeam savedFavoriteTeam = favoriteTeamRepository.save(favoriteTeam);

        // Verify it exists first
        mockMvc.perform(get("/api/favoriteteams/user/" + savedFavoriteTeam.getUserId() + "/team/" + savedFavoriteTeam.getTeamId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.teamId").value(2L));

        // Delete it by ID
        mockMvc.perform(delete("/api/favoriteteams/" + savedFavoriteTeam.getFavoriteTeamId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("deleted")));

        // Verify it's gone (should return null or 404)
        mockMvc.perform(get("/api/favoriteteams/user/" + savedFavoriteTeam.getUserId() + "/team/" + savedFavoriteTeam.getTeamId()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}