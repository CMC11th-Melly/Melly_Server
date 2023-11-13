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
  @Column(name = "position")
  private Position position;

  @Column(name = "name")
  private String name;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "category")
  private String category;

  @Builder
  public Place(Long id, Position position, String name, String imageUrl, String category) {
	this.id = id;
	this.position = position;
	this.name = name;
	this.imageUrl = imageUrl;
	this.category = category;
  }
}
