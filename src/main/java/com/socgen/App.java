package com.socgen;

import java.math.BigDecimal;

import com.socgen.entities.Account;
import com.socgen.services.AccountService;

public class App {
    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        Account account = new Account("Imad");
        accountService.deposit(account, BigDecimal.valueOf(100.53));
        accountService.deposit(account, BigDecimal.valueOf(250.78));
        accountService.withdraw(account, BigDecimal.valueOf(40.22));
        System.out.println(accountService.getStatement(account));
        // should be true
        System.out.println(accountService.validateOperations(account));
        // let's mess with some operation
        account.getOperations().get(0).setAmount(BigDecimal.valueOf(1000));
        // should be false
        System.out.println(accountService.validateOperations(account));
    }
}
