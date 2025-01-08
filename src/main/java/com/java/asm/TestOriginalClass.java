package com.java.asm;

import com.java.asm.v1.BASE64Encoder;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestOriginalClass {
    public static void main(String[] args) throws Exception {
        testASM();
//        testMethod();
        System.out.println("");

    }

    public void testMethod() {
        System.out.println(com.java.asm.v2.BASE64Encoder.encode("123"));
    }

    public static void testMethodOld() {
        System.out.println(com.java.asm.v1.BASE64Encoder.encode("123"));
    }


    public static void testASM() throws Exception {

        ClassLoader classLoader = TestOriginalClass.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(TestOriginalClass.class.getName().replace('.', '/') + ".class");
        Path path = Paths.get(resourceUrl.toURI());

        // 读取原始类文件
        byte[] classBytes = Files.readAllBytes(path);

        // 创建ClassWriter来写入修改后的类
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // 使用自定义的ClassVisitor来修改类
        Base64EncoderClassNameChanger classVisitor = new Base64EncoderClassNameChanger(Opcodes.ASM5, classWriter);

        // 使用ClassReader读取原始类，并通过自定义的ClassVisitor进行修改
        ClassReader classReader = new ClassReader(classBytes);
        classReader.accept(classVisitor, 0);

        // 获取修改后的类字节码
        byte[] modifiedClassBytes = classWriter.toByteArray();

        // 将修改后的类写入文件
        try (FileOutputStream fos = new FileOutputStream("D:\\workspace\\github\\Java-asm\\target\\classes\\com\\java\\asm\\TestOriginalClass.class")) {
            fos.write(modifiedClassBytes);
        }
    }

}
