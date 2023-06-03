package cmc.mellyserver.mellycore.scrap.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cmc.mellyserver.mellycore.common.enums.ScrapType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_place_scrap")
@Getter
public class PlaceScrap extends JpaBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_scrap_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	@Enumerated(EnumType.STRING)
	private ScrapType scrapType;

	@Builder
	public PlaceScrap(User user, Place place, ScrapType scrapType) {
		this.user = user;
		this.place = place;
		this.scrapType = scrapType;
	}

	public static PlaceScrap createScrap(User user, Place place, ScrapType scrapType) {
		return new PlaceScrap(user, place, scrapType);
	}
}
