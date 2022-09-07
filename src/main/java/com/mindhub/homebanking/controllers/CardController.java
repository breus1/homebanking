package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.enums.CardColor;
import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")

public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")

    public ResponseEntity<Object> createCard(@RequestParam CardColor cardColor, @RequestParam CardType cardType ,Authentication authentication) {
        Client client = this.clientRepository.findByEmail(authentication.getName());


        if (cardRepository.findByClientAndType(client,cardType).size() >= 3){
            return new ResponseEntity<>("Se sobrepaso del limite de tarjetas, lo lamentamos", HttpStatus.FORBIDDEN);
        }

        Card card = new Card(client.getLastName() + " " + client.getFirstName(), cardType, cardColor, CardUtils.getCardNumber(cardRepository), CardUtils.getCVVNumber(), LocalDateTime.now(), LocalDateTime.now().plusYears(5),client);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(@RequestParam String number, Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Card cardToDelete = this.cardRepository.findByNumber(number);

        if(!client.getCards().contains(cardToDelete)){
            return new ResponseEntity<>("This card doesn't belong to this client", HttpStatus.FORBIDDEN);
        }

        if(cardToDelete == null){
            return new ResponseEntity<>("The card you are trying to delete does not exist",HttpStatus.FORBIDDEN);
        }

        cardRepository.delete(cardToDelete);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/clients/current/cards/payment")
    public ResponseEntity<Object> makeCardTransaction(@RequestBody CardPaymentDTO cardPaymentDTO,
                                                      Authentication authentication){

        Card card = this.cardRepository.findByNumber(cardPaymentDTO.getNumber());
        Account account = this.accountRepository.findByNumber(cardPaymentDTO.getAccountNumber());
        Client client = card.getClient();

        double amount = cardPaymentDTO.getAmount();
        String description = cardPaymentDTO.getDescription();
        Integer cvv = cardPaymentDTO.getCvv();
        String cardNumber = cardPaymentDTO.getNumber();

        if( description.isEmpty() || amount <= 0 || cardNumber.isEmpty())
        {
            return new ResponseEntity<>("Complete all the fields", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("This card doesn't belong to this client", HttpStatus.FORBIDDEN);
        }

        if(card == null)
        {
            return new ResponseEntity<>("The card number you select doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account))
        {
            return new ResponseEntity<>("The account you select do not belong to the client", HttpStatus.FORBIDDEN);
        }

        if(cvv != card.getCvv())
        {
            return new ResponseEntity<>("The Cvv you select isn't correct",HttpStatus.FORBIDDEN);
        }

        if(card.getThruDate().isBefore(LocalDateTime.now()))
        {
            return new ResponseEntity<>("The card you select has expire", HttpStatus.FORBIDDEN);
        }
        if(account.getBalance() < amount)
        {
            return new ResponseEntity<>("There isn't enough balance to make this transaction",HttpStatus.FORBIDDEN);
        }

        Transaction cardTransaction = new Transaction(TransactionType.DEBIT, -amount, description , LocalDateTime.now(), account);

        transactionRepository.save(cardTransaction);

        account.setBalance(account.getBalance() - amount);

        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
