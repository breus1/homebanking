package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")

public class LoanController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping("/loans")
    public List<LoanDTO> getAllLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyingForLoans(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication)
    {
        Loan loan = this.loanRepository.findById(loanApplicationDTO.getLoanId());
        Account toAccount = this.accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments()<= 0)
        {
            return new ResponseEntity<>("Complete all the fields", HttpStatus.FORBIDDEN);
        }

        if(loan == null)
        {
            return new ResponseEntity<>("The loan you select doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount())
        {
            return new ResponseEntity<>("The amount you select exceeds the max amount of loan", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments()))
        {
            return new ResponseEntity<>("The payments you select exceeds the max amount of payments", HttpStatus.FORBIDDEN);
        }

        if(toAccount == null)
        {
            return new ResponseEntity<>("The account you select does not exist", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(toAccount))
        {
            return new ResponseEntity<>("The account you select doesn't belong a authenticated client", HttpStatus.FORBIDDEN);
        }

        double interest = loan.getInterest() * loanApplicationDTO.getAmount()/100 + loanApplicationDTO.getAmount();

        ClientLoan clientLoan = new ClientLoan(interest, loanApplicationDTO.getPayments(), client, loan);
        clientLoanRepository.save(clientLoan);

        String description = "Loan approved";
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() +" "+ description , LocalDateTime.now(), toAccount);
        transactionRepository.save(transaction);

        toAccount.setBalance(toAccount.getBalance() + loanApplicationDTO.getAmount());
        accountRepository.save(toAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
