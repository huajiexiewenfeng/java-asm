package com.java.asm;

public class TestOriginalClass {

    public static void testMethod() {
        System.out.println(com.java.asm.v2.BASE64Encoder.encode("123"));
    }

    public static void testMethodOld() {
        System.out.println(com.java.asm.v1.BASE64Encoder.encode("123"));
    }

}
