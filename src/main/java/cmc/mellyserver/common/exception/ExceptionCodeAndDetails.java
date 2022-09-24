package cmc.mellyserver.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCodeAndDetails {


    NOT_FOUND_ERROR_CODE("0001", "발생한 에러의 에러코드를 찾을 수 없습니다."),
    NOT_FOUND_API("0002", "해당 경로에 대한 응답 API를 찾을 수 없습니다."),
    BAD_REQUEST("0003", "잘못된 요청입니다."),

    // 인증 관련 예외
    DUPLICATE_EMAIL("1000","중복되는 이메일입니다."),
    DUPLICATE_NICKNAME("1002","중복되는 닉네임입니다."),
    INVALID_EMAIL("1001","이메일이 일치하지 않습니다."),
    INVALID_PASSWORD("1003","비밀번호가 일치하지 않습니다."),
    NO_SUCH_USER("1004","해당 id의 유저가 없습니다."),
    UNAUTHORIZED_USER("1005", "인증되지 않은 유저입니다."),
    FORBIDDEN_USER("1006","접근 권한이 없는 유저입니다."),
    EXPIRED_TOKEN("1007", "JWT 토큰 기한이 만료됐습니다.");


    private final String code;
    private final String message;
}
