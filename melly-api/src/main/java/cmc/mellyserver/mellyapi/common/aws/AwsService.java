package cmc.mellyserver.mellyapi.common.aws;

public interface AwsService {

	Long calculateImageVolume(String bucketName, String username);
}
