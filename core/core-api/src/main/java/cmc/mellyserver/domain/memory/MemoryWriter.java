package cmc.mellyserver.domain.memory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.FileDto;
import cmc.mellyserver.FileService;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryImage;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.domain.place.PlaceReader;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemoryWriter {

	private final MemoryRepository memoryRepository;

	private final PlaceReader placeReader;

	private final FileService fileService;

	public Memory save(CreateMemoryRequestDto createMemoryRequestDto) {

		Memory memory = createMemoryRequestDto.toMemory();
		addPlace(createMemoryRequestDto.getPosition(), memory);
		addMemoryImages(createMemoryRequestDto, memory);
		return memoryRepository.save(memory);
	}

	private void addPlace(final Position position, final Memory memory) {

		Place place = placeReader.findByPosition(position);
		memory.setPlaceId(place.getId());
	}

	private void addMemoryImages(final CreateMemoryRequestDto createMemoryRequestDto, final Memory memory) {

		List<FileDto> fileList = extractFileList(createMemoryRequestDto.getMultipartFiles());

		if (Objects.nonNull(fileList)) {
			List<String> multipartFileNames = fileService.saveFileList(createMemoryRequestDto.getUserId(), fileList);
			memory.setMemoryImages(multipartFileNames.stream().map(MemoryImage::new).collect(Collectors.toList()));
		}
	}

	private List<FileDto> extractFileList(List<MultipartFile> multipartFiles) {

		if (Objects.isNull(multipartFiles)) {
			return null;
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

}
