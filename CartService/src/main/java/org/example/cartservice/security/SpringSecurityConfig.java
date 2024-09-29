package org.example.cartservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/cart").permitAll()
//                        .requestMatchers("/webhooks/stripe/").permitAll()
                        .anyRequest().authenticated()

                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                        )

                );
        return http.build();


    }


//    @Bean
//    public JwtDecoder jwtDecoder() {
//
//        JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(issuerUri).build();
//
//        return new JwtDecoder() {
//            @Override
//            public Jwt decode(String token) throws JwtException {
//                System.out.println("token: " + token);
//                Jwt jwt = jwtDecoder.decode(token);
//                System.out.println("jwt: " + jwt);
//                return jwt;
//            }
//        };
//    }
}