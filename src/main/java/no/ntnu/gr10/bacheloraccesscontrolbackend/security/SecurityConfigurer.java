package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


/**
 * Security configuration class for setting up authentication and authorization.
 * This class configures the security filter chain, authentication manager, and password encoder.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfigurer {

  @Value("#{'${cors.allowedOrigins}'.split(',')}")
  private List<String> allowedOrigins;

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
    return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
  }

  /**
   * Configures CORS (Cross-Origin Resource Sharing) settings.
   * This method sets up the allowed origins, methods, and headers for CORS requests.
   *
   * @return a CorsConfigurationSource instance.
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(allowedOrigins);  // List of allowed origins
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Allowed HTTP methods
    config.setAllowedHeaders(List.of("*"));
    boolean shouldAllowCredentials = allowedOrigins.stream().noneMatch(orig -> orig.equals("*"));
    config.setAllowCredentials(shouldAllowCredentials);  // Allow credentials (cookies, authorization headers)

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);  // Apply CORS configuration to all paths

    return source;
  }

  /**
   * Configures the security filter chain for HTTP requests.
   * @param httpSecurity The HttpSecurity object to configure.
   * @return a SecurityFilterChain instance.
   * @throws Exception Throws Exception if there's an error during configuration.
   */
  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/administrator/register-from-invite").permitAll()
                    .requestMatchers("/administrator/accept-invite").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }

  /**
   * Configures the authentication manager for user authentication.
   * Uses the provided UserDetailsService and PasswordEncoder defined with Bean methods.
   *
   * @param http The HttpSecurity object to configure.
   * @param userDetailsService The UserDetailsService for loading user details.
   * @param passwordEncoder The PasswordEncoder for encoding passwords.
   * @return an AuthenticationManager instance.
   * @throws Exception Throws Exception if there's an error during configuration.
   */
  @Bean
  public AuthenticationManager authenticationManager(
          HttpSecurity http,
          UserDetailsService userDetailsService,
          PasswordEncoder passwordEncoder) throws Exception {

    AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
    builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    return builder.build();
  }

  /**
   * Password encoder bean to hash passwords securely.
   *
   * @return BCryptPasswordEncoder instance for password encoding.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
