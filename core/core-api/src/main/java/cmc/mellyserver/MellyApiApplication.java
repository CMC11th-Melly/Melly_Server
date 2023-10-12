package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @EntityScan 설정 이유 : QueryDSL 의존성을 Core API 모듈에 적용하면 Storage:db-core에 있는 엔티티 클래스 정보를 기반으로 Q-Class를
 * 생성하는것이 불가능했습니다.
 * <p> 이 문제를 해결하기 위해 Core-API의 SpringBootApplication에 Entity 위치를 지정해주는 @EntityScan을 추가했습니다.
 *
 */
@EntityScan(basePackages = {"cmc.mellyserver.dbcore"})
@SpringBootApplication
public class MellyApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(MellyApiApplication.class, args);
    }


}
