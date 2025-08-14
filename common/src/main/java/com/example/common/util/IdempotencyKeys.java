package com.example.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

public final class IdempotencyKeys {
    private static final HexFormat HEX = HexFormat.of();
    private IdempotencyKeys() {}

    public static String forPayment(UUID orderId, String reference, Long amountCents) {
        String input = orderId + ":" + reference + ":" + amountCents;
        return sha256(input);
    }

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HEX.formatHex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}