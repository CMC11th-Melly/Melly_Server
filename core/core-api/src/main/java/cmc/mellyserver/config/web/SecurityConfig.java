package cmc.mellyserver.config.web;

import cmc.mellyserver.common.filter.JwtExceptionFilter;
import cmc.mellyserver.common.filter.RestAuthenticationEntryPoint;
import cmc.mellyserver.common.filter.TokenAccessDeniedHandler;
import cmc.mellyserver.common.filter.TokenAuthenticationFilter;
import cmc.mellyserver.common.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate redisTemplate;

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    private final TokenAccessDeniedHandler accessDeniedHandler;

    private final JwtExceptionFilter jwtExceptionFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        TokenAuthenticationFilter authenticationFilter = new TokenAuthenticationFilter(jwtTokenProvider, redisTemplate);

        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/actuator/**",
                        "/auth/social/signup", "/api/auth/social", "/api/auth/signup", "/api/auth/login", "/api/auth/token/reissue",
                        "/auth/nickname", "/auth/email", "/api/health", "/api/auth/email-certification/sends", "/api/auth/email-certification/resends",
                        "/api/auth/email-certification/confirms", "/api/auth/social-signup", "/").permitAll()
                .anyRequest().authenticated()
                .and()

                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, TokenAuthenticationFilter.class);

    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}