package cmc.mellyserver.dbcore.place;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_place")
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

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Builder
	public Place(Long id, Position position, String placeImage, String placeCategory, String placeName,
			Boolean isDeleted) {
		this.id = id;
		this.position = position;
		this.placeName = placeName;
		this.placeImage = placeImage;
		this.placeCategory = placeCategory;
		this.isDeleted = isDeleted;
	}

	@PrePersist
	public void init() {
		this.isDeleted = Boolean.FALSE;
	}

}
