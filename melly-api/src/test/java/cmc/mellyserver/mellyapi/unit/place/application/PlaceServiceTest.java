package cmc.mellyserver.mellyapi.unit.place.application;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import cmc.mellyserver.mellyapi.place.application.impl.PlaceServiceImpl;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;

public class PlaceServiceTest {

	@InjectMocks
	private PlaceServiceImpl placeService;

	@Mock
	private PlaceQueryRepository placeQueryRepository;

	@Mock
	private PlaceRepository placeRepository;

}
