package com.sim.shopping.interfaces.dto.settlement;

import java.math.BigDecimal;

public class ShopFinanceResponse {

    private BigDecimal balance;
    private BigDecimal totalIncome;
    private BigDecimal totalSettled;
    private BigDecimal frozenAmount;
    private BigDecimal todayIncome;

    public BigDecimal getBalance() { return this.balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getTotalIncome() { return this.totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    public BigDecimal getTotalSettled() { return this.totalSettled; }
    public void setTotalSettled(BigDecimal totalSettled) { this.totalSettled = totalSettled; }
    public BigDecimal getFrozenAmount() { return this.frozenAmount; }
    public void setFrozenAmount(BigDecimal frozenAmount) { this.frozenAmount = frozenAmount; }
    public BigDecimal getTodayIncome() { return this.todayIncome; }
    public void setTodayIncome(BigDecimal todayIncome) { this.todayIncome = todayIncome; }
}
