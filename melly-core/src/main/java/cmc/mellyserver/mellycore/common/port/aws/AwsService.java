package cmc.mellyserver.mellycore.common.port.aws;

public interface AwsService {

    Long calculateImageVolume(String bucketName, String username);
}
