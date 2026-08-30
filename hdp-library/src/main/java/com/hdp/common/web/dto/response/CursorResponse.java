package com.hdp.common.web.dto.response;

import java.util.List;

public class CursorResponse<T> extends BaseResponse {
    private List<T> data;
    private String nextCursor;
    private boolean hasMore;

    public CursorResponse() {
    }

    public CursorResponse(List<T> data, String nextCursor, boolean hasMore) {
        super(true, null, null, null);
        this.data = data;
        this.nextCursor = nextCursor;
        this.hasMore = hasMore;
    }

    public static <T> CursorResponse<T> of(List<T> data, String nextCursor, boolean hasMore) {
        return new CursorResponse<>(data, nextCursor, hasMore);
    }

    public List<T> data() { return data; }
    public void data(List<T> data) { this.data = data; }
    public String nextCursor() { return nextCursor; }
    public void nextCursor(String nextCursor) { this.nextCursor = nextCursor; }
    public boolean hasMore() { return hasMore; }
    public void hasMore(boolean hasMore) { this.hasMore = hasMore; }
}