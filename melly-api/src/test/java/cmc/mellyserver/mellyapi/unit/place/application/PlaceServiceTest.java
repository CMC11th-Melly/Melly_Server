package cmc.mellyserver.mellyapi.unit.place.application;

import cmc.mellyserver.mellycore.place.application.PlaceServiceImpl;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class PlaceServiceTest {

    @InjectMocks
    private PlaceServiceImpl placeService;

    @Mock
    private PlaceQueryRepository placeQueryRepository;

    @Mock
    private PlaceRepository placeRepository;

}
