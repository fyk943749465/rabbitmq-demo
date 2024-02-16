package com.example.emailcode.mailutil;

import java.util.UUID;

public class CodeGeneratorUtil {

    public static String generateCode(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }


}
