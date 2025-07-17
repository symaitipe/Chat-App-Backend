package com.sahan.chatApp.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Autowired
    UserDetailsService userDetailsService;


    // ----------------------- Authentication provider --------------------------------------------
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }


    // ----------------------- customize security filter chain --------------------------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authProvider)
                                                                                                throws Exception {


                http.csrf(AbstractHttpConfigurer::disable);// Disable CSRF for API calls
                http.cors(Customizer.withDefaults()); //Enable CORS
                http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/send-otp",
                                "/api/users/verify-otp",
                                "/api/users/register",
                                "/api/users/login"

                        ).permitAll()
                        .anyRequest().authenticated());
                http.authenticationProvider(authProvider);  // Register custom auth provider
                http.httpBasic(Customizer.withDefaults());
                http.sessionManagement
                        (session->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // ----------------------- customize CORS --------------------------------------------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",  // Frontend on local dev
                "http://localhost:8081" // If Flutter Web runs here
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);  // Only if you use cookies or auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // ----------------------- Authentication Manager  --------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
