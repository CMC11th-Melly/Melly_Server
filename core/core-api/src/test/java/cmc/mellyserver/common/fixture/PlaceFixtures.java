package cmc.mellyserver.common.fixture;


import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;

public abstract class PlaceFixtures {

    private static final String 장소_이름 = "스타벅스";

    private static final String 장소_카테고리 = "디저트/카페";

    private static final Position 장소_좌표 = new Position(1.2345, 1.2345);

    private static final String 장소_이미지_URL = "place_image_url";


    public static Place 스타벅스() {

        return Place.builder()
                .placeName(장소_이름)
                .placeCategory(장소_카테고리)
                .position(장소_좌표)
                .isDeleted(Boolean.FALSE)
                .placeImage(장소_이미지_URL)
                .build();
    }
}
