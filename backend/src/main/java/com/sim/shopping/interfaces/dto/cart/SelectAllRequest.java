package com.sim.shopping.interfaces.dto.cart;

/**
 * SelectAll请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class SelectAllRequest {
    private Integer selected;

    /**
     * 获取Selected
     * @return 返回结果
     */
    public Integer getSelected() { return this.selected; }
    /**
     * set Selected
     * @param selected selected
     */
    public void setSelected(Integer selected) { this.selected = selected; }
}
