package com.sim.shopping.interfaces.dto.settlement;

import java.math.BigDecimal;

/**
 * ShopFinance响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ShopFinanceResponse {

    private BigDecimal balance;
    private BigDecimal totalIncome;
    private BigDecimal totalSettled;
    private BigDecimal frozenAmount;
    private BigDecimal todayIncome;

    /**
     * 获取Balance
     * @return 返回结果
     */
    public BigDecimal getBalance() { return this.balance; }
    /**
     * set Balance
     * @param balance balance
     */
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    /**
     * 获取Total Income
     * @return 返回结果
     */
    public BigDecimal getTotalIncome() { return this.totalIncome; }
    /**
     * set Total Income
     * @param totalIncome totalIncome
     */
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    /**
     * 获取Total Settled
     * @return 返回结果
     */
    public BigDecimal getTotalSettled() { return this.totalSettled; }
    /**
     * set Total Settled
     * @param totalSettled totalSettled
     */
    public void setTotalSettled(BigDecimal totalSettled) { this.totalSettled = totalSettled; }
    /**
     * 获取Frozen Amount
     * @return 返回结果
     */
    public BigDecimal getFrozenAmount() { return this.frozenAmount; }
    /**
     * set Frozen Amount
     * @param frozenAmount frozenAmount
     */
    public void setFrozenAmount(BigDecimal frozenAmount) { this.frozenAmount = frozenAmount; }
    /**
     * 获取Today Income
     * @return 返回结果
     */
    public BigDecimal getTodayIncome() { return this.todayIncome; }
    /**
     * set Today Income
     * @param todayIncome todayIncome
     */
    public void setTodayIncome(BigDecimal todayIncome) { this.todayIncome = todayIncome; }
}
