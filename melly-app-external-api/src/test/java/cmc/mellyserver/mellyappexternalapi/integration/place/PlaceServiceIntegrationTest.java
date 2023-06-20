package cmc.mellyserver.mellyappexternalapi.integration.place;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyappexternalapi.common.factory.UserFactory;
import cmc.mellyserver.mellyappexternalapi.integration.IntegrationTest;
import cmc.mellyserver.mellyappexternalapi.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellyappexternalapi.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellyappexternalapi.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyappexternalapi.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.mellyappexternalapi.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.common.enums.OpenType;
import cmc.mellyserver.mellydomain.memory.domain.Memory;
import cmc.mellyserver.mellydomain.memory.domain.MemoryImage;
import cmc.mellyserver.mellydomain.memory.domain.vo.GroupInfo;
import cmc.mellyserver.mellydomain.place.domain.Position;
import cmc.mellyserver.mellydomain.user.domain.User;

public class PlaceServiceIntegrationTest extends IntegrationTest {

	@DisplayName("사용자가 메모리를 하나 이상 남긴 장소 리스트를 지도 위에 마커로 보여준다.")
	@Test
	void show_place_where_login_user_write_memory() {
		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());

		Memory memory = Memory.builder().userId(user.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
			.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
			.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
			.build();

		memory.setMemoryImages(List.of(new MemoryImage("testImage")));
		memory.setKeyword(List.of("기뻐요", "좋아요"));

		CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
			.userSeq(user.getUserSeq())
			.title("테스트 제목")
			.content("테스트 컨텐츠")
			.placeName("테스트 장소")
			.placeCategory("카페")
			.lat(1.234)
			.lng(1.234)
			.build();

		memoryService.createMemory(createMemoryRequestDto);

		// when
		List<MarkedPlaceResponseDto> markedPlaceResponseDtos = placeService.displayMarkedPlace(user.getUserSeq(), null);

		// then
		assertThat(markedPlaceResponseDtos).hasSize(1);
		assertThat(markedPlaceResponseDtos).extracting("placeId", "position")
			.containsExactlyInAnyOrder(Tuple.tuple(1L, new Position(1.234, 1.234)));

	}

	@DisplayName("장소 식별자로 장소 정보를 가지고 올 수 있다.")
	@Test
	void get_place_info_by_place_identifier() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());

		CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
			.userSeq(user.getUserSeq())
			.title("테스트 제목")
			.content("테스트 컨텐츠")
			.placeName("테스트 장소")
			.placeCategory("카페")
			.lat(1.234)
			.lng(1.234)
			.build();

		memoryService.createMemory(createMemoryRequestDto);
		PlaceResponseDto result = placeService.findPlaceByPosition(user.getUserSeq(), 1.234, 1.234);

		// when
		PlaceResponseDto placeByPlaceId = placeService.findPlaceByPlaceId(user.getUserSeq(), result.getPlaceId());

		// then
		assertThat(placeByPlaceId.getPlaceName()).isEqualTo("테스트 장소");
		assertThat(placeByPlaceId.getPosition()).isEqualTo(new Position(1.234, 1.234));
		System.out.println(placeByPlaceId.getMyMemoryCount()); // TODO : 로직 체크
	}

	@DisplayName("해당 식별자에 해당하는 장소가 없다면 예외가 발생한다.")
	@Test
	void get_place_info_by_place_identifier_not_found_error() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());

		// when then
		assertThatThrownBy(() -> placeService.findPlaceByPlaceId(user.getUserSeq(), 2L))
			.isInstanceOf(GlobalBadRequestException.class)
			.hasMessage(ExceptionCodeAndDetails.NO_SUCH_PLACE.getMessage());
	}

	@DisplayName("아직 한번도 메모리가 등록되지 않은 장소를 좌표로 조회하면 새로운 장소가 생성된다.")
	@Test
	void get_new_place() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());

		// when
		PlaceResponseDto placeByPosition = placeService.findPlaceByPosition(user.getUserSeq(), 1.5, 1.5);

		// then
		assertThat(placeByPosition.getIsScraped()).isFalse();
		assertThat(placeByPosition.getMyMemoryCount()).isZero();
		assertThat(placeByPosition.getOtherMemoryCount()).isZero();

	}

	@DisplayName("좌표로 장소를 조회했을때, 로그인한 사용자가 기존에 스크랩한 장소라면 스크랩 여부가 True로 반환된다.")
	@Test
	void get_place_login_user_scraped() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());

		CreatePlaceScrapRequestDto createPlaceScrapRequestDto = CreatePlaceScrapRequestDto.builder()
			.lng(1.5)
			.lat(1.5)
			.placeName("테스트 장소")
			.userSeq(user.getUserSeq())
			.build();

		placeScrapService.createScrap(createPlaceScrapRequestDto);

		// when
		PlaceResponseDto result = placeService.findPlaceByPosition(user.getUserSeq(), 1.5, 1.5);

		// then
		assertThat(result.getIsScraped()).isTrue();
	}

	@DisplayName("메모리를 수정하려고 할 때, 기존의 메모리와 관련된 정보들을 받을 수 있다.")
	@Test
	void get_update_form() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());

		Memory memory = Memory.builder().userId(user.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠").build();
		Memory savedMemory = memoryRepository.save(memory);
		// when
		MemoryUpdateFormResponseDto result = memoryService.findMemoryUpdateFormData(user.getUserSeq(),
			savedMemory.getId());

		// then
		assertThat(result.getTitle()).isEqualTo("테스트 제목");
		assertThat(result.getContent()).isEqualTo("테스트 컨텐츠");
	}

	@DisplayName("메모리를 수정하려고 할때, 기존에 메모리가 존재하지 않는다면 예외가 발생한다.")
	@Test
	void get_update_form_exception() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());

		// when then
		assertThatThrownBy(() -> memoryService.findMemoryUpdateFormData(user.getUserSeq(), 3L))
			.isInstanceOf(GlobalBadRequestException.class)
			.hasMessage(ExceptionCodeAndDetails.NO_SUCH_MEMORY.getMessage());

	}
}
