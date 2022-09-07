package com.mindhub.homebanking;


import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class RepositoriesTest
{
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;

    /*-------------------LOAN TEST-----------------*/
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    /*-------------------ACCOUNT TEST-----------------*/

    @Test
    public void existAccounts(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }

    @Test
    public void hasBalance(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, everyItem(hasProperty("balance", greaterThanOrEqualTo(0.0))));
    }

    /*-------------------CARD TEST-----------------*/

    @Test
    public void existCards(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    @Test
    public void hasDifferentTypes(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("type", is(CardType.CREDIT))));
    }

    /*-------------------CLIENT TEST-----------------*/

    @Test
    public void existClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void hasPassword(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, everyItem(hasProperty("password",is(not(emptyOrNullString())))));
    }

    /*-------------------TRANSACTION TEST-----------------*/

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void hasDescription(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, everyItem(hasProperty("description",is(not(emptyOrNullString())))));
    }


}
