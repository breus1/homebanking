package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String name;
    private double amount;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getID();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.loanId = clientLoan.getLoan().getID();
        this.name = clientLoan.getLoan().getName();
    }

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }
}
