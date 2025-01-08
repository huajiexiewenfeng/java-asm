package com.java.asm;

import jdk.internal.org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Base64EncoderClassNameChanger extends ClassVisitor {

    private static final String OLD_CLASS_NAME = "com/java/asm/v1/BASE64Encoder";
    private static final String NEW_CLASS_NAME = "com/java/asm/v2/BASE64Encoder";
    private static final String TARGET_METHOD_NAME = "testMethod";

    public Base64EncoderClassNameChanger(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (TARGET_METHOD_NAME.equals(name)) {
            return new MethodAdapter(api, mv);
        }
        return mv;
    }

    private class MethodAdapter extends MethodVisitor {

        public MethodAdapter(int api, MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (OLD_CLASS_NAME.equals(owner)) {
                owner = NEW_CLASS_NAME;
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            if (opcode == Opcodes.NEW && OLD_CLASS_NAME.replace("/", ".").equals(type)) {
                type = NEW_CLASS_NAME.replace("/", ".");
            }
            super.visitTypeInsn(opcode, type);
        }

        // 如果需要处理字段访问，可以添加对visitFieldInsn的覆盖
        // @Override
        // public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        //     if (OLD_CLASS_NAME.equals(owner)) {
        //         owner = NEW_CLASS_NAME;
        //     }
        //     super.visitFieldInsn(opcode, owner, name, descriptor);
        // }

        // 注意：根据具体需求，可能还需要覆盖其他指令的处理方法。
    }

    public static void main(String[] args) throws IOException {
        // 读取原始类文件
        byte[] classBytes = Files.readAllBytes(Paths.get("D:\\workspace\\github\\Java-asm\\target\\classes\\com\\java\\asm\\TestOriginalClass.class"));

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