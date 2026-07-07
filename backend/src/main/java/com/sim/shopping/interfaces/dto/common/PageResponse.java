package com.sim.shopping.interfaces.dto.common;


import java.util.List;
public class PageResponse<T> {

    private List<T> list;
    private long total;
    private int page;
    private int size;
    private int totalPages;

    public PageResponse() {
    }

    public PageResponse(List<T> list, long total, int page, int size) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = size > 0 ? (int) Math.ceil((double) total / size) : 0;
    }

    public static <T> PageResponse<T> of(List<T> list, long total, int page, int size) {
        return new PageResponse<>(list, total, page, size);
    }

    public List<T> getList() { return this.list; }
    public void setList(List<T> list) { this.list = list; }
    public long getTotal() { return this.total; }
    public void setTotal(long total) { this.total = total; }
    public int getPage() { return this.page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return this.size; }
    public void setSize(int size) { this.size = size; }
    public int getTotalPages() { return this.totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}
