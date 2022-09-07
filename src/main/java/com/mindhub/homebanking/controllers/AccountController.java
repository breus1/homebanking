package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.enums.AccountType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping ("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id)
    {
        return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication)
    {
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType type){
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3){
            return new ResponseEntity<>("Client's of accounts limit reached", HttpStatus.FORBIDDEN);
        }
        accountRepository.save(new Account(AccountUtils.getAccountNumber(accountRepository),0 , LocalDateTime.now(),client, type));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(@RequestParam String number, Authentication authentication){
        Account accountToDelete = this.accountRepository.findByNumber(number);
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Set<Transaction> transactionToDelete = accountToDelete.getTransactions();

        if(!client.getAccounts().contains(accountToDelete)){
            return new ResponseEntity<>("This account doesn't belong to this client", HttpStatus.FORBIDDEN);
        }

        if(accountToDelete == null){
            return new ResponseEntity<>("The account you are trying to delete does not exist",HttpStatus.FORBIDDEN);
        }

        if(accountToDelete.getBalance() > 0){
            return new ResponseEntity<>("The account you are trying to delete does not have an empty balance",HttpStatus.FORBIDDEN);
        }

        transactionRepository.deleteAll(transactionToDelete);
        accountRepository.delete(accountToDelete);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
