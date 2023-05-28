package cmc.mellyserver.common.mockapi;

import cmc.mellyserver.common.util.aws.AwsService;

public class MockAwsService implements AwsService {
    @Override
    public Long calculateImageVolume(String bucketName, String username) {
        return 0L;
    }
}
