package com.mindhub.homebanking.Utils;


import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public final class AccountUtils {
    private AccountUtils(){}


    public static String getAccountNumber(AccountRepository accountRepository){
        int min = 10000000;
        int max = 99999999;
        String number = "VIN-"+ ((int) ((Math.random() * (max - min)) + min));

        while (accountRepository.findByNumber(number) != null){
            number = "VIN-"+ ((int) ((Math.random() * (max - min)) + min));
        }
        return number;
    }
}
