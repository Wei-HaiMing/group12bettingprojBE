package com.grouptwelve.grouptwelveBE.security;

import com.grouptwelve.grouptwelveBE.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.grouptwelve.grouptwelveBE.model.User;
import com.grouptwelve.grouptwelveBE.repository.UserRepository;
import java.util.Optional;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extract GitHub user information
        Integer githubIdInt = oAuth2User.getAttribute("id");
        Long githubId = githubIdInt != null ? githubIdInt.longValue() : null;
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String login = oAuth2User.getAttribute("login"); // GitHub username
        
        // Use login as name if name is null
        final String finalName = (name == null || name.isEmpty()) ? login : name;
        
        // Save or update user in database
        Optional<User> existingUser = userRepository.findById(githubId);
        User user;
        if (existingUser.isPresent()) {
            // User exists, just use it (don't update to avoid version conflicts)
            user = existingUser.get();
        } else {
            // New user, create and save
            User newUser = new User();
            newUser.setUserId(githubId);
            newUser.setEmail(email);
            newUser.setName(finalName);
            user = userRepository.save(newUser);
        }
        
        // Generate JWT token (convert githubId to String)
        String jwtToken = jwtUtil.createToken(String.valueOf(githubId), email, finalName);
        
        // Get the redirect IP from session (set during OAuth initiation)
        HttpSession session = request.getSession(false);
        String redirectIp = null;
        if (session != null) {
            redirectIp = (String) session.getAttribute("redirect_ip");
            // Clear the attribute after use
            session.removeAttribute("redirect_ip");
        }
        
        String frontendUrl;
        if (redirectIp != null && !redirectIp.isEmpty()) {
            // Development mode: use exp:// with provided IP
            frontendUrl = "exp://" + redirectIp + ":8081/--/(tabs)/logout?token=" + jwtToken;
        } else {
            // Production mode: use custom scheme
            frontendUrl = "myapp://auth/callback?token=" + jwtToken;
        }
        
        System.out.println("Redirecting to: " + frontendUrl);
        response.sendRedirect(frontendUrl);
    }
}
