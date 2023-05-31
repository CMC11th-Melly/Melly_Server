package cmc.mellyserver.unit.place.application;

import cmc.mellyserver.place.application.impl.PlaceServiceImpl;
import cmc.mellyserver.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
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
