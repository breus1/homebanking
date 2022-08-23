package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.enums.CardColor;
import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")

public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)

    public ResponseEntity<Object> createCard(@RequestParam CardColor cardColor, @RequestParam CardType cardType ,Authentication authentication) {
        Client client = this.clientRepository.findByEmail(authentication.getName());


        if (cardRepository.findByClientAndType(client,cardType).size() >= 3){
            return new ResponseEntity<>("Se sobrepaso del limite de tarjetas, lo lamentamos", HttpStatus.FORBIDDEN);
        }

        Card card = new Card(client.getLastName() + " " + client.getFirstName(), cardType, cardColor, CardUtils.getCardNumber(1000,9999, cardRepository), CardUtils.getCVVNumber(100,999), LocalDateTime.now(), LocalDateTime.now().plusYears(5),client);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
