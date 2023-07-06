package cmc.mellyserver.mellyapi.common.mockapi;

public class MockAwsService implements AwsService {
    @Override
    public Long calculateImageVolume(String bucketName, String username) {
        return 0L;
    }
}
