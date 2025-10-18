package com.grouptwelve.grouptwelveBE.security;

import com.grouptwelve.grouptwelveBE.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        
        // Redirect to React Native Expo app using deep link
        // Format: exp://localhost:8081/--/auth/callback?token=...
        // For physical device, use: myapp://auth/callback?token=...
        String frontendUrl = "exp://10.11.48.42:8081/--/(tabs)/index?token=" + jwtToken;
        // System.out.println("Redirecting to: " + frontendUrl);
        response.sendRedirect(frontendUrl);
    }
}
