package cmc.mellyserver.common.mock;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cmc.mellyserver.FileDto;
import cmc.mellyserver.FileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("test")
public class MockFileService implements FileService {

    @Override
    public List<String> saveFiles(Long userId, List<FileDto> multipartFiles) {
        return null;
    }

    @Override
    public String saveFile(Long userId, FileDto file) {
        return "수정된프로필.jpg";
    }

    @Override
    public void deleteFile(String fileName) {
    }

    @Override
    public void deleteFiles(List<Long> deleteFileIds) {
    }
}
