package com.java.asm;

import com.java.asm.model.ClassNameChangeEntity;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ASMUtils {
    public static void asmClassPathUpdate(ClassNameChangeEntity classNameChangeEntity) throws Exception {
        ClassLoader classLoader = classNameChangeEntity.getClass().getClassLoader();
        URL resourceUrl = classLoader.getResource(TestOriginalClass.class.getName().replace('.', '/') + ".class");
        Path path = Paths.get(resourceUrl.toURI());

        // 读取原始类文件
        byte[] classBytes = Files.readAllBytes(path);

        // 创建ClassWriter来写入修改后的类
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // 使用自定义的ClassVisitor来修改类
        ClassNameChanger classVisitor = new ClassNameChanger(Opcodes.ASM5, classWriter, classNameChangeEntity);

        // 使用ClassReader读取原始类，并通过自定义的ClassVisitor进行修改
        ClassReader classReader = new ClassReader(classBytes);
        classReader.accept(classVisitor, 0);

        // 获取修改后的类字节码
        byte[] modifiedClassBytes = classWriter.toByteArray();

        // 将修改后的类写入文件
        try (FileOutputStream fos = new FileOutputStream(resourceUrl.getPath())) {
            fos.write(modifiedClassBytes);
        }
    }

}
