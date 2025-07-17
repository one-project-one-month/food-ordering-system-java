package org._p1m.foodorderingsystem.features.menu.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class UploadMenuImageRequest {

    @Schema(type = "string", format = "binary", description = "The image file to upload")
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

