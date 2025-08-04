package org._p1m.foodorderingsystem.common.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils; // Utility for I/O operations in v1 SDK

import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource; // Used for Resource type
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("awsS3Service")
public class AwsS3Service implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(AwsS3Service.class);

    private final AmazonS3 s3Client;
	private final String bucketName;
	public AwsS3Service(AmazonS3 s3Client, @Value("${s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        log.info("AWS S3 Service initialized with bucket: {}", bucketName);
    }

    @Override
    public void init() {
        log.info("Checking if bucket '{}' exists...", bucketName);
        try {
            if (s3Client.doesBucketExistV2(bucketName)) {
                log.info("Bucket '{}' is accessible.", bucketName);
            } else {
                log.error("Bucket '{}' does not exist.", bucketName);
                 s3Client.createBucket(bucketName);
            }
        } catch (AmazonS3Exception e) {
            log.error("Error checking bucket '{}': {}", bucketName, e.getMessage());
        }
    }

    @Override
    public String store(final MultipartFile file) {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest putRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
            s3Client.putObject(putRequest);

            log.info("File '{}' uploaded to bucket as '{}'", file.getOriginalFilename(), key);
            return key;
        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new RuntimeException("S3 upload failed", e);
        } catch (AmazonS3Exception e) {
            log.error("S3 error during upload: {}", e.getMessage());
            throw new RuntimeException("S3 upload failed", e);
        }
    }

    @Override
    public List<Path> loadAll() {
        try {
            ObjectListing objectListing = s3Client.listObjects(bucketName);
            return objectListing.getObjectSummaries().stream()
                    .map(S3ObjectSummary::getKey)
                    .map(Path::of)
                    .collect(Collectors.toList());
        } catch (AmazonS3Exception e) {
            log.error("Failed to list files in S3 bucket: {}", e.getMessage());
            throw new RuntimeException("S3 list failed", e);
        }
    }

    @Override
    public Path load(String filename) {
        return Path.of(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, filename));
            byte[] content = IOUtils.toByteArray(s3Object.getObjectContent());
            return new ByteArrayResource(content);
        } catch (AmazonS3Exception | IOException e) {
            log.error("Failed to load file '{}' from S3: {}", filename, e.getMessage());
            throw new RuntimeException("S3 download failed", e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, filename));
            log.info("Deleted file '{}' from S3 bucket", filename);
        } catch (AmazonS3Exception e) {
            log.error("Failed to delete file '{}' from S3: {}", filename, e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        List<Path> allFiles = loadAll();
        allFiles.forEach(p -> delete(p.toString()));
    }

    @Override
    public String update(MultipartFile newFile, String publicId, String folderName) {
        delete(publicId);
        return store(newFile);
    }
}