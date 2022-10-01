package com.socgen.services;

import java.math.BigDecimal;

import com.socgen.entities.Account;
import com.socgen.entities.Operation;
import com.socgen.entities.OperationType;
import com.socgen.utils.Generator;

public class AccountService {
    public synchronized boolean deposit(Account account, BigDecimal amount) {
        // check if the params are not null
        if (account == null || amount == null)
            return false;
        // check if the amount is positive (greater than 0)
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return false;
        // update the operations
        try {
            Operation operation = Operation.getOperation(account, OperationType.Deposit, amount,
                    "Client has deposited in his account.");
            account.getOperations().add(operation);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        // update the balance
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        return true;
    }

    public synchronized boolean withdraw(Account account, BigDecimal amount) {
        // check if the params are not null
        if (account == null || amount == null)
            return false;
        // check if the amount is positive (greater than 0)
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return false;
        // check if the account has atleast the amount needed
        if (account.getBalance().compareTo(amount) < 0)
            return false;
        // update the operations
        try {
            Operation operation = Operation.getOperation(account, OperationType.Withdrawal, amount,
                    "Client has withdrawn from his account.");
            account.getOperations().add(operation);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        // update the balance
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        return true;
    }

    public synchronized String getStatement(Account account) {
        if (account == null)
            return null;

        StringBuilder builder = new StringBuilder();

        // add the customer name
        builder.append(String.format("Customer: %s \n", account.getName()));
        builder.append(String.format("Balance: %s EUR\n", account.getBalance().toString()));
        builder.append("Operations: \n");
        builder.append("--------------------------------------\n");

        // add the operations
        account.getOperations().stream().forEach(x -> builder.append(x.toString() + "\n"));

        builder.append("--------------------------------------\n");

        return builder.toString();
    }

    public synchronized boolean validateOperations(Account account) {
        if (account == null)
            return false;

        BigDecimal correctBalance = BigDecimal.ZERO;

        for (Operation operation : account.getOperations()) {
            try {
                if (operation == null || !Generator.verifyOperation(operation.toBytes(), operation.getSignature())) {
                    System.err.println("Operation " + operation + " is not valid (potentially tampered with).");
                    return false;
                }
                if (operation.getType() == OperationType.Deposit) {
                    correctBalance = correctBalance.add(operation.getAmount());
                } else if (operation.getType() == OperationType.Withdrawal) {
                    correctBalance = correctBalance.subtract(operation.getAmount());
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return false;
            }
        }
        return account.getBalance().compareTo(correctBalance) == 0;
    }
}
