package cmc.mellyserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {

    private String originalFilename;
    private long size;
    private String contentType;
    private InputStream inputStream;
}
