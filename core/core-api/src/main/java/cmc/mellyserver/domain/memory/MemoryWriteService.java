package cmc.mellyserver.domain.memory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.common.aop.place.ValidatePlaceExisted;
import cmc.mellyserver.common.event.MemoryCreatedEvent;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.domain.memory.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.domain.place.PlaceReader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoryWriteService {

	private final PlaceReader placeReader;

	private final MemoryReader memoryReader;

	private final MemoryWriter memoryWriter;

	private final ApplicationEventPublisher applicationEventPublisher;

	@ValidatePlaceExisted
	@Transactional
	public Long createMemory(final CreateMemoryRequestDto createMemoryRequestDto) {

		Memory memory = createMemoryRequestDto.toMemory();
		enrollPlaceInfo(createMemoryRequestDto, memory);
		enrollMemoryImage(createMemoryRequestDto, memory);
		applicationEventPublisher.publishEvent(new MemoryCreatedEvent(memory.getId()));
		return memoryWriter.save(memory).getId();
	}

	@CacheEvict(value = "memory", key = "{#updateMemoryRequestDto.id,#updateMemoryRequestDto.memoryId}")
	@Transactional
	public void updateMemory(final UpdateMemoryRequestDto updateMemoryRequestDto) {

	}

	@CacheEvict(value = "memory", key = "#memoryId")
	@Transactional
	public void removeMemory(final Long memoryId) {

		Memory memory = memoryReader.findById(memoryId);
		memory.delete();
	}

	private void enrollPlaceInfo(final CreateMemoryRequestDto createMemoryRequestDto, final Memory memory) {

		Place place = placeReader.findByPosition(createMemoryRequestDto.getPosition());
		memory.setPlaceId(place.getId());
	}

	private void enrollMemoryImage(final CreateMemoryRequestDto createMemoryRequestDto, final Memory memory) {

		// List<String> multipartFileNames =
		// storageService.saveFileList(createMemoryRequestDto.getUserId(),
		// createMemoryRequestDto.getMultipartFiles());
		// memory.setMemoryImages(multipartFileNames.stream().map(MemoryImage::new).collect(Collectors.toList()));
	}

}
