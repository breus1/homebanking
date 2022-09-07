package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.enums.CardColor;
import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> makeTransaction(@RequestParam String fromAccountNumber,
                                                  @RequestParam String toAccountNumber,
                                                  @RequestParam double amount,
                                                  @RequestParam String description,
                                                  Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());
        Account fromAccount = this.accountRepository.findByNumber(fromAccountNumber);
        Account toAccount = this.accountRepository.findByNumber(toAccountNumber);

        if( description.isEmpty() || amount <= 0 || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty())
        {
            return new ResponseEntity<>("Complete all the fields", HttpStatus.FORBIDDEN);
        }

        if(fromAccountNumber.equals(toAccountNumber))
        {
            return new ResponseEntity<>("The accounts numbers are the same", HttpStatus.FORBIDDEN);
        }

        if(fromAccount == null)
        {
            return new ResponseEntity<>("The account number you select doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(fromAccount))
        {
            return new ResponseEntity<>("The account you select do not belong to the client", HttpStatus.FORBIDDEN);
        }

        if(toAccount == null)
        {
            return new ResponseEntity<>("The account receiver doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(fromAccount.getBalance() < amount)
        {
            return new ResponseEntity<>("There isn't enough balance to make this transaction",HttpStatus.FORBIDDEN);
        }

        Transaction transaction_origin = new Transaction(TransactionType.DEBIT, -amount, toAccountNumber + " " + description , LocalDateTime.now(), fromAccount);
        Transaction transaction_destination = new Transaction(TransactionType.CREDIT, amount, fromAccountNumber + " " + description , LocalDateTime.now(), toAccount);

        transactionRepository.save(transaction_origin);
        transactionRepository.save(transaction_destination);


        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }


}
