package cmc.mellyserver.mellycore.unit.scrap.repository;

import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapJDBCRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.unit.common.fixtures.PlaceScrapFixtureFactory;
import cmc.mellyserver.mellycore.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@SpringBootTest
@ActiveProfiles("local")
@Transactional
@Rollback(value = false)
public class BulkInsertTest {


    @Autowired
    public PlaceScrapRepository placeScrapRepository;

    @Autowired
    public PlaceScrapJDBCRepository placeScrapJDBCRepository;

    @Test
    public void bulkInsert_user() {

        var easyRandom = PlaceScrapFixtureFactory.createUser("$2a$10$rghaTRzFA74TXU.qDeTySOwqXo6ckpvtCKWbsba8W7hlc6HIYbKIu");
        int _200만 = 2_000_000;

        List<User> users = IntStream.range(0, _200만 * 1)
                .parallel() // 병렬로 안 돌리면 시간 완전 오래 걸린다.
                .mapToObj(i -> easyRandom.nextObject(User.class))
                .collect(Collectors.toList());

        var stopWatch = new StopWatch();

        stopWatch.start(); // 시간 재기

        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

        var queryStopWatch = new StopWatch();

        queryStopWatch.start();

        placeScrapJDBCRepository.saveUser(users);

        queryStopWatch.stop();
        System.out.println("DB 인서트 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }

    @Test
    public void bulkInsert_place_scrap() {

        var easyRandom = PlaceScrapFixtureFactory.getPlaceScrap();
        int _50만 = 1_000_000;

        List<PlaceScrap> placeScraps = IntStream.range(0, _50만 * 1)
                .parallel() // 병렬로 안 돌리면 시간 완전 오래 걸린다.
                .mapToObj(i -> easyRandom.nextObject(PlaceScrap.class))
                .collect(Collectors.toList());

        var stopWatch = new StopWatch();

        stopWatch.start();


        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();

        placeScrapJDBCRepository.savePlaceScrap(placeScraps);

        queryStopWatch.stop();
        System.out.println("DB 인서트 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }


}
