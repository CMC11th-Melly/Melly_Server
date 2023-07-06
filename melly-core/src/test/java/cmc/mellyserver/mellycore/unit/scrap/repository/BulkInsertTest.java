package cmc.mellyserver.mellycore.unit.scrap.repository;

import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapJDBCRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.unit.common.fixtures.PlaceScrapFixtureFactory;
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

//    @Autowired
//    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static final String TABLE = "tb_place_scrap";

    //        List<PlaceScrap> collect = IntStream.range(0, _1만 * 1)
//                .parallel() // 병렬로 안 돌리면 시간 완전 오래 걸린다.
//                .mapToObj(i -> easyRandom.nextObject(PlaceScrap.class))
//                .collect(Collectors.toList());

    @Test
    public void bulkInsert() {
//        var easyRandom = PlaceScrapFixtureFactory.get(1L, 2L);

        var stopWatch = new StopWatch();
        stopWatch.start(); // 시간 재기

        int _300만 = 3_000_000;


        List<Place> places = IntStream.range(0, _300만)
                .parallel()
                .mapToObj(i -> Place.builder().placeName("장소").isDeleted(DeleteStatus.N).build())
                .collect(Collectors.toList());

        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();

        placeScrapJDBCRepository.saveAll(places);

        queryStopWatch.stop();
        System.out.println("DB 인서트 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }

    @Test
    public void bulkInsert_place_scrap() {

        var easyRandom = PlaceScrapFixtureFactory.get();
        int _10만 = 1_000_000;

        List<PlaceScrap> collect = IntStream.range(0, _10만 * 1)
                .parallel() // 병렬로 안 돌리면 시간 완전 오래 걸린다.
                .mapToObj(i -> easyRandom.nextObject(PlaceScrap.class))
                .collect(Collectors.toList());

        var stopWatch = new StopWatch();

        stopWatch.start(); // 시간 재기


        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();

        placeScrapJDBCRepository.savePlaceScrap(collect);

        queryStopWatch.stop();
        System.out.println("DB 인서트 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }

    @Test
    public void bulkInsert_user() {

        var easyRandom = PlaceScrapFixtureFactory.get();
        int _10만 = 1_000_000;

        List<PlaceScrap> collect = IntStream.range(0, _10만 * 1)
                .parallel() // 병렬로 안 돌리면 시간 완전 오래 걸린다.
                .mapToObj(i -> easyRandom.nextObject(PlaceScrap.class))
                .collect(Collectors.toList());

        var stopWatch = new StopWatch();

        stopWatch.start(); // 시간 재기


        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();

        placeScrapJDBCRepository.savePlaceScrap(collect);

        queryStopWatch.stop();
        System.out.println("DB 인서트 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }


//    public void bulkInsert(List<PlaceScrap> placeScrap) {
//        var sql = String.format("INSERT INTO `%s` (scrap_type)" +
//                "VALUES (:scrapType)", TABLE);
//
//        List<String> collect = placeScrap.stream().map(t -> t.getScrapType().toString()).collect(Collectors.toList());
//
//        SqlParameterSource[] collects = collect
//                .stream()
//                .map(BeanPropertySqlParameterSource::new)
//                .toArray(SqlParameterSource[]::new);
//        namedParameterJdbcTemplate.batchUpdate(sql, collects);
//    }
}
