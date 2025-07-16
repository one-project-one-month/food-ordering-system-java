package org._p1m.foodorderingsystem.common.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service("awsS3Service")
public class AwsS3Service implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(AwsS3Service.class);

    public AwsS3Service(/* AmazonS3 s3Client */) {
        // this.s3Client = s3Client;
        log.info("AWS S3 Service is configured (placeholder).");
    }


    @Override
    public void init() {
        log.info("Initializing AWS S3 Service. Checking bucket existence and permissions...");
    }

    @Override
    public String store(final MultipartFile file) {
        log.warn("AWS S3 store method is not yet implemented.");
        // TODO: Implement file upload logic to S3
        // 1. Generate a unique filename
        // 2. Create ObjectMetadata (content type, size, etc.)
        // 3. Use s3Client.putObject(...) to upload the file
        return "s3-placeholder-" + file.getOriginalFilename();
    }

    @Override
    public List<Path> loadAll() {
        log.warn("AWS S3 loadAll method is not yet implemented.");
        // TODO: Implement logic to list objects from the S3 bucket
        return List.of();
    }

    @Override
    public Path load(final String filename) {
        log.warn("AWS S3 load method is not yet implemented.");
        return null;
    }

    @Override
    public Resource loadAsResource(final String filename) {
        log.warn("AWS S3 loadAsResource method is not yet implemented.");
        return null;
    }

    @Override
    public void delete(final String filename) {
        log.warn("AWS S3 delete method is not yet implemented.");
        // TODO: Implement s3Client.deleteObject(...)
    }

    @Override
    public void deleteAll() {
        log.warn("AWS S3 deleteAll method is not yet implemented.");
    }

    @Override
    public String update(MultipartFile newFile, String publicId, String folderName) {
        return "";
    }
}
