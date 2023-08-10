package cmc.mellyserver.mellycore.unit.common.bulkinsert;

import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.scrap.domain.enums.ScrapType;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.*;
import cmc.mellyserver.mellycore.user.enums.*;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.misc.EnumRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;


public class RandomFixtureFactory {
//    public static PlaceScrap create() {
//        EasyRandomParameters parameter = getEasyRandomParameters();
//        return new EasyRandom(parameter).nextObject(PlaceScrap.class);
//    }

//    public static Post create(Long memberId, LocalDate createdDate) {
//        EasyRandomParameters parameter = getEasyRandomParameters();
//        parameter
//                .randomize(memberId(), () -> memberId)
//                .randomize(createdDate(), new LocalDateRangeRandomizer(createdDate, createdDate));
//
//        return new EasyRandom(parameter).nextObject(Post.class);
//    }

    public static EasyRandom createUser(String password) {
        EasyRandomParameters parameter = new EasyRandomParameters();
        parameter
                .excludeField(named("id"))
                .randomize(email(), new StringRandomizer()) // 내가 강제로 주입받은 걸로 바꿀 수 있다.
                .randomize(password(), () -> password)
                .randomize(ageGroup(), new EnumRandomizer<>(AgeGroup.class))
                .randomize(gender(), new EnumRandomizer<>(Gender.class))
                .randomize(nickname(), new StringRandomizer())
                .randomize(provider(), new EnumRandomizer<>(Provider.class))
                .randomize(role_type(), new EnumRandomizer<>(RoleType.class))
                .randomize(user_id(), new StringRandomizer())
                .randomize(userStatus(), new EnumRandomizer<>(UserStatus.class));

        return new EasyRandom(parameter);
    }

//    public static EasyRandom createMemory() {
//        EasyRandomParameters parameter = new EasyRandomParameters();
//        parameter
//                .excludeField(named("id"))
//                .randomize(email(), new StringRandomizer()) // 내가 강제로 주입받은 걸로 바꿀 수 있다.
//                .randomize(password(), () -> password)
//                .randomize(ageGroup(), new EnumRandomizer<>(AgeGroup.class))
//                .randomize(gender(), new EnumRandomizer<>(Gender.class))
//                .randomize(nickname(), new StringRandomizer())
//                .randomize(provider(), new EnumRandomizer<>(Provider.class))
//                .randomize(role_type(), new EnumRandomizer<>(RoleType.class))
//                .randomize(user_id(), new StringRandomizer())
//                .randomize(userStatus(), new EnumRandomizer<>(UserStatus.class));
//
//        return new EasyRandom(parameter);
//    }

    // 사용자는 하나, 장소는 모두 다르게, scrapType은 고르게
    public static EasyRandom getPlaceScrap() {

        EasyRandomParameters parameter = new EasyRandomParameters();
        LongRangeRandomizer longRangeRandomizer = new LongRangeRandomizer(1L, 30_000_000L);
        parameter
                .excludeField(named("id"))
                .randomize(user(), () -> User.builder().id(longRangeRandomizer.getRandomValue()).nickname("jemin").build())
                .randomize(scrapType(), new EnumRandomizer<>(ScrapType.class));

        return new EasyRandom(parameter);
    }

//    public static EasyRandom getUser() {
//
//        EasyRandomParameters parameter = getEasyRandomParameters();
//
//        parameter
//                .randomize(user(), () -> User.cmc.mellyserver.mellycore.builder().id(1L).nickname("jemin").build())
//                .randomize(scrapType(), new EnumRandomizer<>(ScrapType.class));
//        return new EasyRandom(parameter);
//    }

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

    private static Predicate<Field> email() {
        return named("email").and(ofType(String.class));
    }

    private static Predicate<Field> password() {
        return named("password").and(ofType(String.class));
    }

    private static Predicate<Field> ageGroup() {
        return named("ageGroup").and(ofType(String.class));
    }

    private static Predicate<Field> gender() {
        return named("gender").and(ofType(String.class));
    }

    private static Predicate<Field> nickname() {
        return named("nickname").and(ofType(String.class));
    }

    private static Predicate<Field> provider() {
        return named("provider").and(ofType(String.class));
    }

    private static Predicate<Field> role_type() {
        return named("roleType").and(ofType(String.class));
    }

    private static Predicate<Field> user_id() {
        return named("userId").and(ofType(String.class));
    }

    private static Predicate<Field> place_id() {
        return named("place_id").and(ofType(String.class));
    }

    private static Predicate<Field> userStatus() {
        return named("userStatus").and(ofType(String.class));
    }


}
