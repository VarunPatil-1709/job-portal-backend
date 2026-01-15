package com.JobPortalJobService.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

                // 🌍 PUBLIC
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/jobs", "/jobs/*").permitAll()

                // 🔗 INTERNAL (chat / job-service)
                .requestMatchers("/internal/**").authenticated()

                // 🎓 STUDENT
                .requestMatchers(HttpMethod.POST, "/jobs/*/apply").hasRole("STUDENT")
                .requestMatchers(HttpMethod.GET, "/applications/my").hasRole("STUDENT")
                .requestMatchers("/my/job/*").hasRole("STUDENT")

                // 🧑‍💼 RECRUITER
                .requestMatchers(HttpMethod.POST, "/jobs").hasRole("RECRUITER")
                .requestMatchers(HttpMethod.GET, "/jobs/my").hasRole("RECRUITER")
                .requestMatchers(HttpMethod.GET, "/jobs/*/applications").hasRole("RECRUITER")
                .requestMatchers(HttpMethod.POST, "/jobs/*/chat-session").hasRole("RECRUITER")



                // 📊 STATS / STATUS
                .requestMatchers(HttpMethod.GET, "/application/status").permitAll()
                .requestMatchers(HttpMethod.GET, "/stats").permitAll()

                // 🔐 MUST BE LAST
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
