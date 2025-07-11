package org._p1m.foodorderingsystem.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf-> csrf.disable()).
                authorizeHttpRequests(auth->auth
                        .requestMatchers(new AntPathRequestMatcher("/${api.base.path}/auth/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
  }
}
