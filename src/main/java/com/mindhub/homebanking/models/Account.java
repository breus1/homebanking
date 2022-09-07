package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity

public class Account {
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Id

//--------------------------------
    private long id;
    private String number;
    private double balance;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy = "account",fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

//--------------------------------

    public Account() {
    }

    public Account(String number, double balance, LocalDateTime creationDate, Client client) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
        this.client = client;
    }

    public Account(String number, double balance, LocalDateTime creationDate) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
    }

//-------------------------------

    public Set<Transaction> getTransactions(){return transactions;}

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

}
