package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter for JWT authentication.
 * This filter checks for the presence of a JWT token in the request header,
 * validates it, and sets the authentication in the security context.
 *
 * @author Anders Lund and Daniel Neset
 * @version 07.04.2025
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Constructor for JwtAuthenticationFilter.
   *
   * @param jwtTokenProvider   the JWT token provider for generating and validating tokens
   * @param userDetailsService the service for loading user details
   */
  @Autowired
  public JwtAuthenticationFilter(
          JwtTokenProvider jwtTokenProvider,
          UserDetailsService userDetailsService
  ) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Filters incoming requests to process the JWT token if present.
   * If a valid JWT is found, it extracts the user details from it and registers
   * the authentication in the SecurityContext.
   *
   * @param request     the HTTP request
   * @param response    the HTTP response
   * @param filterChain the filter chain
   */
  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) {
    try {
      String token = getJwtFromRequest(request);

      if (token != null) {
        String username = jwtTokenProvider.verifyAccessTokenAndGetUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        registerUserAsAuthenticated(request, userDetails);
      }

      filterChain.doFilter(request, response);
    } catch (JwtException | IllegalArgumentException ex) {
      writeJsonError(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
    } catch (UsernameNotFoundException ex) {
      writeJsonError(response, HttpStatus.UNAUTHORIZED, "User not found");
    } catch (Exception ex) {
      logger.error("An error occurred while running the filter: " + ex.getMessage());
      writeJsonError(
              response,
              HttpStatus.INTERNAL_SERVER_ERROR,
              "An error occurred while processing the token"
      );
    }
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    final String bearerPrefix = "Bearer ";
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (bearerToken != null && bearerToken.startsWith(bearerPrefix)) {
      return bearerToken.substring(bearerPrefix.length()); // Remove "Bearer " prefix
    }
    return null;
  }

  private static void registerUserAsAuthenticated(
          HttpServletRequest request,
          UserDetails userDetails) {
    final UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
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
      logger.error(
              String.format(
                      "Error writing JSON error response: %s%n. Original error: %s%n",
                      e.getMessage(),
                      message
              )
      );
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      try {
        response.getWriter().write("Internal server error");
      } catch (IOException ioException) {
        logger.error("Error writing internal server error response: " + ioException.getMessage());
      }
    }
  }
}
