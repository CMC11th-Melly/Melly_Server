package cmc.mellyserver.mellycore.common.aws;

public interface AwsService {

    Long calculateImageVolume(String bucketName, String username);
}
