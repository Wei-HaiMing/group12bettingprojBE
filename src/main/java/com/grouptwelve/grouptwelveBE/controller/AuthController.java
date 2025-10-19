package com.grouptwelve.grouptwelveBE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grouptwelve.grouptwelveBE.model.User;
import com.grouptwelve.grouptwelveBE.repository.UserRepository;
import com.grouptwelve.grouptwelveBE.model.FavoriteTeam;
import com.grouptwelve.grouptwelveBE.repository.FavoriteTeamRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/start")
    public String authStart() {
        return "https://bettingprojheroku-0f16500feb98.herokuapp.com/oauth2/authorization/github";
    }

    // @GetMapping("/callback")
    // public String authCallback() {
    //     return "OAuth2 callback endpoint hit.";
    // }
}
