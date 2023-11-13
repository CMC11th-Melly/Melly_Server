package cmc.mellyserver.domain.memory.integration;

import static fixtures.UserFixtures.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.memory.OpenType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.memory.MemoryService;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.support.IntegrationTestSupport;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import fixtures.MemoryFixtures;

public class MemoryServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemoryService memoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @DisplayName("[AOP 동작 테스트] 메모리를 저장할때 장소가 DB에 없으면 추가한다")
    @Test
    void 메모리를_저장할때_장소가_DB에_없으면_추가한다() {

        // given
        CreateMemoryRequestDto 스타벅스 = CreateMemoryRequestDto.builder()
            .lng(1.234)
            .lat(1.234)
            .star(1L)
            .placeName("스타벅스")
            .build();

        // when
        memoryService.createMemory(스타벅스);

        // then
        Optional<Place> place = placeRepository.findByPosition(스타벅스.getPosition());
        assertThat(place).isPresent();
    }

    @DisplayName("메모리를 삭제하려고 할때")
    @Nested
    class When_remove_memory {

        @DisplayName("메모리가 존재한다면 삭제처리한다")
        @Test
        void 메모리가_존재한다면_삭제처리한다() {

            // given
            User 모카 = userRepository.save(모카());

            // when
            Memory 메모리 = memoryRepository.save(MemoryFixtures.메모리(1L, 모카.getId(), null, "성수 재밌었다", OpenType.ALL));
            memoryService.removeMemory(메모리.getId());

            // then
            Memory memory = memoryRepository.findById(메모리.getId()).get();
            assertThat(memory.getDeletedAt()).isNotNull();
        }

        @DisplayName("메모리가 없으면 예외를 발생시킨다.")
        @Test
        void memory_not_exist_exception() {

            // given
            User 모카 = userRepository.save(모카());

            // when & then
            assertThatThrownBy(() -> memoryService.removeMemory(-1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.NO_SUCH_MEMORY.getMessage());
        }
    }
}
