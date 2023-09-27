package com.example.file;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class FileDto {

    private final String originalFilename;
    private final long size;
    private final String contentType;
    private final InputStream inputStream;
}
