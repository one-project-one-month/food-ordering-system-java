package org._p1m.foodorderingsystem.features.files.controller;

import org._p1m.foodorderingsystem.common.storage.StorageService;
import org._p1m.foodorderingsystem.common.storage.StorageServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
public class FileController {

    private final StorageService storageService;

    @Autowired
    public FileController(final StorageServiceFactory factory) {
        this.storageService = factory.getConfiguredStorageService();
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable final String filename) {
        final Resource file = this.storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
