package cmc.mellyserver.memory.domain.service;


import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.GroupInfo;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryImage;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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

    /**
     * 메모리 생성
     */
    public Memory createMemory(String uid,Double lat, Double lng, String title, String placeName, String placeCategory, String content, Long star, Long groupId, GroupType groupType,String groupName, List<String> keyword, List<MultipartFile> multipartFiles)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        List<String> multipartFileNames = s3FileLoader.getMultipartFileNames(multipartFiles);
        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(lat,lng));

        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().position(new Position(lat, lng)).placeCategory(placeCategory).placeName(placeName).build());

            Memory memory = (groupId == null) ? Memory.builder().title(title).content(content).openType(OpenType.ALL).stars(star).build() :
                    Memory.builder().title(title).content(content).groupInfo(new GroupInfo(groupName,groupType,groupId)).openType(OpenType.GROUP).stars(star).build();

            // user 세팅
            memory.setUser(user);
            // memoryImage 세팅
            memory.setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
            // 연관된 장소 세팅
            memory.setPlaceForMemory(savePlace);

            memory.setKeyword(keyword);
            // 사용자가 방문한 장소 세팅
            user.getVisitedPlace().add(savePlace.getId());
            return memoryRepository.save(memory);
        }
        else{

            // 만약에 전체 공개로 선택했다면, groupId는 따로 보내지 않기! 그걸로 분기하자.
            Memory memory = (groupId == null) ?
                    // 만약에 전체 공개로 들어오면 OpenType.ALL로 설정
                    Memory.builder().title(title).content(content).groupInfo(new GroupInfo(null,null,null)).openType(OpenType.ALL).stars(star).build() :
                    Memory.builder().title(title).content(content).groupInfo(new GroupInfo(groupName,groupType,groupId)).openType(OpenType.GROUP).stars(star).build();
            memory.setUser(user);

            if(multipartFileNames != null)
            {
                memory.setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
            }

            memory.setPlaceForMemory(placeOpt.get());
            memory.setKeyword(keyword);
            user.getVisitedPlace().add(placeOpt.get().getId());
            return memoryRepository.save(memory);
        }

    }


}
















//            if(groupId == null)
//            {
//             memory = Memory.builder().title(title).content(content).openType(OpenType.ALL).stars(star).build();
//            }
//            else{
//                memory = Memory.builder().title(title).content(content).groupInfo(new GroupInfo(groupType,groupId)).openType(OpenType.GROUP).stars(star).build();
//            }




//          if(groupId == null)
//            {
//
//                memory = Memory.builder().title(title).content(content).openType(OpenType.ALL).stars(star).build();
//            }
//            else{
//                memory = Memory.builder().title(title).content(content).groupInfo(new GroupInfo(groupType,groupId)).openType(OpenType.GROUP).stars(star).build();
//            }