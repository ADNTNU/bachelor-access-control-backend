package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  public SecurityConfigurer(UserDetailsServiceImpl userDetailsService,
                            JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  /**
   * Configures the authentication manager builder to use a custom user details service for authentication.
   *
   * @param auth The AuthenticationManagerBuilder to set up the authentication provider.
   * @throws Exception Throws Exception if there's an error during configuration.
   */
  @Autowired
  protected void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

//  @Bean
//  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//    CorsConfiguration config = new CorsConfiguration();
//    config.setAllowedOrigins(allowedOrigins);  // List of allowed origins
//    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Allowed HTTP methods
//    config.setAllowedHeaders(List.of("*"));
//    boolean shouldAllowCredentials = allowedOrigins.stream().noneMatch(orig -> orig.equals("*"));
//    config.setAllowCredentials(shouldAllowCredentials);  // Allow credentials (cookies, authorization headers)
//
//    source.registerCorsConfiguration("/**", config);  // Apply CORS configuration to all paths
//
//    return source;
//  }

  /**
   * Configures the security filter chain for HTTP requests.
   * @param httpSecurity The HttpSecurity object to configure.
   * @return a SecurityFilterChain instance.
   * @throws Exception Throws Exception if there's an error during configuration.
   */
  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
//    TODO: Add CORS and/or CSRF configuration if needed
    httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
//            .cors(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }

  /**
   * Provides the authentication manager bean from authentication configuration.
   *
   * @param config The authenticationConfiguration to retrieve the authentication manager.
   * @return an AuthenticationManager instance.
   * @throws Exception Throws Exception if there's an error retrieving the authentication manager.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Password encoder bean to hash passwords securely.
   *
   * @return a BCryptPasswordEncoder instance.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
