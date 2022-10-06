package cmc.mellyserver.memory.domain.service;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.exception.GlobalServerException;
import cmc.mellyserver.common.util.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.AWSS3UploadService;
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
import cmc.mellyserver.user.domain.UserRepository;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoryDomainService {

    private final MemoryRepository memoryRepository;
    private final PlaceRepository placeRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final AWSS3UploadService uploadService;

    public Memory createMemory(String uid, Double lat, Double lng, String title , String content, Long star, Long groupId, GroupType groupType, List<String> keyword, List<MultipartFile> multipartFiles)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        List<String> multipartFileNames = getMultipartFileNames(multipartFiles);
        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(lat,lng));

        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().position(new Position(lat, lng)).build());
            /**
             * 메모리 제목, 메모리 컨텐츠, 메모리 사진들, 별점, 키워드
             */
            System.out.println("hello~~~~");
            Memory memory;
            if(groupId == null)
            {
                System.out.println("opentype");
             memory = Memory.builder().title(title).content(content).openType(OpenType.ALL).stars(star).build();
            }
            else{
                memory = Memory.builder().title(title).content(content).groupInfo(new GroupInfo(groupType,groupId)).openType(OpenType.GROUP).stars(star).build();
            }

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
            Memory memory;
            if(groupId == null)
            {
                System.out.println("opentype");
                memory = Memory.builder().title(title).content(content).openType(OpenType.ALL).stars(star).build();
            }
            else{
                memory = Memory.builder().title(title).content(content).groupInfo(new GroupInfo(groupType,groupId)).openType(OpenType.GROUP).stars(star).build();
            }
            memory.setUser(user);
            memory.setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
            memory.setPlaceForMemory(placeOpt.get());
            memory.setKeyword(keyword);
            user.getVisitedPlace().add(placeOpt.get().getId());
            return memoryRepository.save(memory);
        }

    }

    private List<String> getMultipartFileNames(List<MultipartFile> multipartFiles) {

        if(multipartFiles != null)
        {
            List<String> fileNameList = new ArrayList<>();

            multipartFiles.forEach(file->{
                String fileName = createFileName(file.getOriginalFilename());
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try(InputStream inputStream = file.getInputStream()) {
                    uploadService.uploadFile(inputStream,objectMetadata,fileName);
                } catch(IOException e) {
                    throw new GlobalServerException();
                }
                fileNameList.add(uploadService.getFileUrl(fileName));
            });
            return fileNameList;
        }
        return null;
    }

    private String createFileName(String fileName) {
        return "user1/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식 입니다.");
        }
    }
}
