package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Id
    private long ID;

    private String name;

    private double maxAmount;

    private double interest;

    @ElementCollection
    @Column(name ="payments")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments, double interest) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
    }

    public Set<ClientLoan> getClientLoans(){return clientLoans;}
    public void setClientLoans(Set<ClientLoan>clientLoans){this.clientLoans = clientLoans;}

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}
