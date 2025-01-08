package com.java.asm;

import com.java.asm.v1.BASE64Encoder;

public class TestOriginalClass {

    public static void testMethod() {
        System.out.println(BASE64Encoder.encode("123"));
    }

    public static void testMethodOld() {
        System.out.println(BASE64Encoder.encode("123"));
    }

}
