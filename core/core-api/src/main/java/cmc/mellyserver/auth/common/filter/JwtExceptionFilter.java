package cmc.mellyserver.auth.common.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmc.mellyserver.support.exception.ErrorCode;
import cmc.mellyserver.support.exception.LogoutOrWithdrawException;
import cmc.mellyserver.support.response.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JwtException ex) {
            setExpiredErrorResponse(response);
        } catch (LogoutOrWithdrawException ex) {
            setLogoutOrWithdrawErrorResponse(response);
        }
    }

    private void setExpiredErrorResponse(HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());

        ErrorResponse error = ErrorResponse.of(ErrorCode.EXPIRED_TOKEN);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), error);

    }

    private void setLogoutOrWithdrawErrorResponse(HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());

        ErrorResponse error = ErrorResponse.of(ErrorCode.LOGOUT_WITHDRAW_USER);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), error);
    }

}
