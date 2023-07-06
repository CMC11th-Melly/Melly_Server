package cmc.mellyserver.mellycore.unit.common.fixtures;

import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.user.domain.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.misc.EnumRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;


public class PlaceScrapFixtureFactory {
    public static PlaceScrap create() {
        EasyRandomParameters parameter = getEasyRandomParameters();
        return new EasyRandom(parameter).nextObject(PlaceScrap.class);
    }

//    public static Post create(Long memberId, LocalDate createdDate) {
//        EasyRandomParameters parameter = getEasyRandomParameters();
//        parameter
//                .randomize(memberId(), () -> memberId)
//                .randomize(createdDate(), new LocalDateRangeRandomizer(createdDate, createdDate));
//
//        return new EasyRandom(parameter).nextObject(Post.class);
//    }

//    public static Post create(Long memberId, LocalDate start, LocalDate end) {
//        EasyRandomParameters parameter = getEasyRandomParameters();
//        parameter
//                .randomize(memberId(), () -> memberId) // 내가 강제로 주입받은 걸로 바꿀 수 있다.
//                .randomize(createdDate(), new LocalDateRangeRandomizer(start, end));
//
//        return new EasyRandom(parameter).nextObject(Post.class);
//    }

    // 사용자는 하나, 장소는 모두 다르게, scrapType은 고르게
    public static EasyRandom get() {

        EasyRandomParameters parameter = getEasyRandomParameters();

        parameter
                .randomize(user(), () -> User.builder().userSeq(1L).nickname("jemin").build())
                .randomize(scrapType(), new EnumRandomizer<>(ScrapType.class));
        return new EasyRandom(parameter);
    }

    private static EasyRandomParameters getEasyRandomParameters() {
        return new EasyRandomParameters()
                .excludeField(named("id"))
                .stringLengthRange(1, 100)
                .randomize(Long.class, new LongRangeRandomizer(1L, 10000L));
    }

    private static Predicate<Field> user() {
        return named("user").and(ofType(User.class));
    }

    private static Predicate<Field> place() {
        return named("place").and(ofType(Place.class));
    }

    private static Predicate<Field> scrapType() {
        return named("scrapType").and(ofType(ScrapType.class)).and(inClass(PlaceScrap.class));
    }
}
