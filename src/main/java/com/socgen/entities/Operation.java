package com.socgen.entities;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.socgen.utils.Generator;

public class Operation {
    private String id;

    // Identify the account the operation belongs to
    private Account account;

    // Operation type (deposit or withdrawal)
    private OperationType type;

    // Operation time
    private LocalDateTime timestamp;

    // Operation amount
    private BigDecimal amount;

    // Small text to describe operation
    private String label;

    // Internally generated key on operation creation (based on the operation
    // fields)
    // can be used to make sure no one tampered with the operation
    // ie (add more to the amount or change withdrawal to deposit to get more cash)
    // there are many ways to ensure immutability, for our simple example we can use
    // an rsa signature
    private String signature;

    private Operation() {
    }

    public static Operation getOperation(Account account, OperationType type, BigDecimal amount, String label)
            throws Exception {
        if (account == null || type == null || amount == null || label == null)
            throw new Exception("Invalid parameters.");

        Operation operation = new Operation();
        operation.setId(Generator.getUuid());
        operation.setAccount(account);

        operation.setAmount(amount);
        operation.setType(type);

        operation.setTimestamp(LocalDateTime.now());
        operation.setLabel(label);

        operation.setSignature(Generator.signOperation(operation.toBytes()));
        return operation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Operation [id=" + id + ", type=" + type + ", timestamp=" + timestamp
                + ", amount=" + amount + ", label=" + label + "]";
    }

    public byte[] toBytes() {
        try {
            return String.format("%s - %s - %s - %s - %s",
                    this.getId(),
                    this.getAccount().getId(),
                    this.getType().toString(),
                    this.getTimestamp().toString(),
                    this.getAmount().toString()).getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            return new byte[] {};
        }
    }
}
