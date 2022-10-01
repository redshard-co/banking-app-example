package com.socgen.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.util.Base64;
import java.util.UUID;

public class Generator {
    private static KeyPair keyPair = Generator.getKeyPair();

    public static String getUuid() {
        return UUID.randomUUID().toString();
    }

    public static String signOperation(byte[] operationBytes) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(keyPair.getPrivate());
        privateSignature.update(operationBytes);

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verifyOperation(byte[] operationBytes, String signature) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(keyPair.getPublic());
        publicSignature.update(operationBytes);

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

    private static KeyPair getKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(4096);
            return kpg.genKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
