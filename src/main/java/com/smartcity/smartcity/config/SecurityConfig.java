package com.smartcity.smartcity.config;

import com.smartcity.smartcity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        HttpSessionRequestCache requestCache =
            new HttpSessionRequestCache();

        http
            // ── 1. Disable CSRF ───────────────────────────
            .csrf(csrf -> csrf.disable())

            // ── 2. Content Security Policy ────────────────
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline' " +
                            "https://cdnjs.cloudflare.com " +
                            "https://unpkg.com; " +
                        "style-src 'self' 'unsafe-inline' " +
                            "https://cdnjs.cloudflare.com " +
                            "https://unpkg.com " +
                            "https://fonts.googleapis.com; " +
                        "font-src 'self' " +
                            "https://fonts.gstatic.com; " +
                        "img-src 'self' data: blob: " +
                            "https://*.tile.openstreetmap.org " +
                            "https://*.basemaps.cartocdn.com " +
                            "https://raw.githubusercontent.com " +
                            "https://cdnjs.cloudflare.com " +
                            "https://unpkg.com; " +
                        "connect-src 'self' " +
                            "https://*.tile.openstreetmap.org " +
                            "https://*.basemaps.cartocdn.com;"
                    )
                )
            )

            // ── 3. Request Cache ──────────────────────────
            .requestCache(cache -> cache
                .requestCache(requestCache)
            )

            // ── 4. URL Permissions ────────────────────────
            .authorizeHttpRequests(auth -> auth

                // Public pages
                .requestMatchers(
                    "/",
                    "/home",
                    "/hash",           // password generator
                    "/map",
                    "/map/**",
                    "/history",
                    "/news",
                    "/news/**",
                    "/business",
                    "/business/**",
                    "/search",
                    "/forum",
                    "/forum/**",
                    "/auth/**",
                    "/login",
                    "/error",
                    "/error/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**",
                    "/static/**"
                ).permitAll()

                // Admin only
                .requestMatchers(
                    "/admin",
                    "/admin/**"
                ).hasAuthority("ROLE_ADMIN")

                // Logged in users
                .requestMatchers(
                    "/paid/**",
                    "/profile/**"
                ).authenticated()

                // Everything else
                .anyRequest().permitAll()
            )

            // ── 5. Login ──────────────────────────────────
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/auth/login?error")
                .permitAll()
            )

            // ── 6. Logout ─────────────────────────────────
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // ── 7. Access Denied ──────────────────────────
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            )

            // ── 8. Session Management ─────────────────────
            .sessionManagement(session -> session
                .maximumSessions(5)
                .maxSessionsPreventsLogin(false)
            )

            // ── 9. User Details ───────────────────────────
            .userDetailsService(userService);

        return http.build();
    }
}