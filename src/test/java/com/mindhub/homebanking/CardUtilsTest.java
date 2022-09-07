package com.mindhub.homebanking;

import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardUtilsTest {

    @Autowired
    CardRepository cardRepository;

    /*-------------------CARD UTIL TEST-----------------*/

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber(cardRepository);
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void CVVNumberIsCreated(){

        int CVVNumber = CardUtils.getCVVNumber();
        assertThat(CVVNumber, lessThanOrEqualTo(999));
    }

}
