package cmc.mellyserver.auth.config;

import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final MemoryRepository memoryRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    @Transactional
    public void loadData()
    {
        memoryRepository.deleteAll();
        placeRepository.deleteAll();
        userRepository.deleteAll();

        Place savePlace = placeRepository.save(Place.builder().position(new Position(127.2343252, 34.2352326)).name("성수").build());
    }
}
