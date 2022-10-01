package com.socgen.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.socgen.utils.Generator;

public class Account {
    // Id can be a long and auto generated using sequence/serial in db
    // we'll use uuids here for simplicity
    private String id;

    // Name attribute for display
    private String name;

    // BigDecimal used for better accuracy in flaoting points
    private BigDecimal balance;

    // Account operations are stored here
    // for simplity we'll just use a list
    private List<Operation> operations;

    // Create a new account
    public Account(String name) {
        this.id = Generator.getUuid();
        this.name = name;
        this.operations = new ArrayList<Operation>();
        this.balance = BigDecimal.ZERO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
