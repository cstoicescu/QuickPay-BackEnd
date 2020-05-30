package com.quickpay.controller;

import com.quickpay.model.dto.AccountLoginRequestDTO;
import com.quickpay.model.dto.AccountAccessTokenDTO;
import com.quickpay.model.dto.AccountRegisterRequestDTO;
import com.quickpay.model.dto.AccountRegisterResponseDTO;
import com.quickpay.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins="http://localhost:8081", maxAge = 2000)
@RestController
@Slf4j
public class AccountController {

    private AccountService accountService;

    public AccountController ( AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity registerAccount (@RequestBody AccountRegisterRequestDTO accountRegisterRequestDTO) {
        // Validation. Check if username or accountNumber already exists
        if (accountService.checkUsername(accountRegisterRequestDTO.getUsername())) {
            log.error("Username: " + accountRegisterRequestDTO.getUsername() + " is already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username " + accountRegisterRequestDTO.getUsername() + " already exists");
        }
        if(accountService.checkAccountNumber(accountRegisterRequestDTO.getAccountNumber()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account Number " + accountRegisterRequestDTO.getAccountNumber()
                    + " is already alocated to an existing account");

        AccountRegisterResponseDTO accountRegisterResponseDTO = accountService.register(accountRegisterRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account " + accountRegisterRequestDTO.getUsername() + " created succesfully");
    }

    @PostMapping("/login")
    public ResponseEntity login (@RequestBody AccountLoginRequestDTO accountLoginRequestDTO) {
        if(accountService.checkLogin(accountLoginRequestDTO)) {
            AccountAccessTokenDTO accountLoginResponseDTO = accountService.requestLogin(accountLoginRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).header("Status", "Login succesfully").body(accountLoginResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).header("Status","Login failed").body("Invalid Credentials");
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity getUserData (@PathVariable String username, @RequestParam String accessToken) {
        ResponseEntity responseEntity = accountService.getUserData(username,accessToken);
        return responseEntity;
    }
    @PostMapping("/logout")
    public ResponseEntity logout (@RequestBody AccountAccessTokenDTO accountLoginResponseDTO) {
        if (accountService.logOut(accountLoginResponseDTO))
            return ResponseEntity.status(HttpStatus.OK).body("Logout succesfully");
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Logout failed");
    }

    @GetMapping("/allusers")
    public ResponseEntity getAllUsers() {
        ResponseEntity responseEntity = accountService.getAllUsers();
        return responseEntity;
    }
}
