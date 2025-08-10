package org._p1m.foodorderingsystem.features.mail;

import lombok.Data;

@Data
public class EmailRequestDto {
    private String to;
    private String subject;
    private String htmlContent;
}
