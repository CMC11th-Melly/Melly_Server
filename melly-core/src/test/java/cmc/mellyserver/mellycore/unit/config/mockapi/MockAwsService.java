package cmc.mellyserver.mellycore.unit.config.mockapi;

import cmc.mellyserver.mellycore.common.port.aws.AwsService;

public class MockAwsService implements AwsService {
    @Override
    public Long calculateImageVolume(String bucketName, String username) {
        return 0L;
    }
}
