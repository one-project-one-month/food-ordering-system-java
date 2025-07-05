package org._p1m.foodorderingsystem.common.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageServiceFactory {

    private final StorageService localStorageService;
    private final StorageService awsS3Service;
    private final StorageType configuredStorageType;

    @Autowired
    public StorageServiceFactory(
            @Qualifier("localStorageService") final  StorageService localStorageService,
            @Qualifier("awsS3Service") final  StorageService awsS3Service,
            @Value("${app.storage.type:LOCAL}") final  String storageType) {
        this.localStorageService = localStorageService;
        this.awsS3Service = awsS3Service;
        try {
            this.configuredStorageType = StorageType.valueOf(storageType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value for app.storage.type property. Must be 'LOCAL' or 'AWS_S3'.", e);
        }
    }

    public StorageService getConfiguredStorageService() {
        return getStorageService(this.configuredStorageType);
    }

    public StorageService getStorageService(final StorageType storageType) {
        if (storageType == null) {
            return localStorageService;
        }
        return switch (storageType) {
            case LOCAL -> localStorageService;
            case AWS_S3 -> awsS3Service;
        };
    }
}
