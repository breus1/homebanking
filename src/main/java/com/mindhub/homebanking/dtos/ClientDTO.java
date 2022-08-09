package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import javax.persistence.Id;
import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private long id;
    private String firstName, lastName, email;

    private Set<AccountDTO>accounts = new HashSet<>();

    public ClientDTO(){
    }

    public ClientDTO(Client client){
        this.id = client.getID();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }

    public long getId(){
        return id;
    }

    public Set<AccountDTO>getAccounts(){
        return accounts;
    }


}
