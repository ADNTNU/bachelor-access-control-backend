package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) in the application.
 * This class implements the WebMvcConfigurer interface to customize the CORS settings.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("#{'${cors.allowedOrigins}'.split(',')}")
    private List<String> allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] originsArray = allowedOrigins.toArray(new String[0]);
        boolean shouldAllowCredentials = Arrays.stream(originsArray).noneMatch(orig -> orig.equals("*"));

      registry.addMapping("/**")
                .allowedOrigins(originsArray)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(shouldAllowCredentials);
    }
}