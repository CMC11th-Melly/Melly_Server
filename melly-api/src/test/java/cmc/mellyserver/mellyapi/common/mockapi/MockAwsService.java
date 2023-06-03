package cmc.mellyserver.mellyapi.common.mockapi;

import cmc.mellyserver.mellyapi.common.aws.AwsService;

public class MockAwsService implements AwsService {
	@Override
	public Long calculateImageVolume(String bucketName, String username) {
		return 0L;
	}
}
