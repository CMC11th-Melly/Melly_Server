package cmc.mellyserver.mellycommon.codes;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {

    /*
    Global Error
     */
    NOT_FOUND_ERROR_CODE(HttpStatus.BAD_REQUEST.value(), "G001", "발생한 에러의 에러코드를 찾을 수 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "G002", "잘못된 요청입니다."),
    NOT_FOUND_API(HttpStatus.BAD_REQUEST.value(), "G003", "해당 경로에 대한 응답 API를 찾을 수 없습니다."),


    /*
    Business Error
     */

    // Authentication
    KAKAO_ACCESS(HttpStatus.BAD_REQUEST.value(), "B001", "카카오 리소스 서버 접근 중 문제가 발생했습니다."),
    GOOGLE_ACCESS(HttpStatus.BAD_REQUEST.value(), "B002", "구글 리소스 서버 접근 중 문제가 발생했습니다."),
    NAVER_ACCESS(HttpStatus.BAD_REQUEST.value(), "B003", "네이버 리소스 서버 접근 중 문제가 발생했습니다."),
    APPLE_ACCESS(HttpStatus.BAD_REQUEST.value(), "B004", "애플 로그인 중 문제가 발생했습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST.value(), "B005", "JWT 토큰 기한이 만료됐습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED.value(), "B006", "인증되지 않은 유저입니다."),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN.value(), "B007", "접근 권한이 없는 유저입니다."),

    // User
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST.value(), "B008", "중복되는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST.value(), "B009", "중복되는 닉네임입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST.value(), "B010", "이메일이 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST.value(), "B011", "비밀번호가 일치하지 않습니다."),
    NO_SUCH_USER(HttpStatus.NOT_FOUND.value(), "B012", "유저가 존재하지 않습니다."),


    NO_SUCH_PLACE(HttpStatus.NOT_FOUND.value(), "B013", "해당하는 장소가 없습니다."),
    DUPLICATE_SCRAP(400, "B013", "중복 스크랩 할 수 없습니다."),
    NOT_EXIST_SCRAP(400, "B014", "스크랩 취소할 수 없습니다."),
    NO_SUCH_GROUP(HttpStatus.NOT_FOUND.value(), "B015", "그룹이 존재하지 않습니다."),
    NO_SUCH_MEMORY(HttpStatus.NOT_FOUND.value(), "B016", "메모리가 존재하지 않습니다."),
    NO_SUCH_COMMENT(HttpStatus.NOT_FOUND.value(), "B017", "댓글이 존재하지 않습니다."),
    NO_SUCH_COMMENT_LIKE(HttpStatus.NOT_FOUND.value(), "B018", "댓글에 좋아요가 존재하지 않습니다."),
    NO_SUCH_NOTIFICATION(HttpStatus.NOT_FOUND.value(), "B019", "알림이 존재하지 않습니다."),
    DUPLICATED_GROUP(HttpStatus.BAD_REQUEST.value(), "B020", "이미 수락한 그룹입니다"),
    NO_AUTHORITY_TO_REMOVE(HttpStatus.FORBIDDEN.value(), "B021", "그룹을 삭제할 권한이 없습니다"),
    DUPLICATED_COMMENT_LIKE(HttpStatus.BAD_REQUEST.value(), "B022", "댓글에 좋아요가 중복되었습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
