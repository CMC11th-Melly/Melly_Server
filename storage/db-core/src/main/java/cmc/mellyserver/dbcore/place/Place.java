package cmc.mellyserver.dbcore.place;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_place")
@Entity
public class Place extends JpaBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_id")
	private Long id;

	@Embedded
	@Column(name = "place_position")
	private Position position;

	@Column(name = "place_name")
	private String placeName;

	@Column(name = "place_image")
	private String placeImage;

	@Column(name = "place_category")
	private String placeCategory;

	@Builder
	public Place(Long id, Position position, String placeImage, String placeCategory, String placeName) {
		this.id = id;
		this.position = position;
		this.placeName = placeName;
		this.placeImage = placeImage;
		this.placeCategory = placeCategory;
	}
}
