package fixtures;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;

public abstract class PlaceFixtures {

  /* 스타벅스 */
  private static final String 스타벅스_이름 = "스타벅스";

  private static final String 스타벅스_카테고리 = "디저트/카페";

  private static final Position 스타벅스_좌표 = new Position(1.2345, 1.2345);

  private static final String 스타벅스_이미지_URL = "place_image_url";

  /* 이디야 */
  private static final String 이디야_이름 = "이디야";

  private static final String 이디야_카테고리 = "디저트/카페";

  private static final Position 이디야_좌표 = new Position(1.5555, 1.5555);

  private static final String 이디야_이미지_URL = "place_image_url";

  public static Place 스타벅스() {

	return Place.builder()
		.name(스타벅스_이름)
		.category(스타벅스_카테고리)
		.position(스타벅스_좌표)
		.imageUrl(스타벅스_이미지_URL)
		.build();
  }

  public static Place 이디야() {

	return Place.builder()
		.name(이디야_이름)
		.category(이디야_카테고리)
		.position(이디야_좌표)
		.imageUrl(이디야_이미지_URL)
		.build();
  }

}
