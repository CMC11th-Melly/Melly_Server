package cmc.mellyserver.common.util.aws;

public interface AwsService {

    Long calculateImageVolume(String bucketName, String username);
}
