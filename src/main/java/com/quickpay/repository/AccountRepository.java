package com.quickpay.repository;

import com.quickpay.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByUsername (String username);
    Account findAccountByAccountNumber (String accountNumber);
}
