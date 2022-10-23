package cmc.mellyserver.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCodeAndDetails {


    // HTTP Status : 400 Bad Request
    NOT_FOUND_ERROR_CODE("0001", "발생한 에러의 에러코드를 찾을 수 없습니다."),
    BAD_REQUEST("0002", "잘못된 요청입니다."),

    // HTTP Status : 404 Not Found
    NOT_FOUND_API("0003", "해당 경로에 대한 응답 API를 찾을 수 없습니다."),

    // HTTP Status : 400 Bad Request
    DUPLICATE_EMAIL("1000","중복되는 이메일입니다."),
    DUPLICATE_NICKNAME("1001","중복되는 닉네임입니다."),
    INVALID_EMAIL("1002","이메일이 일치하지 않습니다."),
    INVALID_PASSWORD("1003","비밀번호가 일치하지 않습니다."),
    NO_SUCH_USER("1004","해당 id의 유저가 없습니다."),
    KAKAO_ACCESS("1005","카카오 리소스 서버 접근 중 문제가 발생했습니다."),
    GOOGLE_ACCESS("1006","구글 리소스 서버 접근 중 문제가 발생했습니다."),
    NAVER_ACCESS("1007","네이버 리소스 서버 접근 중 문제가 발생했습니다."),
    APPLE_ACCESS("1008","애플 로그인 중 문제가 발생했습니다."),
    // HTTP Status : 401 Unauthorized
    UNAUTHORIZED_USER("1009", "인증되지 않은 유저입니다."),
    EXPIRED_TOKEN("1010", "JWT 토큰 기한이 만료됐습니다."),

    // HTTP Status : 403 Forbidden
    FORBIDDEN_USER("1011","접근 권한이 없는 유저입니다."),

    NO_SUCH_PLACE("2000","해당하는 장소가 없습니다."),
    DUPLICATE_SCRAP("2001","중복 스크랩 할 수 없습니다."),
    NOT_EXIST_SCRAP("2002","스크랩 취소할 수 없습니다."),
    NO_SUCH_GROUP("2003","그룹이 존재하지 않습니다."),
    NO_SUCH_MEMORY("2004","그룹이 존재하지 않습니다."),
    NO_SUCH_COMMENT("2005","댓글이 존재하지 않습니다.");
    private final String code;
    private final String message;
}
