package cmc.mellyserver.mellydomain.place.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import cmc.mellyserver.mellydomain.common.util.jpa.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_place")
@Getter
@AllArgsConstructor
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

	private String placeImage;

	private String placeCategory;

	private boolean isDeleted;

	@Builder
	public Place(Long id, Position position, String placeImage, String placeCategory, String placeName) {
		this.id = id;
		this.position = position;
		this.placeName = placeName;
		this.placeImage = placeImage;
		this.placeCategory = placeCategory;
	}

	@PrePersist
	public void init() {
		this.isDeleted = false;
	}
}
