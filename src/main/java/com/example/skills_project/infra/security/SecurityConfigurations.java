package com.example.skills_project.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/skill/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/user_skills/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user_skills/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/skill/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/skill").permitAll()
                        .requestMatchers(HttpMethod.POST, "/skill/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user_skills").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/questions/user_skill/{skillId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user_skills").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/questions/{skillId}/question").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/questions/question/{questionId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/questions/{skillId}/question").permitAll()
                        .requestMatchers(HttpMethod.POST, "/questions/{skillId}/validate").permitAll()
                        .requestMatchers(HttpMethod.GET, "/questions/skill/{skillId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/skills/by-category/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/skills/search").permitAll()


                        .requestMatchers(
                                "/configuration/ui",
                                "/configuration/security",
                                "/v2/api-docs",
                                "/v3/api-docs/swagger-config",
                                "/v3/api-docs",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest() .authenticated()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
