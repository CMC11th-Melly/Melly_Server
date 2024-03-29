package cmc.mellyserver.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cmc.mellyserver.auth.common.filter.JwtExceptionFilter;
import cmc.mellyserver.auth.common.filter.TokenAuthenticationFilter;
import cmc.mellyserver.auth.common.handler.RestAuthenticationEntryPoint;
import cmc.mellyserver.auth.common.handler.TokenAccessDeniedHandler;
import cmc.mellyserver.auth.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate redisTemplate;

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    private final TokenAccessDeniedHandler accessDeniedHandler;

    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        TokenAuthenticationFilter authenticationFilter = new TokenAuthenticationFilter(jwtTokenProvider, redisTemplate);

        httpSecurity.csrf(CsrfConfigurer::disable)
            .cors(CorsConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .sessionManagement(
                (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(
                (authenticationManager) -> authenticationManager.authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
            .authorizeHttpRequests(authorize -> authorize.requestMatchers(WhiteList.WHITE_LIST)
                .permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            )
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, TokenAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

}