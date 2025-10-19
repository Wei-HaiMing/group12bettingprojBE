package com.grouptwelve.grouptwelveBE.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * OAuth start endpoint that accepts IP address parameter
     * Usage: /auth/start?redirect_ip=192.168.1.5
     */
    @GetMapping("/start")
    public String authStart(
            @RequestParam(value = "redirect_ip", required = false) String redirect_ip,
            HttpServletRequest request) {
        
        // Store the redirect IP in session so it can be retrieved after OAuth callback
        if (redirect_ip != null && !redirect_ip.isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("redirect_ip", redirect_ip);
            System.out.println("Stored redirect_ip in session: " + redirect_ip);
        }

        return "{\"url\": \"https://bettingprojheroku-0f16500feb98.herokuapp.com/oauth2/authorization/github\"}";
    }

    // @GetMapping("/callback")
    // public String authCallback() {
    //     return "OAuth2 callback endpoint hit.";
    // }
}
