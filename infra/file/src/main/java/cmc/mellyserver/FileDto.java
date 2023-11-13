package cmc.mellyserver;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {

  private String originalFilename;

  private long size;

  private String contentType;

  private InputStream inputStream;

}
