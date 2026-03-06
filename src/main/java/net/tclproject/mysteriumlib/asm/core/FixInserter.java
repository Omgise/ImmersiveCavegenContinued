package net.tclproject.mysteriumlib.asm.core;

import org.objectweb.asm.commons.*;
import org.objectweb.asm.*;

public abstract class FixInserter extends AdviceAdapter
{
    protected final ASMFix fix;
    protected final FixInserterClassVisitor classVisitor;
    public final String methodName;
    public final Type methodType;
    public final boolean isStatic;

    protected FixInserter(final MethodVisitor mv, final int access, final String name, final String descriptor, final ASMFix fix, final FixInserterClassVisitor classVisitor) {
        super(327680, mv, access, name, descriptor);
        this.fix = fix;
        this.classVisitor = classVisitor;
        this.isStatic = ((access & 0x8) != 0x0);
        this.methodName = name;
        this.methodType = Type.getMethodType(descriptor);
    }

    protected final void insertFix() {
        if (!this.classVisitor.visitingFix) {
            this.classVisitor.visitingFix = true;
            this.fix.insertFix(this);
            this.classVisitor.visitingFix = false;
        }
    }

    public static class OnEnterInserter extends FixInserter
    {
        public OnEnterInserter(final MethodVisitor mv, final int access, final String name, final String desc, final ASMFix fix, final FixInserterClassVisitor cv) {
            super(mv, access, name, desc, fix, cv);
        }

        protected void onMethodEnter() {
            this.insertFix();
        }
    }

    public static class OnExitInserter extends FixInserter
    {
        public boolean insertOnThrows;

        public OnExitInserter(final MethodVisitor mv, final int access, final String name, final String desc, final ASMFix fix, final FixInserterClassVisitor cv) {
            super(mv, access, name, desc, fix, cv);
            this.insertOnThrows = false;
        }

        public OnExitInserter(final MethodVisitor mv, final int access, final String name, final String desc, final ASMFix fix, final FixInserterClassVisitor cv, final boolean insertOnThrows) {
            super(mv, access, name, desc, fix, cv);
            this.insertOnThrows = insertOnThrows;
        }

        protected void onMethodExit(final int opcode) {
            if (opcode != 191 || this.insertOnThrows) {
                this.insertFix();
            }
        }
    }

    public static class OnLineNumberInserter extends FixInserter
    {
        private int lineNumber;

        public OnLineNumberInserter(final MethodVisitor mv, final int access, final String name, final String desc, final ASMFix fix, final FixInserterClassVisitor cv, final int lineNumber) {
            super(mv, access, name, desc, fix, cv);
            this.lineNumber = lineNumber;
        }

        public void visitLineNumber(final int lineVisiting, final Label start) {
            super.visitLineNumber(lineVisiting, start);
            if (lineVisiting == this.lineNumber) {
                this.insertFix();
            }
        }
    }
}
