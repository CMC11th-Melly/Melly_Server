package cmc.mellyserver.mellycore.memory.application;


import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.aws.FileUploader;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.group.exception.GroupNotFoundException;
import cmc.mellyserver.mellycore.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellycore.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.MemoryImage;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.memory.exception.MemoryNotFoundException;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoryWriteService {

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final MemoryRepository memoryRepository;

    private final PlaceRepository placeRepository;

    private final FileUploader fileUploader;

    private final GroupRepository groupRepository;


    @Transactional
    public Memory createMemory(CreateMemoryRequestDto createMemoryRequestDto) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(createMemoryRequestDto.getUserSeq());

        List<String> multipartFileNames = fileUploader.getMultipartFileNames(user.getUserId(), createMemoryRequestDto.getMultipartFiles());

        Memory memory = createMemoryRequestDto.toMemory();
        memory.setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));

        Place place = placeRepository.findPlaceByPosition(new Position(createMemoryRequestDto.getLat(), createMemoryRequestDto.getLng()));
        setPlaceId(createMemoryRequestDto, memory, place);

        return memoryRepository.save(memory);
    }


    @CacheEvict(value = "memory", key = "{#updateMemoryRequestDto.userSeq,#updateMemoryRequestDto.memoryId}")
    @Transactional
    public void updateMemory(UpdateMemoryRequestDto updateMemoryRequestDto) {

        Memory memory = memoryRepository.findById(updateMemoryRequestDto.getMemoryId()).orElseThrow(() -> {
            throw new MemoryNotFoundException();
        });

        UserGroup userGroup = groupRepository.findById(updateMemoryRequestDto.getGroupId()).orElseThrow(() -> {
            throw new GroupNotFoundException();
        });

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(updateMemoryRequestDto.getUserSeq());

        memory.updateMemory(updateMemoryRequestDto.getTitle(),
                updateMemoryRequestDto.getContent(),
                updateMemoryRequestDto.getKeyword(),
                userGroup.getId(),
                updateMemoryRequestDto.getOpenType(),
                updateMemoryRequestDto.getVisitedDate(),
                updateMemoryRequestDto.getStar(),
                updateMemoryRequestDto.getDeleteImageList(),
                fileUploader.getMultipartFileNames(user.getUserId(), updateMemoryRequestDto.getImages()));
    }


    @CacheEvict(value = "memory", key = "#memoryId", cacheManager = "redisCacheManager")
    @Transactional
    public void removeMemory(Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_MEMORY);
        });
        memory.delete();
    }

    private void setPlaceId(CreateMemoryRequestDto createMemoryRequestDto, Memory memory, Place place) {

        if (!Objects.isNull(place)) {
            memory.setPlaceId(place.getId());
        } else {
            Place savePlace = createMemoryRequestDto.toPlace();
            placeRepository.save(savePlace);
            memory.setPlaceId(savePlace.getId());
        }
    }
}
