package com.mindhub.homebanking.Utils;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

public final class CardUtils {
    private CardUtils(){}

    @Autowired
    private CardRepository cardRepository;

    public static String getCardNumber(int min, int max, CardRepository cardRepository){
        String number = (int) ((Math.random()* (max - min)) + min) + "-" +
                        (int) ((Math.random()* (max - min)) + min) + "-" +
                        (int) ((Math.random()* (max - min)) + min) + "-" +
                        (int) ((Math.random()* (max - min)) + min);

        while (cardRepository.findByNumber(number) != null){
            number = (int) ((Math.random()* (max - min)) + min) + "-" +
                     (int) ((Math.random()* (max - min)) + min) + "-" +
                     (int) ((Math.random()* (max - min)) + min) + "-" +
                     (int) ((Math.random()* (max - min)) + min);}

        return number;
    }

    public static int getCVVNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }
}
