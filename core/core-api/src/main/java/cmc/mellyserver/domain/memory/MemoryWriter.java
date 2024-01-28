package cmc.mellyserver.domain.memory;

import static cmc.mellyserver.support.exception.ErrorCode.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.FileDto;
import cmc.mellyserver.FileService;
import cmc.mellyserver.common.aspect.place.CheckPlaceExist;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.memory.memory.MemoryImage;
import cmc.mellyserver.dbcore.memory.memory.MemoryRepository;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.domain.memory.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.domain.place.PlaceReader;
import cmc.mellyserver.support.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemoryWriter {

    private final MemoryRepository memoryRepository;

    private final MemoryReader memoryReader;

    private final PlaceReader placeReader;

    private final FileService fileService;

    private final ThreadPoolTaskExecutor imageUploadTaskExecutor;

    @CheckPlaceExist
    public Long save(CreateMemoryRequestDto createMemoryRequestDto) {

        Memory memory = createMemoryRequestDto.toMemory();
        addPlace(createMemoryRequestDto.getPosition(), memory);
        addKeywords(createMemoryRequestDto.getKeywordIds(), memory);
        Memory savedMemory = memoryRepository.save(memory);
        imageUploadTaskExecutor.execute(() -> addMemoryImages(memory.getId(), createMemoryRequestDto.getUserId(),
            createMemoryRequestDto.getMultipartFiles()));
        return savedMemory.getId();
    }

    public void update(UpdateMemoryRequestDto updateDto) {

        Memory memory = memoryReader.read(updateDto.getMemoryId());
        memory.update(updateDto.getTitle(), updateDto.getContent(), updateDto.getGroupId(), updateDto.getOpenType(),
            updateDto.getVisitedDate(), updateDto.getStar());
        List<String> imageUrls = updateImages(updateDto.getDeleteImageList(), updateDto.getImages(),
            memory.getUserId());
        memory.updateMemoryImages(updateDto.getDeleteImageList(), imageUrls);
    }

    private List<String> updateImages(List<Long> deleteImages, List<MultipartFile> newImages, Long userId) {
        fileService.deleteFiles(deleteImages);
        return saveImages(userId, newImages);
    }

    private List<String> saveImages(Long userId, List<MultipartFile> newImages) {
        return fileService.saveFiles(userId, extractFileDtos(newImages));
    }

    private List<FileDto> extractFileDtos(List<MultipartFile> newImages) {
        return newImages.stream()
            .map(image -> {
                try {
                    return new FileDto(image.getOriginalFilename(), image.getSize(), image.getContentType(),
                        image.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
    }

    public void remove(final Long memoryId) {
        Memory memory = memoryReader.read(memoryId);
        memory.delete();
    }

    private void addPlace(Position position, Memory memory) {

        Place place = placeReader.read(position);
        memory.setPlaceId(place.getId());
    }

    private void addMemoryImages(Long userId, Long memoryId, List<MultipartFile> images) {

        List<FileDto> fileList = extractImageData(images);

        if (fileList.isEmpty()) {
            return;
        }

        List<String> multipartFileNames = fileService.saveFiles(userId, fileList);
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> new BusinessException(NO_SUCH_MEMORY));
        memory.setMemoryImages(multipartFileNames.stream().map(MemoryImage::new).collect(Collectors.toList()));
    }

    private List<FileDto> extractImageData(List<MultipartFile> multipartFiles) {

        if (Objects.isNull(multipartFiles)) {
            return Collections.emptyList();
        }

        return multipartFiles.stream().map(file -> {
            try {
                return new FileDto(file.getOriginalFilename(), file.getSize(), file.getContentType(),
                    file.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private void addKeywords(List<Long> keywordIds, Memory memory) {
        memory.addKeywordIds(keywordIds);
    }

}
