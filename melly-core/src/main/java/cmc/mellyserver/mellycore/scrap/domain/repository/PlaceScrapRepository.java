package cmc.mellyserver.mellycore.scrap.domain.repository;

import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceScrapRepository extends JpaRepository<PlaceScrap, Long> {

    Optional<PlaceScrap> findByUserIdAndPlaceId(Long id, Long placeId);

    void deleteByUserIdAndPlacePosition(Long id, Position position);

}