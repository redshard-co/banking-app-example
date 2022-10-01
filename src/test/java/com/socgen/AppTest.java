package com.socgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.socgen.entities.Account;
import com.socgen.services.AccountService;

// test our banking app
public class AppTest {
    AccountService accountService;
    Account account;

    @Before
    public void setUp() {
        accountService = new AccountService();
        account = new Account("Imad");
    }

    @Test
    public void testDepositing() {
        // cant deposit to null or negative values
        assertFalse(accountService.deposit(null, null));
        assertFalse(accountService.deposit(account, BigDecimal.valueOf(-50)));

        // test normal deposit
        assertTrue(accountService.deposit(account, BigDecimal.valueOf(150.56)));
        assertTrue(accountService.deposit(account, BigDecimal.valueOf(100.11)));

        // check the balance and the operations count
        assertEquals(account.getBalance(), BigDecimal.valueOf(250.67));
        assertEquals(account.getOperations().size(), 2);
    }

    @Test
    public void testWithdrawing() {
        // init the account with some balance
        account.setBalance(BigDecimal.valueOf(1000));

        // can't withdraw null or negative values
        assertFalse(accountService.withdraw(null, null));
        assertFalse(accountService.withdraw(account, BigDecimal.valueOf(-500)));

        // normal withdrawal
        assertTrue(accountService.withdraw(account, BigDecimal.valueOf(500.50)));
        assertTrue(accountService.withdraw(account, BigDecimal.valueOf(400)));

        // can't withdraw in balance doesn't cover amount needed
        assertFalse(accountService.withdraw(account, BigDecimal.valueOf(400)));

        // should have only 99.50 left and with 2 valid operations
        assertEquals(account.getBalance(), BigDecimal.valueOf(99.50));
        assertEquals(account.getOperations().size(), 2);
    }

    @Test
    public void testValidatingAccount() {
        // deposit some balance
        assertTrue(accountService.deposit(account, BigDecimal.valueOf(1000)));

        // account is valid, nothing was tampered with
        assertTrue(accountService.validateOperations(account));

        // simulate a change to the operation (some had access to the db and added money
        // to himself)
        account.getOperations().get(0).setAmount(BigDecimal.valueOf(100000000));

        // data stored in account isn't consistent anymore
        assertFalse(accountService.validateOperations(account));
    }
}
