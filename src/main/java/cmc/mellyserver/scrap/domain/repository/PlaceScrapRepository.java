package cmc.mellyserver.scrap.domain.repository;

import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.scrap.domain.PlaceScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceScrapRepository extends JpaRepository<PlaceScrap,Long> {

    Optional<PlaceScrap> findByUserUserSeqAndPlaceId(Long userSeq, Long placeId);
    void deleteByUserUserSeqAndPlacePosition(Long userSeq ,Position position);


}