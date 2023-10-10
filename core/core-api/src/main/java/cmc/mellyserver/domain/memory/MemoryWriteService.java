package cmc.mellyserver.domain.memory;


import cmc.mellyserver.common.aop.place.ValidatePlaceExisted;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.group.GroupReader;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.domain.memory.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.domain.place.PlaceReader;
import cmc.mellyserver.domain.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoryWriteService {


    private final PlaceReader placeReader;

    private final MemoryReader memoryReader;

    private final MemoryWriter memoryWriter;

    private final UserReader userReader;

    private final GroupReader groupReader;


    @ValidatePlaceExisted
    @Transactional
    public Long createMemory(final CreateMemoryRequestDto createMemoryRequestDto) {

        Memory memory = createMemoryRequestDto.toMemory();
        enrollPlaceInfo(createMemoryRequestDto, memory);
        enrollMemoryImage(createMemoryRequestDto, memory);

        return memoryWriter.save(memory).getId();
    }


    @CacheEvict(value = "memory", key = "{#updateMemoryRequestDto.id,#updateMemoryRequestDto.memoryId}")
    @Transactional
    public void updateMemory(final UpdateMemoryRequestDto updateMemoryRequestDto) {

        Memory memory = memoryReader.findById(updateMemoryRequestDto.getMemoryId());
        UserGroup userGroup = groupReader.findById(updateMemoryRequestDto.getGroupId());
        User user = userReader.findById(updateMemoryRequestDto.getId());

        //  memory.updateMemory(updateMemoryRequestDto.getTitle(), updateMemoryRequestDto.getContent(), updateMemoryRequestDto.getKeyword(),
//                userGroup.getId(), updateMemoryRequestDto.getOpenType(), updateMemoryRequestDto.getVisitedDate(), updateMemoryRequestDto.getStar(), updateMemoryRequestDto.getDeleteImageList(), storageService.saveFileList(user.getId(), updateMemoryRequestDto.getImages()));
    }


    @CacheEvict(value = "memory", key = "#memoryId")
    @Transactional
    public void removeMemory(final Long memoryId) {

        Memory memory = memoryReader.findById(memoryId);
        memory.delete();
    }

    private void enrollPlaceInfo(final CreateMemoryRequestDto createMemoryRequestDto, final Memory memory) {

        Place place = placeReader.findByPosition(new Position(createMemoryRequestDto.getLat(), createMemoryRequestDto.getLng()));
        memory.setPlaceId(place.getId());
    }

    private void enrollMemoryImage(final CreateMemoryRequestDto createMemoryRequestDto, final Memory memory) {

//        List<String> multipartFileNames = storageService.saveFileList(createMemoryRequestDto.getUserId(), createMemoryRequestDto.getMultipartFiles());
//        memory.setMemoryImages(multipartFileNames.stream().map(MemoryImage::new).collect(Collectors.toList()));
    }
}
