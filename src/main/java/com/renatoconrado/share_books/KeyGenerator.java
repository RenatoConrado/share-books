package com.renatoconrado.share_books;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class KeyGenerator {

    public static void main(String[] args) {

        SecretKey key = Jwts.SIG.HS256.key().build();

        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("Generated Key (Base64): " + base64Key);
    }
}
