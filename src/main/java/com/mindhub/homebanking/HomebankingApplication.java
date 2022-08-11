package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(HomebankingApplication.class, args);
	}

	/*El bean hace que se puedan traer cosas de todos lados*/
	@Bean
	public CommandLineRunner InitData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository)
	{
		return (args) ->
		{
			Client client1 = new Client("Melba", "Lorenzo", "melba@mindhub.com");

			Account account1 = new Account("VIN001", 5000,  LocalDateTime.now());
			Account account2 = new Account("VIN002",7500,  LocalDateTime.now().plusDays(1));

			Client client2 = new Client("Bruno", "Reus", "brunoreus04@gmail.com");

			Account account3 = new Account("VIN003", 10000,  LocalDateTime.now());
			Account account4 = new Account("VIN004",30000,  LocalDateTime.now());

			Loan loan1 = new Loan("Hipotecario",500000, Arrays.asList(12,24,36,48,60));
			Loan loan2 = new Loan("Personal",100000,Arrays.asList(6,12,24));
			Loan loan3 = new Loan("Automotriz",300000,Arrays.asList(6,12,24,36));

			ClientLoan clientLoan1 = new ClientLoan(400000, 60, client1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, client1, loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24, client2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36, client2, loan3);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 2000, "transferencia recibida" ,LocalDateTime.now(), account1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -4000, "Compra tienda xx" ,LocalDateTime.now(), account1);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 1000, "transferencia recibida" ,LocalDateTime.now(), account2);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -200, "Compra tienda xy" ,LocalDateTime.now(), account2);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 8000, "transferencia recibida" ,LocalDateTime.now(), account3);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -2000, "Compra tienda xz" ,LocalDateTime.now(), account3);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 700, "transferencia recibida" ,LocalDateTime.now(), account4);
			Transaction transaction8 = new Transaction(TransactionType.DEBIT, -2000, "Compra tienda xi" ,LocalDateTime.now(), account4);


			client1.addAccount(account1);
			client1.addAccount(account2);

			client2.addAccount(account3);
			client2.addAccount(account4);


			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

		};
	}

}
