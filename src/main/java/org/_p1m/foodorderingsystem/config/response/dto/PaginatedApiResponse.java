package org._p1m.foodorderingsystem.config.response.dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginatedApiResponse<T>  {
	private int success; 
    private int code; 
    private String message;
    private Map<String, Object> meta;
    
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private List<T> data;
}
