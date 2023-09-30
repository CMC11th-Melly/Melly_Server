package cmc.mellyserver.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
public enum ErrorCode {

    /*
    Global Error
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "COMMON-001", "서버에서 처리할 수 없습니다."),

    INVALID_INPUT_VALUE(BAD_REQUEST.value(), "COMMON-002", "유효성 검증에 실패했습니다."),

    BINDING_ERROR(BAD_REQUEST.value(), "COMMON-003", "요청 값 바인딩에 실패했습니다."),

    BAD_REQUEST_ERROR(BAD_REQUEST.value(), "COMMON-004", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(BAD_REQUEST.value(), "COMMON-005", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(BAD_REQUEST.value(), "COMMON-006", " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(BAD_REQUEST.value(), "COMMON-007", "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(BAD_REQUEST.value(), "COMMON-008", "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(BAD_REQUEST.value(), "COMMON-009", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(BAD_REQUEST.value(), "COMMON-010", "com.fasterxml.jackson.core Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(NOT_FOUND.value(), "COMMON-011", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(NOT_FOUND.value(), "COMMON-012", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(NOT_FOUND.value(), "COMMON-013", "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(NOT_FOUND.value(), "COMMON-014", "Header에 데이터가 존재하지 않는 경우 "),

    /*
    Business Error
     */

    /* Auth */

    OAUTH_CLIENT_ERROR(BAD_REQUEST.value(), "AUTH-001", "OAuth 통신 중 에러가 발생했습니다."),
    EXPIRED_TOKEN(BAD_REQUEST.value(), "AUTH-002", "JWT 토큰 기한이 만료됐습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "AUTH-003", "인증되지 않은 유저입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "AUTH-004", "접근 권한이 없는 유저입니다."),
    RELOGIN_REQUIRED(HttpStatus.UNAUTHORIZED.value(), "AUTH-005", "토큰 기한 만료로 재로그인이 필요합니다."),
    BEFORE_PASSWORD_NOT_EXIST(BAD_REQUEST.value(), "AUTH-006", "이전 비밀번호가 일치하지 않습니다."),
    ABNORMAL_ACCESS(HttpStatus.UNAUTHORIZED.value(), "AUTH-007", "비정상적인 접속이 감지되어 재로그인 필요합니다."),
    LOGOUT_WITHDRAW_USER(HttpStatus.UNAUTHORIZED.value(), "AUTH-008", "이미 로그아웃 했거나 탈퇴한 유저입니다."),


    /* User */
    DUPLICATE_EMAIL(CONFLICT.value(), "USER-001", "중복되는 이메일입니다."),
    DUPLICATE_NICKNAME(CONFLICT.value(), "USER-002", "중복되는 닉네임입니다."),
    INVALID_EMAIL(BAD_REQUEST.value(), "USER-003", "이메일이 일치하지 않습니다."),
    INVALID_PASSWORD(BAD_REQUEST.value(), "USER-004", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(NOT_FOUND.value(), "USER-005", "유저가 존재하지 않습니다."),


    /* Place */
    NO_SUCH_PLACE(NOT_FOUND.value(), "B013", "해당하는 장소가 없습니다."),


    /* Scrap */
    DUPLICATE_SCRAP(CONFLICT.value(), "SCRAP-001", "중복 스크랩 할 수 없습니다."),
    NOT_EXIST_SCRAP(CONFLICT.value(), "SCRAP-002", "스크랩 취소할 수 없습니다."),


    /* Group */
    NO_SUCH_GROUP(NOT_FOUND.value(), "GROUP-001", "그룹이 존재하지 않습니다."),
    NO_AUTHORITY_TO_REMOVE(HttpStatus.FORBIDDEN.value(), "GROUP-002", "그룹을 삭제할 권한이 없습니다"),
    DUPLICATED_GROUP(CONFLICT.value(), "GROUP-003", "이미 수락한 그룹입니다"),
    EXIT_GROUP_NOT_POSSIBLE(CONFLICT.value(), "GROUP-004", "그룹의 인원이 2명 이상일때 그룹에서 나갈 수 있습니다."),
    PARTICIPATE_GROUP_NOT_POSSIBLE(CONFLICT.value(), "GROUP-005", "그룹의 인원은 최대 10명 입니다."),


    /* Memory */
    NO_SUCH_MEMORY(NOT_FOUND.value(), "MEMORY-001", "메모리가 존재하지 않습니다."),


    /* Comment */
    NO_SUCH_COMMENT(NOT_FOUND.value(), "COMMENT-001", "댓글이 존재하지 않습니다."),
    NO_SUCH_COMMENT_LIKE(NOT_FOUND.value(), "COMMENT-002", "댓글에 좋아요가 존재하지 않습니다."),
    DUPLICATED_COMMENT_LIKE(CONFLICT.value(), "COMMENT-003", "댓글에 좋아요가 중복되었습니다."),


    /* Notification */
    NO_SUCH_NOTIFICATION(NOT_FOUND.value(), "NOTI-001", "알림이 존재하지 않습니다.");


    private final int status;

    private final String code;

    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
