package com.hdp.common.web.dto.response;

import java.util.List;

public class PagedResponse<T> extends BaseResponse {
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public PagedResponse() {
    }

    public PagedResponse(List<T> data, int page, int size, long totalElements) {
        super(true, null, null, null);
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasNext = page < totalPages - 1;
        this.hasPrevious = page > 0;
    }

    public static <T> PagedResponse<T> of(List<T> data, int page, int size, long totalElements) {
        return new PagedResponse<>(data, page, size, totalElements);
    }

    public List<T> data() { return data; }
    public void data(List<T> data) { this.data = data; }
    public int page() { return page; }
    public void page(int page) { this.page = page; }
    public int size() { return size; }
    public void size(int size) { this.size = size; }
    public long totalElements() { return totalElements; }
    public void totalElements(long totalElements) { this.totalElements = totalElements; }
    public int totalPages() { return totalPages; }
    public void totalPages(int totalPages) { this.totalPages = totalPages; }
    public boolean hasNext() { return hasNext; }
    public void hasNext(boolean hasNext) { this.hasNext = hasNext; }
    public boolean hasPrevious() { return hasPrevious; }
    public void hasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
}