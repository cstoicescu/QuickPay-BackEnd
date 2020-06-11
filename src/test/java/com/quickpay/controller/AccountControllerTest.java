package com.quickpay.controller;

import com.quickpay.model.dto.AccountLoginRequestDTO;
import com.quickpay.model.dto.AccountRegisterRequestDTO;
import com.quickpay.service.AccountService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

//    @Rule
//    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Mock
    private RestTemplate restTemplate;

    // LOGIN test when auth success
    @Test
    public void when_loginOK_expect_200K() {
        AccountLoginRequestDTO accountLoginRequestDTO = new AccountLoginRequestDTO("cstoicescu","password");
        when(accountService.checkLogin(accountLoginRequestDTO)).thenReturn(true);
        ResponseEntity responseEntity = accountController.login(accountLoginRequestDTO);
        assertEquals(responseEntity.getStatusCode(),HttpStatus.OK);
    }

    // LOGIN test when auth fail
    @Test
    public void when_loginFailed_expect_409CONFLICT() {
       AccountLoginRequestDTO accountLoginRequestDTO = new AccountLoginRequestDTO("cstoicescu","password");
       when(accountService.checkLogin(accountLoginRequestDTO)).thenReturn(false);

       ResponseEntity responseEntity = accountController.login(accountLoginRequestDTO);

       assertSame(responseEntity.getStatusCode(),HttpStatus.CONFLICT);
    }
    // REGISTER test when Username  is taken
    @Test
    public void when_register_usernameTaken_expect409CONFLICT() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "Catalin","Stoicescu","cstoicescu","4RO5EF8543G","pass",800);
        when(accountService.checkUsername(accountRegisterRequestDTO.getUsername())).thenReturn(true);
        ResponseEntity responseEntity = accountController.registerAccount(accountRegisterRequestDTO);

        assertSame(responseEntity.getStatusCode(),HttpStatus.CONFLICT);
    }

    // REGISTER test when account number is taken
    @Test
    public void when_register_accountNumber_taken_expect409CONFLICT() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "Catalin","Stoicescu","cstoicescu","4RO5EF8543G","pass",800);
        when(accountService.checkUsername(accountRegisterRequestDTO.getUsername())).thenReturn(false); // username is not taken
        when(accountService.checkAccountNumber(accountRegisterRequestDTO.getAccountNumber())).thenReturn(true); // account number is taken

        ResponseEntity responseEntity = accountController.registerAccount(accountRegisterRequestDTO);

        assertSame(responseEntity.getStatusCode(), HttpStatus.CONFLICT);
    }

    // REGISTER when ok then HTTP status 201 CREATED
    @Test
    public void when_registerOK_expect201CREATED() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "Catalin","Stoicescu","cstoicescu","4RO5EF8543G","pass",800);
        when(accountService.checkUsername(accountRegisterRequestDTO.getUsername())).thenReturn(false); // username is not taken
        when(accountService.checkAccountNumber(accountRegisterRequestDTO.getAccountNumber())).thenReturn(false); // account number is not taken

        ResponseEntity responseEntity = accountController.registerAccount(accountRegisterRequestDTO);

        assertSame(responseEntity.getStatusCode(),HttpStatus.CREATED);
    }
}