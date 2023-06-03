package cmc.mellyserver.mellycore.scrap.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;

public interface PlaceScrapRepository extends JpaRepository<PlaceScrap, Long> {

	Optional<PlaceScrap> findByUserUserSeqAndPlaceId(Long userSeq, Long placeId);

	void deleteByUserUserSeqAndPlacePosition(Long userSeq, Position position);

}