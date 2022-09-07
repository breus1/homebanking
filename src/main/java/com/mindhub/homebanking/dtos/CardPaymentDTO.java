package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Transaction;

public class CardPaymentDTO {

    private  String accountNumber;
    private String cardNumber;
    private int cvv;
    private double amount;
    private String description;

    public CardPaymentDTO(String accountNumber, String cardNumber, Integer cvv, double amount, String description) {
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getNumber() {
        return cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
