package cmc.mellyserver.memory.domain.service;


import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.domain.group.GroupRepository;
import cmc.mellyserver.group.domain.group.UserGroup;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.GroupInfo;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryImage;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.common.enums.OpenType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MemoryDomainService {

    private final MemoryRepository memoryRepository;
    private final PlaceRepository placeRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final S3FileLoader s3FileLoader;
    private final GroupRepository groupRepository;



    // TODO : 찐막 수정 완료
    public Memory createMemory(Long userSeq, Double lat, Double lng, String title, String placeName, String placeCategory, String content, Long star, Long groupId, OpenType openType, List<String> keyword, LocalDateTime visitedDate, List<MultipartFile> multipartFiles)
    {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        List<String> multipartFileNames = s3FileLoader.getMultipartFileNames(user.getUserId(),multipartFiles);

        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(lat,lng)); // 해당 좌표를 가진 장소 정보가 기존에 존재하는지 체크

        // 만약 아직 장소가 없다면?
        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().position(new Position(lat, lng)).placeCategory(placeCategory).placeName(placeName).build());

            Memory memory;

            // 1. 만약 그룹이 없다면?
            if(groupId == null || groupId == -1L)
            {
               memory  = Memory.builder().title(title).content(content).openType(OpenType.ALL).groupInfo(new GroupInfo("그룹 미설정",GroupType.ALL,-1L)).openType(openType).stars(star).visitedDate(visitedDate).build();
            }
            else{
                UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
                });
                memory = Memory.builder().title(title).content(content).groupInfo(new GroupInfo(userGroup.getGroupName(),userGroup.getGroupType(),groupId)).visitedDate(visitedDate).openType(openType).stars(star).build();
            }

            // user 세팅
            memory.setUserId(userSeq);
            // memoryImage 세팅
            memory.setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
            // 연관된 장소 세팅
            memory.setPlaceId(savePlace.getId());
            // 키워드 등록
            memory.setKeyword(keyword);
            // 트렌드 분석에 사용되는 장소 이름 추가
            return memoryRepository.save(memory);
        }
        else{

            Memory memory;

            if(groupId == null || groupId == -1L)
            {
              memory =  Memory.builder().title(title).content(content).groupInfo(new GroupInfo("그룹 미설정",GroupType.ALL,-1L)).openType(openType).stars(star).visitedDate(visitedDate).build();
            }
            else{
                UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
                });
               memory = Memory.builder().title(title).content(content).groupInfo(new GroupInfo(userGroup.getGroupName(),userGroup.getGroupType(),groupId)).openType(openType).stars(star).visitedDate(visitedDate).build();
            }

            memory.setUserId(userSeq);

            if(multipartFileNames != null)
            {
                memory.setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
            }

            memory.setPlaceId(placeOpt.get().getId());
            memory.setKeyword(keyword);
            return memoryRepository.save(memory);
        }

    }


}