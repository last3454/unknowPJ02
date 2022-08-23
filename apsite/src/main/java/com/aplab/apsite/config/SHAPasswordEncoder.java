package com.aplab.apsite.config;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.springframework.security.crypto.password.PasswordEncoder;

public class SHAPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {

        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(rawPassword.toString().getBytes("utf8"));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // String password = this.encode(rawPassword);
        String password = rawPassword.toString(); // 로그인시 비밀번호가 sha512 로 변환되어 넘어와 encode 하지 않고 비교한다.
        return password.equals(encodedPassword);
    }
}
