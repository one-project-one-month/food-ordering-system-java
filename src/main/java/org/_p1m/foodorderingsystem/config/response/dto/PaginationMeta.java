package org._p1m.foodorderingsystem.config.response.dto;

import lombok.Data;

@Data
public class PaginationMeta {
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private String method;
    private String endpoint;
}
