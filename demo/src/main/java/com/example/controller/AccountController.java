package com.example.controller;

import com.example.exception.AccountError;
import com.example.exception.ResourceExistException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Account;
import com.example.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @GetMapping("/Account/{idendityid}")
    public Account getAccount(@PathVariable("idendityid") String idendityId) {
        var account = accountRepository.findById(idendityId)
                .orElseThrow(() -> new ResourceNotFoundException(idendityId));
        return account;

    }

    @PostMapping("/Account")
    @Transactional
    public Account createAccount(@RequestBody Account account) {
        var accountresult = accountRepository.findById(account.getIdentityid());
        if (!accountresult.isEmpty()) {
            throw new ResourceExistException(account.getIdentityid());
        }
        return accountRepository.save(account);
    }

    @DeleteMapping("/Account/{identityid}")
    @Transactional
    public ResponseEntity<Account> deleteAccount(@PathVariable("identityid") String identityId) {

        var account = accountRepository.findById(identityId)
                .orElseThrow(() -> new ResourceNotFoundException(identityId));
        accountRepository.deleteById(identityId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/Account/{identityid}")
    @Transactional
    public Account updateAccount(@RequestBody Account account, @PathVariable("identityid") String identityId) {
        Account existingAccount = this.accountRepository.findById(identityId)
                .orElseThrow(() -> new ResourceNotFoundException(identityId));
        existingAccount.setGender(account.getGender());
        existingAccount.setName(account.getName());
        return this.accountRepository.save(existingAccount);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AccountError AccountNotFound(ResourceNotFoundException e) {
        String id = e.getIdenityId();
        return new AccountError(0, "Account" + id + " not found");

    }

    @ExceptionHandler(ResourceExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public AccountError AccountExist(ResourceExistException e) {
        String id = e.getIdenityId();
        return new AccountError(0, "Account" + id + " exist");

    }

}
