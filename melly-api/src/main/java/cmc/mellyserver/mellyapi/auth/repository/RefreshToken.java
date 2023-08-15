package cmc.mellyserver.mellyapi.auth.repository;

public class RefreshToken {

    private Long userId; // 분명이 구성이 맞다.

    private String refreshToken; // 이 구성이 맞는 것 같아


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