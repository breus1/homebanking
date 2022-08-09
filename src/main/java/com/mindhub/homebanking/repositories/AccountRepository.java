package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource //que puede usar "rest" -> generacion de rutas
public interface AccountRepository extends JpaRepository<Account, Long> {

}

