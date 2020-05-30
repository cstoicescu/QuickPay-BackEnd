package com.quickpay.service;

import com.quickpay.model.Account;
import com.quickpay.model.Transaction;
import com.quickpay.model.dto.AccountAccessTokenDTO;
import com.quickpay.model.dto.AccountTransactionsDTO;
import com.quickpay.model.dto.TransactionRequestDTO;
import com.quickpay.repository.AccountRepository;
import com.quickpay.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TransactionService {

    private AccountService accountService;

    private TransactionRepository transactionRepository;

    private AccountRepository accountRepository;

    public TransactionService(AccountService accountService, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    // New Transaction with user validation and token

    @Transactional
    public ResponseEntity newTransaction (TransactionRequestDTO transactionRequestDTO) {
        String description = transactionRequestDTO.getDescription();
        String sendingUsername = transactionRequestDTO.getSendingUsername();
        String accessToken = transactionRequestDTO.getAccessToken();
        String receivingAccountNumber = transactionRequestDTO.getReceivingAccountNumber();
        float sendingAmount = transactionRequestDTO.getAmount();
        Account receivingAccount = accountRepository.findAccountByAccountNumber(receivingAccountNumber);
        if( receivingAccount == null) {
            log.error("Account Number " + receivingAccountNumber + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
        Account sendingAccount = accountRepository.findAccountByUsername(sendingUsername);
        float currentAmount = sendingAccount.getBalance();
        if(accountService.authorizeAccess(sendingUsername,accessToken)) {
            if(receivingAccount != null && receivingAccount != sendingAccount) {
                if( currentAmount - sendingAmount < 0) {
                    log.error("Insuficient Balance - " + sendingUsername);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Insuficient Balance");
                }
                Transaction transaction = new Transaction(sendingAmount, description,receivingAccount,sendingAccount);
                sendingAccount.setBalance( currentAmount - sendingAmount);
                sendingAccount.addTransactionSent(transaction);
                currentAmount = receivingAccount.getBalance();
                receivingAccount.setBalance( currentAmount + sendingAmount);
                receivingAccount.addTransactionReceived(transaction);
//                accountRepository.save(receivingAccount);
//                accountRepository.save(sendingAccount);
                transactionRepository.save(transaction);
                log.info("Transaction succes - " + sendingUsername);
                return ResponseEntity.status(HttpStatus.OK).body(transaction);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
    }

    public AccountTransactionsDTO getAllUserTransactions (AccountAccessTokenDTO accountAccessTokenDTO) {
        if(accountService.authorizeAccess(accountAccessTokenDTO.getUsername(),accountAccessTokenDTO.getAccessToken())) {
            Account account = accountRepository.findAccountByUsername(accountAccessTokenDTO.getUsername());
            if (account != null) {
                return new AccountTransactionsDTO(account.getId(),account.getFirstName(),account.getLastName(),
                        account.getTransactionSent(),account.getTransactionReceived());
            }
            return null;
        }
        return null;
    }

    public List<Transaction> getAllTransactions () {
        return transactionRepository.findAll();
    }
}


