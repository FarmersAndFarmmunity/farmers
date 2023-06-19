package com.shop.farmers.base.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Uploader {

    private static final String S3_BUCKET_DIRECTORY_NAME = "static";

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile, String imgName) {
        // 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());


        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, imgName, inputStream, objectMetadata));
//            .withCannedAcl(CannedAccessControlList.PublicRead)
//            S3에 putObject 를 할 때에는 withCannedAcl(CannedAccessControlList.PublicRead)옵션으로 설정하여 퍼블릭 액세스를 허용 -> AmazonS3Exception 발생으로 제거
        } catch (Exception e) {
            log.error("S3 파일 업로드에 실패했습니다: ", e);
            throw new RuntimeException("S3 파일 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, imgName).toString();
    }
}

