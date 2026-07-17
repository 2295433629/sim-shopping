package com.sim.shopping.interfaces.dto.common;


import java.util.List;
/**
 * 分页响应DTO，封装分页数据和分页信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * of
     * @param list list
     * @param total total
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public static <T> PageResponse<T> of(List<T> list, long total, int page, int size) {
        return new PageResponse<>(list, total, page, size);
    }

    /**
     * 查询列表
     * @return 返回结果
     */
    public List<T> getList() { return this.list; }
    /**
     * set List
     * @param list list
     */
    public void setList(List<T> list) { this.list = list; }
    /**
     * 获取Total
     * @return 返回结果
     */
    public long getTotal() { return this.total; }
    /**
     * set Total
     * @param total total
     */
    public void setTotal(long total) { this.total = total; }
    /**
     * 分页查询
     * @return 返回结果
     */
    public int getPage() { return this.page; }
    /**
     * set Page
     * @param page page
     */
    public void setPage(int page) { this.page = page; }
    /**
     * 获取Size
     * @return 返回结果
     */
    public int getSize() { return this.size; }
    /**
     * set Size
     * @param size size
     */
    public void setSize(int size) { this.size = size; }
    /**
     * 获取Total Pages
     * @return 返回结果
     */
    public int getTotalPages() { return this.totalPages; }
    /**
     * set Total Pages
     * @param totalPages totalPages
     */
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}
