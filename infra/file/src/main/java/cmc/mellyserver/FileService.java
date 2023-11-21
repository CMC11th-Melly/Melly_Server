package cmc.mellyserver;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<String> saveFileList(Long userId, List<FileDto> multipartFiles);

    String saveFile(Long userId, FileDto file);

    void deleteFile(String fileName) throws IOException;

    void deleteFileList(List<Long> deleteFileIds);

    long calculateImageVolume(String username);

}
