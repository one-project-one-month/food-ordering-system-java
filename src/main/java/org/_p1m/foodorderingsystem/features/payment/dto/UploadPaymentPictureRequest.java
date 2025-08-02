package org._p1m.foodorderingsystem.features.payment.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public class UploadPaymentPictureRequest {

    @Schema(type = "string", format = "binary", description = "The image file to upload")
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
