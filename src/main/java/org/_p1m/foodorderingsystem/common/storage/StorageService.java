package org._p1m.foodorderingsystem.common.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

/**
 * Defines the contract for storage operations.
 * Any storage implementation (local, cloud, etc.) must implement this interface.
 */
public interface StorageService {

    /**
     * Initializes the storage location.
     * This might involve creating directories or checking bucket access.
     */
    void init();

    /**
     * Stores a file.
     *
     * @param file The MultipartFile to be stored.
     * @return The generated filename of the stored file.
     */
    String store(MultipartFile file);

    /**
     * Loads all available file paths.
     *
     * @return A List of Paths to the files.
     */
    List<Path> loadAll();

    /**
     * Loads a single file by its filename.
     *
     * @param filename The name of the file to load.
     * @return A Path to the loaded file.
     */
    Path load(String filename);

    /**
     * Loads a file as a Spring Resource.
     *
     * @param filename The name of the file to load.
     * @return A Resource object for the file.
     */
    Resource loadAsResource(String filename);

    /**
     * Deletes a file by its filename.
     *
     * @param filename The name of the file to delete.
     */
    void delete(String filename);

    /**
     * Deletes all stored files.
     */
    void deleteAll();

    String update(MultipartFile newFile, String publicId, String folderName);
}
