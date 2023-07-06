package cmc.mellyserver.mellyapi.config;

import cmc.mellyserver.mellyapi.common.filter.JwtExceptionFilter;
import cmc.mellyserver.mellyapi.common.filter.TokenAuthenticationFilter;
import cmc.mellyserver.mellyapi.common.handler.RestAuthenticationEntryPoint;
import cmc.mellyserver.mellyapi.common.handler.TokenAccessDeniedHandler;
import cmc.mellyserver.mellyapi.common.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final TokenAccessDeniedHandler accessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger*/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        TokenAuthenticationFilter authenticationFilter = new TokenAuthenticationFilter(jwtTokenProvider);

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
                .antMatchers("/actuator/**", "/v3/api-docs/**", "/api/memory/**", "/api/place/list", "/api/imageTest",
                        "/api/pw", "/api/optional", "/auth/social/signup", "/auth/social", "/auth/signup", "/auth/login",
                        "/auth/nickname", "/auth/email", "/api/health", "/").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, TokenAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}