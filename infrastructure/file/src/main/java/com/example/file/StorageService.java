package com.example.file;


import java.io.IOException;
import java.util.List;

public interface StorageService {

    List<String> saveFileList(Long userId, List<FileDto> multipartFiles);

    String saveFile(Long userId, FileDto file);

    void deleteFile(String fileName) throws IOException;

    Long calculateImageVolume(String username);
}
