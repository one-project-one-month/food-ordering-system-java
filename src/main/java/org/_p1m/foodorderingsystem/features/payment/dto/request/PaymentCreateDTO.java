package org._p1m.foodorderingsystem.features.payment.dto.request;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PaymentCreateDTO {
    private Long orderId;
    private MultipartFile paymentScreenshot;
}
