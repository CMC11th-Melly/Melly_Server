package cmc.mellyserver.domain.memory;


import cmc.mellyserver.common.aop.place.ValidatePlaceExisted;
import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.memory.enums.OpenType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.domain.memory.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.domain.memory.event.GroupUserMemoryCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoryWriteService {

    private final MemoryRepository memoryRepository;

    private final UserRepository userRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final PlaceRepository placeRepository;

    private final GroupRepository groupRepository;


    /*
    추가 가능 기능 : 내가 메모리를 작성했을때,
    그룹 공개 -> 내가 이 메모리를 함께 공유하기로한 그룹의 사람들에게 푸시 알림을 전송하기, 단 그룹으로부터 알림을 받겠다에 동의한 사람만 가능
    장소 추가 로직은 차후 AOP 적용하기
     */
    @ValidatePlaceExisted
    @Transactional
    public Long createMemory(final CreateMemoryRequestDto createMemoryRequestDto) {

        Memory memory = createMemoryRequestDto.toMemory();
        enrollPlaceInfo(createMemoryRequestDto, memory);
        enrollMemoryImage(createMemoryRequestDto, memory);

        publishToGroupUser(memory, createMemoryRequestDto.getGroupId());

        return memoryRepository.save(memory).getId();
    }


    @CacheEvict(value = "memory", key = "{#updateMemoryRequestDto.id,#updateMemoryRequestDto.memoryId}")
    @Transactional
    public void updateMemory(final UpdateMemoryRequestDto updateMemoryRequestDto) {

        Memory memory = memoryRepository.getById(updateMemoryRequestDto.getMemoryId());
        UserGroup userGroup = groupRepository.getById(updateMemoryRequestDto.getGroupId());
        User user = userRepository.getById(updateMemoryRequestDto.getId());

        //  memory.updateMemory(updateMemoryRequestDto.getTitle(), updateMemoryRequestDto.getContent(), updateMemoryRequestDto.getKeyword(),
//                userGroup.getId(), updateMemoryRequestDto.getOpenType(), updateMemoryRequestDto.getVisitedDate(), updateMemoryRequestDto.getStar(), updateMemoryRequestDto.getDeleteImageList(), storageService.saveFileList(user.getId(), updateMemoryRequestDto.getImages()));
    }


    @CacheEvict(value = "memory", key = "#memoryId")
    @Transactional
    public void removeMemory(final Long memoryId) {

        Memory memory = memoryRepository.getById(memoryId);
        memory.delete();
    }

    private void publishToGroupUser(final Memory memory, final Long groupId) {

        if (memory.getOpenType().equals(OpenType.GROUP)) {
            applicationEventPublisher.publishEvent(new GroupUserMemoryCreatedEvent(memory.getUserId(), memory.getId(), groupId));
        }
    }

    private void enrollPlaceInfo(final CreateMemoryRequestDto createMemoryRequestDto, final Memory memory) {

        Optional<Place> place = placeRepository.findPlaceByPosition(new Position(createMemoryRequestDto.getLat(), createMemoryRequestDto.getLng()));
        memory.setPlaceId(place.get().getId());
    }

    private void enrollMemoryImage(final CreateMemoryRequestDto createMemoryRequestDto, final Memory memory) {

//        List<String> multipartFileNames = storageService.saveFileList(createMemoryRequestDto.getUserId(), createMemoryRequestDto.getMultipartFiles());
//        memory.setMemoryImages(multipartFileNames.stream().map(MemoryImage::new).collect(Collectors.toList()));
    }
}
