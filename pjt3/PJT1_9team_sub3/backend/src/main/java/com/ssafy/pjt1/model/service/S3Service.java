package com.ssafy.pjt1.model.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;

import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

@Service
@NoArgsConstructor
public class S3Service {
    private AmazonS3 s3Client;

    public static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region).build();
    }

    public String upload(MultipartFile file) throws IOException {
        // String fileName = file.getOriginalFilename();
        Date date = new Date();
        StringBuilder sb = new StringBuilder();

        sb.append(date.getTime());
        sb.append("|");
        sb.append(file.getOriginalFilename());

        // 파일명에 따라 ContentType을 설정
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(Mimetypes.getInstance().getMimetype(file.getOriginalFilename()));

        // byte length를 추가
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        objMeta.setContentLength(bytes.length);

        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

        logger.info("bucket!!!!!!! "+bucket);
        logger.info("key!!!!!!! "+sb.toString());
        s3Client.putObject(new PutObjectRequest(bucket, sb.toString(), byteArrayIs, objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, sb.toString()).toString();
    }

    public void delete(String url) throws IOException {
        try {
            String key = url.substring(51);
            //Delete 객체 생성
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucket, key);
            //Delete
            this.s3Client.deleteObject(deleteObjectRequest);

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }
    
}
