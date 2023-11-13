package cmc.mellyserver.auth.repository;

public class RefreshToken {

  private Long userId;

  private String refreshToken;

  public RefreshToken(final String refreshToken, final Long userId) {
	this.refreshToken = refreshToken;
	this.userId = userId;
  }

  public String getRefreshToken() {
	return refreshToken;
  }

  public Long getUserId() {
	return userId;
  }

}