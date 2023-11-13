package cmc.mellyserver.dbcore.scrap;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_place_scrap")
@Entity
public class PlaceScrap extends JpaBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_scrap_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	@Enumerated(EnumType.STRING)
	@Column(name = "scrap_type")
	private ScrapType scrapType;

	@Builder
	public PlaceScrap(User user, Place place, ScrapType scrapType) {
		this.user = user;
		this.place = place;
		this.scrapType = scrapType;
	}

	public static PlaceScrap createScrap(User user, Place place, ScrapType scrapType) {
		return PlaceScrap.builder().user(user).place(place).scrapType(scrapType).build();
	}
}
