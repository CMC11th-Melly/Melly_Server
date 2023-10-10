package cmc.mellyserver;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class FileDto {

    private String originalFilename;
    private long size;
    private String contentType;
    private InputStream inputStream;
}
