package cmc.mellyserver.mellyappexternalapi.common.mockapi;

import cmc.mellyserver.mellyappexternalapi.common.aws.AwsService;

public class MockAwsService implements AwsService {
	@Override
	public Long calculateImageVolume(String bucketName, String username) {
		return 0L;
	}
}
