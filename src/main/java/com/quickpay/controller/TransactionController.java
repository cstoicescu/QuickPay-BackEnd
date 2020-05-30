package com.quickpay.controller;


import com.quickpay.model.dto.AccountAccessTokenDTO;
import com.quickpay.model.dto.TransactionRequestDTO;
import com.quickpay.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:8081", maxAge = 2000)
@RestController
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController (TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction/new")
    public ResponseEntity newTransaction (@RequestBody TransactionRequestDTO transactionRequestDTO) {
        ResponseEntity transactionResponse = transactionService.newTransaction(transactionRequestDTO);
        return transactionResponse;
    }

    @GetMapping("/transaction/all")
    public ResponseEntity allUserTransactions(@RequestBody AccountAccessTokenDTO accountAccessTokenDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getAllUserTransactions(accountAccessTokenDTO));
    }

    @GetMapping("/alltransactions")
    public ResponseEntity getAllTransactions () {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getAllTransactions());
    }
}
