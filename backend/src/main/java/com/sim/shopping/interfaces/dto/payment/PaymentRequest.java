package com.sim.shopping.interfaces.dto.payment;

/**
 * 支付请求DTO，包含订单号和支付方式
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class PaymentRequest {
    private String paymentMethod;

    /**
     * 获取Payment Method
     * @return 返回结果
     */
    public String getPaymentMethod() { return this.paymentMethod; }
    /**
     * set Payment Method
     * @param paymentMethod paymentMethod
     */
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
