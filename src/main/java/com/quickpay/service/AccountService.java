package com.quickpay.service;


import com.quickpay.model.Account;
import com.quickpay.model.dto.*;
import com.quickpay.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountService {

    public AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // username validation
    public boolean checkUsername(String username) {
        if (accountRepository.findAccountByUsername(username.toLowerCase()) == null) {

            return false;
        }
        return true;
    }

    // accountNumber validation
    public boolean checkAccountNumber(String accountNumber) {
        if (accountRepository.findAccountByAccountNumber(accountNumber) == null) {

            return false;
        }
        log.error("Account Number :" + accountNumber + " already assignated to a user");
        return true;
    }

    //register
    public AccountRegisterResponseDTO register(AccountRegisterRequestDTO accountRegisterRequestDTO) {
        Account account = new Account(accountRegisterRequestDTO.getFirstName(), accountRegisterRequestDTO.getLastName(), accountRegisterRequestDTO.getUsername(),
                accountRegisterRequestDTO.getAccountNumber(), accountRegisterRequestDTO.getPassword(), accountRegisterRequestDTO.getBalance());
        accountRepository.save(account);
        log.info("Register succesful : " + account.getUsername());
        return new AccountRegisterResponseDTO(account.getFirstName(), account.getLastName(), account.getAccountNumber(), account.getBalance());
    }

    //credentials validations
    public boolean checkLogin(AccountLoginRequestDTO accountLoginRequestDTO) {
        // Login Validation. If username found and passwords match then set isLogged to true;
        Account checkedAccount = accountRepository.findAccountByUsername(accountLoginRequestDTO.getUsername().toLowerCase());
        if (checkedAccount != null) {
            if (checkedAccount.getPassword().equals(accountLoginRequestDTO.getPassword())) {
                return true;
            }
            log.error("Login failed :" + checkedAccount.getUsername());
            return false;
        }
        log.error("Login failed :" + accountLoginRequestDTO.getUsername());
        return false;
    }

    public boolean logOut(AccountAccessTokenDTO accountLoginResponseDTO) {
        // Logout Validation because we don't want other users to be able to logout or close session for other logged users.
        //FRONT END button Logout eventlistener -> send accessToken from session storage to backend,
        // so that user doesn't have to reenter credentials for logout.
        Account checkedAccount = accountRepository.findAccountByUsername(accountLoginResponseDTO.getUsername().toLowerCase());
        System.out.println(checkedAccount);
        if (checkedAccount != null) {
            if (checkedAccount.getAccessToken().equals(accountLoginResponseDTO.getAccessToken())) {
                checkedAccount.setAccessToken(null);
                checkedAccount.setLogged(false);
                accountRepository.save(checkedAccount);
                log.info("Logout Succesful " + checkedAccount.getUsername());
                return true;
            }
            return false;
        }
        return false;
    }

    // Login request
    public AccountAccessTokenDTO requestLogin(AccountLoginRequestDTO accountLoginRequestDTO) {
        if (checkLogin(accountLoginRequestDTO)) {
            Account checkedAccount = accountRepository.findAccountByUsername(accountLoginRequestDTO.getUsername());
            String accessToken = generateLogOutKey();
            checkedAccount.setLogged(true);
            checkedAccount.setAccessToken(accessToken);
            accountRepository.save(checkedAccount);
            log.info("Login succesful : " + checkedAccount.getUsername());
            log.info("Access Token Generated : " + checkedAccount.getUsername() + " - " + accessToken);
            return new AccountAccessTokenDTO(accountLoginRequestDTO.getUsername(),
                    accountRepository.findAccountByUsername(accountLoginRequestDTO.getUsername()).getAccessToken());
        }
        return null;
    }

    // Generating access tokens with apache commons
    private String generateLogOutKey() {
        String generatedString = RandomStringUtils.randomAlphanumeric(15);
        return generatedString;
    }

    // Getting user data
    // changed to call to method : username and access Token validation
    public ResponseEntity getUserData(String username, String accessToken) {
        if (authorizeAccess(username,accessToken)) {
            Account userAccount = accountRepository.findAccountByUsername(username);
            AccountGetUserResponseDTO userData = new AccountGetUserResponseDTO(userAccount.getFirstName(),
                    userAccount.getLastName(),userAccount.getAccountNumber(),userAccount.getBalance(),
                    userAccount.getTransactionSent(),userAccount.getTransactionReceived());
            log.info("Get " + username + " data");
                return ResponseEntity.status(HttpStatus.OK).body(userData);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
    }

    // Username and access Token Validation
    public boolean authorizeAccess (String username, String accessToken) {
        if (checkUsername(username)) {
            Account userAccount = accountRepository.findAccountByUsername(username);
            if (accessToken.equals(userAccount.getAccessToken())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public ResponseEntity getAllUsers() {
        List<Account> users = accountRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


}
