package cmc.mellyserver.mellyappexternalapi.unit.place.application;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import cmc.mellyserver.mellyappexternalapi.place.application.impl.PlaceServiceImpl;
import cmc.mellyserver.mellydomain.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.mellydomain.place.domain.repository.PlaceRepository;

public class PlaceServiceTest {

	@InjectMocks
	private PlaceServiceImpl placeService;

	@Mock
	private PlaceQueryRepository placeQueryRepository;

	@Mock
	private PlaceRepository placeRepository;

}
