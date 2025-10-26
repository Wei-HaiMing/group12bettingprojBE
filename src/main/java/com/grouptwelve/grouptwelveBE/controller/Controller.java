package com.grouptwelve.grouptwelveBE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grouptwelve.grouptwelveBE.model.FavoriteTeam;
import com.grouptwelve.grouptwelveBE.model.Game;
import com.grouptwelve.grouptwelveBE.model.Player;
import com.grouptwelve.grouptwelveBE.model.User;
import com.grouptwelve.grouptwelveBE.repository.FavoriteTeamRepository;
import com.grouptwelve.grouptwelveBE.repository.GameRepository;
import com.grouptwelve.grouptwelveBE.repository.PlayerRepository;
import com.grouptwelve.grouptwelveBE.repository.UserRepository;


// keep all controllers in this file for simplicity

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteTeamRepository favoriteTeamRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;


    @GetMapping("/")
    public String root() {
        return "API up. Try /api/greeting";
    }

    @GetMapping("/greeting")
    public String hello(@RequestParam(defaultValue = "world") String name) {
        return "Greetings, " + name + "!";
    }

    // User CRUD operations BEGIN
    @GetMapping("/users") // GET 1
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}") // GET 2
    public User getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/users") // POST 1
    public User createUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @PatchMapping("/users/{id}/name") // PATCH 1
    public User updateName(@PathVariable("id") Long id, @RequestBody String newName) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(newName);
            return userRepository.save(user);
        }
        return null;
    }

    @PatchMapping("/users/{id}/password") // PATCH 2
    public User updatePassword(@PathVariable("id") Long id, @RequestBody String newPassword) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setPassword(newPassword);
            return userRepository.save(user);
        }
        return null;
    }


    //GAMES (8 roues) 
    // POST (1): create one game record 
    @PostMapping("/games")
    public Game createGame(@RequestBody Game g) {
        if (g.getLeague() == null || g.getLeague().isBlank()) g.setLeague("NFL");
        if (g.getStatus() == null || g.getStatus().isBlank()) g.setStatus("scheduled");
        return gameRepository.save(g);
    }

    // POST (2): bulk create multiple games at once
    @PostMapping("/games/bulk")
    public List<Game> bulkCreateGames(@RequestBody List<Game> games) {
        for (Game g : games) {
            if (g.getLeague() == null || g.getLeague().isBlank()) g.setLeague("NFL");
            if (g.getStatus() == null || g.getStatus().isBlank()) g.setStatus("scheduled");
        }
        return gameRepository.saveAll(games);
    }

    // GET (1): list (optional status) list all games or by status
    @GetMapping("/games")
    public List<Game> listGames(@RequestParam(required = false) String status) {
        return (status == null || status.isBlank())
                ? gameRepository.findAll()
                : gameRepository.findByStatus(status);
    }

    // GET (2): one by id
    
    // Player (8 routes)
    // GET /players - lists all players (optional filters: team, position)
    @GetMapping("/players")
    public List<Player> listPlayers(@RequestParam(required = false) String team,
                                    @RequestParam(required = false) String position) {
        boolean noTeam = (team == null || team.isBlank());
        boolean noPos = (position == null || position.isBlank());
        if (noTeam && noPos) return playerRepository.findAll();
        if (!noTeam && noPos) return playerRepository.findByTeam(team);
        if (noTeam && !noPos) return playerRepository.findByPosition(position);
        // both provided: filter team result by position
        List<Player> byTeam = playerRepository.findByTeam(team);
        return byTeam.stream().filter(p -> p.getPosition() != null && position.equalsIgnoreCase(p.getPosition())).toList();
    }

    // GET /players/{id} - get player by id
    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable("id") Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    // GET /players/position/{position} - list players by position
    @GetMapping("/players/position/{position}")
    public List<Player> listPlayersByPosition(@PathVariable("position") String position) {
        return playerRepository.findByPosition(position);
    }

    // GET /players/team/{team} - list players by team
    @GetMapping("/players/team/{team}")
    public List<Player> listPlayersByTeam(@PathVariable("team") String team) {
        return playerRepository.findByTeam(team);
    }

    // GET /players/search?q=... - search by name or team (case-insensitive contains)
    @GetMapping("/players/search")
    public List<Player> searchPlayers(@RequestParam("q") String q) {
        if (q == null || q.isBlank()) return List.of();
        String qLower = q.toLowerCase();
        return playerRepository.findAll().stream()
                .filter(p -> (p.getName() != null && p.getName().toLowerCase().contains(qLower))
                        || (p.getTeam() != null && p.getTeam().toLowerCase().contains(qLower)))
                .toList();
    }

    // POST /players - create new player
    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player p) {
        return playerRepository.save(p);
    }

    // PUT /players/{id} - update full player record (partial-update semantics)
    @PutMapping("/players/{id}")
    public Player updatePlayer(@PathVariable("id") Long id, @RequestBody Player u) {
        return playerRepository.findById(id).map(existing -> {
            if (u.getName() != null) existing.setName(u.getName());
            if (u.getTeam() != null) existing.setTeam(u.getTeam());
            if (u.getPosition() != null) existing.setPosition(u.getPosition());
            if (u.getJerseyNumber() != 0) existing.setJerseyNumber(u.getJerseyNumber());
            if (u.getTouchdowns() != 0) existing.setTouchdowns(u.getTouchdowns());
            if (u.getTotalYards() != 0) existing.setTotalYards(u.getTotalYards());
            if (u.getGamesPlayed() != 0) existing.setGamesPlayed(u.getGamesPlayed());
            if (u.getFieldGoals() != 0) existing.setFieldGoals(u.getFieldGoals());
            if (u.getCompletionRate() != 0.0) existing.setCompletionRate(u.getCompletionRate());
            return playerRepository.save(existing);
        }).orElse(null);
    }

    // DELETE /players/{id} - delete player by ID
    @DeleteMapping("/players/{id}")
    public String deletePlayer(@PathVariable("id") Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return "Player with id " + id + " deleted.";
        } else {
            return "Player with id " + id + " not found.";
        }
    }
    @GetMapping("/favoriteteams/user/{userId}/team/{teamId}") // GET 3
    public FavoriteTeam getFavoriteTeamByUserIdAndTeamId(@PathVariable("userId") Long userId, @PathVariable("teamId") Long teamId) {
        return favoriteTeamRepository.findByUserIdAndTeamId(userId, teamId).orElse(null);
    }

    @PostMapping("/favoriteteams/user/{userId}/team/{teamId}") // POST 1
    public FavoriteTeam addFavoriteTeamByUserIdAndTeamId(@PathVariable("userId") Long userId, @PathVariable("teamId") Long teamId) {
        FavoriteTeam favoriteTeam = new FavoriteTeam();
        favoriteTeam.setUserId(userId);
        favoriteTeam.setTeamId(teamId);
        return favoriteTeamRepository.save(favoriteTeam);
    }

    @PutMapping("/favoriteteams/user/{userId}/team/{teamId}") // PUT 1
    public FavoriteTeam updateFavoriteTeam(@PathVariable("userId") Long userId, @PathVariable("teamId") Long teamId, @RequestBody FavoriteTeam updatedFavoriteTeam) {
        FavoriteTeam existing = favoriteTeamRepository.findByUserIdAndTeamId(userId, teamId).orElse(null);
        if (existing != null) {
            // update fields we allow to change
            existing.setUserId(updatedFavoriteTeam.getUserId());
            existing.setTeamId(updatedFavoriteTeam.getTeamId());
            return favoriteTeamRepository.save(existing);
        }
        return null;
    }

    @DeleteMapping("/favoriteteams/user/{userId}/team/{teamId}") // DELETE 1
    public String deleteFavoriteTeamByUserIdAndTeamId(@PathVariable("userId") Long userId, @PathVariable("teamId") Long teamId) {
        if (favoriteTeamRepository.existsByUserIdAndTeamId(userId, teamId)) {
            favoriteTeamRepository.deleteByUserIdAndTeamId(userId, teamId);
            return "FavoriteTeam with userId " + userId + " and teamId " + teamId + " deleted.";
        } else {
            return "FavoriteTeam with userId " + userId + " and teamId " + teamId + " not found.";
        }
    }

    @DeleteMapping("/favoriteteams/{id}") // DELETE 2
    public String deleteFavoriteTeamById(@PathVariable("id") Long id) {
        if (favoriteTeamRepository.existsById(id)) {
            favoriteTeamRepository.deleteById(id);
            return "FavoriteTeam with id " + id + " deleted.";
        } else {
            return "FavoriteTeam with id " + id + " not found.";
        }
    }
    // FavoriteTeam CRUD operations END

}
