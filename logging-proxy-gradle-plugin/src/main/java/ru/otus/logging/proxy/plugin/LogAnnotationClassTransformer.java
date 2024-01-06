package ru.otus.logging.proxy.plugin;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

public class LogAnnotationClassTransformer extends ClassVisitor {
    private static final String LOG_ANNOTATION_DESC = "Lhomework/annotation/Log;";

    private boolean classHasBeenTransformed = false;

    public LogAnnotationClassTransformer(ClassVisitor classVisitor) {
        super(ASM9, classVisitor);
    }


    public boolean classHasBeenTransformed() {
        return classHasBeenTransformed;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodTransformer(methodVisitor, access, name, descriptor, signature, exceptions);
    }

    private class MethodTransformer extends MethodVisitor {
        private final String name;
        private final Type[] argumentTypes;

        private boolean hasLoggingAnnotation = false;

        public MethodTransformer(MethodVisitor methodVisitor, int access, String name,
                                 String descriptor, String signature, String[] exceptions) {
            super(ASM9, methodVisitor);
            this.argumentTypes = Type.getMethodType(descriptor).getArgumentTypes();
            this.name = name;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (descriptor.equals(LOG_ANNOTATION_DESC)) {
                hasLoggingAnnotation = true;
                classHasBeenTransformed = true;
            }
            return super.visitAnnotation(descriptor, visible);
        }

        @Override
        public void visitCode() {
            if (hasLoggingAnnotation) {
                addLogging();
            }
            super.visitCode();
        }

        private void addLogging() {
            int messageVarIndex = argumentTypes.length + 1;
            super.visitLdcInsn("");
            super.visitVarInsn(ASTORE, messageVarIndex);
            concatConst(messageVarIndex, "executed method: " + name);
            if (argumentTypes.length > 0) {
                concatConst(messageVarIndex, ", ");
            }
            for (int i = 0; i < argumentTypes.length; ++i) {
                concatConst(messageVarIndex, "param" + (i + 1) + ": ");
                concatVar(messageVarIndex, i + 1, argumentTypes[i]);
                if (i != argumentTypes.length - 1) {
                    concatConst(messageVarIndex, " ");
                }
            }
            printString(messageVarIndex);
        }

        private void concatConst(int varIndex, String stringConst) {
            super.visitVarInsn(ALOAD, varIndex);
            super.visitInvokeDynamicInsn(
                    "makeConcatWithConstants",
                    "(Ljava/lang/String;)Ljava/lang/String;",
                    new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false),
                    "\u0001" + stringConst);
            super.visitVarInsn(ASTORE, varIndex);
        }

        private void concatVar(int varIndex1, int varIndex2, Type type) {
            super.visitVarInsn(ALOAD, varIndex1);
            final int loadOpcode;
            if (type.getSort() == Type.OBJECT) {
                loadOpcode = ALOAD;
            } else {
                loadOpcode = type.getOpcode(ILOAD);
            }
            super.visitVarInsn(loadOpcode, varIndex2);
            super.visitInvokeDynamicInsn(
                    "makeConcatWithConstants",
                    "(Ljava/lang/String;" + type.getDescriptor() + ")Ljava/lang/String;",
                    new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false),
                    "\u0001\u0001");
            super.visitVarInsn(ASTORE, varIndex1);
        }

        private void printString(int varIndex) {
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitVarInsn(ALOAD, varIndex);
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

    }
}