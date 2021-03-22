/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.security;

import at.adridi.generalmanagement.gmgateway.model.AppUser;
import at.adridi.generalmanagement.gmgateway.repository.AppUserRepository;
import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * Implementation of the JWT authentication process
 *
 * @author A.Dridi
 */
@Getter
@Setter
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private AppUserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AppUserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    /**
     * Check if the request contains the authentication header (that contains
     * String "Bearer" and the authentication token). If this exist, then do the
     * authentication process by checking the passed token in the authentication
     * header.
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    /**
     * Encode the jwt token and return authentication object with the
     * authenticated user that was loaded from the database..
     *
     * @param request
     * @return
     */
    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String jwtToken = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        if (jwtToken != null) {
            String username = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(jwtToken)
                    .getSubject();

            if (username != null) {
                Optional<AppUser> user = userRepository.findByEmail(username);
                if (user.isPresent()) {
                    UserPrincipal principal = new UserPrincipal(user.get());
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, principal.getAuthorities());
                    return auth;
                }
            }
        }
        return null;
    }

}
