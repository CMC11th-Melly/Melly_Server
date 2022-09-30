package cmc.mellyserver.memory.domain.service;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.group.domain.GroupType;
import cmc.mellyserver.memory.domain.GroupInfo;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.OpenType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoryDomainService {

    private final MemoryRepository memoryRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;


    public Memory createMemory(String userId, Double lat, Double lng, String title, GroupType groupType, String content, int star, OpenType openType,Long groupId,String keyword)
    {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });
        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(lat,lng));

        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().position(new Position(lat, lng)).build());
            Memory memory = Memory.builder().title(title).content(content).keyword(keyword).groupInfo(new GroupInfo(groupType, groupId)).stars(star).openType(openType).build();
            memory.setUser(user);
            memory.setPlaceForMemory(savePlace);
            user.getVisitedPlace().add(savePlace.getId());
            return memoryRepository.save(memory);
        }
        else{
            Memory memory = Memory.builder().title(title).content(content).keyword(keyword).groupInfo(new GroupInfo(groupType, groupId)).stars(star).openType(openType).build();
            memory.setUser(user);
            memory.setPlaceForMemory(placeOpt.get());
            user.getVisitedPlace().add(placeOpt.get().getId());
            return memoryRepository.save(memory);
        }

    }
}
