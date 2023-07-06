package cmc.mellyserver.mellyinfra.aws;

import cmc.mellyserver.mellycore.common.aws.AwsService;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile({"local", "prod"})
public class AwsServiceImpl implements AwsService {

    private final AmazonS3Client amazonS3Client;

    @Override
    public Long calculateImageVolume(String bucketName, String username) {
        ObjectListing mellyimage = amazonS3Client.listObjects(bucketName, username);
        List<S3ObjectSummary> objectSummaries = mellyimage.getObjectSummaries();
        return objectSummaries.stream().mapToLong(S3ObjectSummary::getSize).sum();
    }
}
