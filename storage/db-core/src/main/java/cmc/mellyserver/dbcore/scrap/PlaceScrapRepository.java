package cmc.mellyserver.dbcore.scrap;

import org.springframework.data.jpa.repository.JpaRepository;

import cmc.mellyserver.dbcore.place.Position;

public interface PlaceScrapRepository extends JpaRepository<PlaceScrap, Long> {

  boolean existsByUserIdAndPlaceId(Long id, Long placeId);

  void deleteByUserIdAndPlacePosition(Long id, Position position);

}