package org._p1m.foodorderingsystem.common.storage;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cloudinaryStorageService")
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public void init() {

    }

    @Override
    public String store(MultipartFile file) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", "food_ordering_system");
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload to Cloudinary", e);
        }
    }

    @Override
    public List<Path> loadAll() {
        return List.of();
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void delete(String publicId) {
        try {
            Map<String, Object> deleteOptions = new HashMap<>();
            deleteOptions.put("invalidate", true);
            this.cloudinary.uploader().destroy(publicId, deleteOptions);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete from Cloudinary", e);
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public String update(MultipartFile newFile, String publicId, String folderName) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("public_id", folderName + "/" + publicId);
            options.put("overwrite", true);

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadedFile = (Map<String, Object>) cloudinary.uploader().upload(newFile.getBytes(), options);
            return (String) uploadedFile.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Failed to update image on Cloudinary", e);
        }
    }
}