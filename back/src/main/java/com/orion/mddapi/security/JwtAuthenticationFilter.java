package com.orion.mddapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

/**
 * Filtre exécuté à chaque requête : si l'en-tête {@code Authorization} contient
 * un token JWT valide, l'utilisateur correspondant est placé dans le contexte de
 * sécurité de Spring. Sinon la requête continue sans authentification (et sera
 * rejetée plus loin si la ressource est protégée).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtService.isValid(token)) {
                String username = jwtService.extractUsername(token);
                var auth = new UsernamePasswordAuthenticationToken(username, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Force ce filtre à s'exécuter aussi sur les dispatches d'erreur ({@code /error}).
     * Par défaut Spring ne le fait pas : une erreur serveur serait alors vue comme
     * non authentifiée et renverrait un 401 trompeur au lieu du vrai code d'erreur.
     */
    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}