package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filter for JWT authentication.
 * This filter checks for the presence of a JWT token in the request header,
 * validates it, and sets the authentication in the security context.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    try {
      String token = getJwtFromRequest(request);

      if (token != null) {
        String username = jwtTokenProvider.verifyTokenAndGetUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        registerUserAsAuthenticated(request, userDetails);
      }

      filterChain.doFilter(request, response);
    } catch (JwtException | IllegalArgumentException ex) {
      writeJsonError(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
    } catch (UsernameNotFoundException ex) {
      writeJsonError(response, HttpStatus.UNAUTHORIZED, "User not found");
    } catch (Exception ex) {
      System.err.println("An error occurred while running the filter: " + ex.getMessage());
      writeJsonError(response, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the token");
    }
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    final String BEARER_PREFIX = "Bearer ";
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length()); // Remove "Bearer " prefix
    }
    return null;
  }

  private static void registerUserAsAuthenticated(HttpServletRequest request, UserDetails userDetails) {
    final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  private void writeJsonError(HttpServletResponse response, HttpStatus status, String message) {
    try {
    response.setContentType("application/json");
    response.setStatus(status.value());

    ErrorResponse errorResponse = new ErrorResponse(message);
    String json = objectMapper.writeValueAsString(errorResponse);

    response.getWriter().write(json);
    } catch (Exception e) {
      System.err.printf("Error writing JSON error response: %s%n. Original error: %s%n", e.getMessage(), message);
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      try {
        response.getWriter().write("Internal server error");
      } catch (IOException ioException) {
        System.err.println("Error writing internal server error response: " + ioException.getMessage());
      }
    }
  }
}
