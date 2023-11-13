package cmc.mellyserver.auth.common.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmc.mellyserver.support.exception.ErrorCode;
import cmc.mellyserver.support.exception.LogoutOrWithdrawExpcetion;
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

	  setExpiredErrorResponse(request, response, ex);

	} catch (LogoutOrWithdrawExpcetion ex) {
	  setLogoutOrWithdrawErrorResponse(request, response, ex);
	}
  }

  public void setExpiredErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex)
	  throws IOException {

	response.setContentType("application/json; charset=UTF-8");
	response.setStatus(HttpStatus.OK.value());

	ErrorResponse error = ErrorResponse.of(ErrorCode.EXPIRED_TOKEN);

	final ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getOutputStream(), error);

  }

  public void setLogoutOrWithdrawErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex)
	  throws IOException {

	response.setContentType("application/json; charset=UTF-8");
	response.setStatus(HttpStatus.OK.value());

	ErrorResponse error = ErrorResponse.of(ErrorCode.LOGOUT_WITHDRAW_USER);

	final ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getOutputStream(), error);
  }

}
