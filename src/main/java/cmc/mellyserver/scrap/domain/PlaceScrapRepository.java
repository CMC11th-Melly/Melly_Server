package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.place.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceScrapRepository extends JpaRepository<PlaceScrap,Long> {

    Optional<PlaceScrap> findByUserIdAndPlaceId(Long userId, Long placeId);
    void deleteByUserIdAndPlaceId(Long userId,Position position);


}