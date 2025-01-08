package com.java.asm;

import com.java.asm.model.ClassNameChangeEntity;
import org.objectweb.asm.*;


public class ClassNameChanger extends ClassVisitor {

    private final String OLD_CLASS_NAME;
    private final String NEW_CLASS_NAME;
    private final ClassNameChangeEntity classNameChangeEntity;

    public ClassNameChanger(int api, ClassVisitor cv, ClassNameChangeEntity classNameChangeEntity) {
        super(api, cv);
        this.classNameChangeEntity = classNameChangeEntity;
        OLD_CLASS_NAME = classNameChangeEntity.getOldClassName();
        NEW_CLASS_NAME = classNameChangeEntity.getNewClassName();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (classNameChangeEntity.getMethodNames().contains(name)) {
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
    }
}