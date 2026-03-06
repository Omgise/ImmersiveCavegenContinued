package net.tclproject.mysteriumlib.asm.core;

import org.objectweb.asm.*;

public abstract class FixInserterFactory
{
    protected boolean priorityReversed;

    public FixInserterFactory() {
        this.priorityReversed = false;
    }

    abstract FixInserter createFixInserter(final MethodVisitor p0, final int p1, final String p2, final String p3, final ASMFix p4, final FixInserterClassVisitor p5);

    public static class OnEnter extends FixInserterFactory
    {
        public static final OnEnter INSTANCE;

        public FixInserter createFixInserter(final MethodVisitor mv, final int access, final String name, final String desc, final ASMFix fix, final FixInserterClassVisitor cv) {
            return new FixInserter.OnEnterInserter(mv, access, name, desc, fix, cv);
        }

        static {
            INSTANCE = new OnEnter();
        }
    }

    public static class OnExit extends FixInserterFactory
    {
        public static final OnExit INSTANCE;
        public boolean insertOnThrows;

        public OnExit() {
            this.priorityReversed = true;
            this.insertOnThrows = false;
        }

        public OnExit(final boolean insertOnThrows) {
            this.insertOnThrows = insertOnThrows;
            this.priorityReversed = true;
        }

        public FixInserter createFixInserter(final MethodVisitor mv, final int access, final String name, final String desc, final ASMFix fix, final FixInserterClassVisitor cv) {
            return new FixInserter.OnExitInserter(mv, access, name, desc, fix, cv, this.insertOnThrows);
        }

        static {
            INSTANCE = new OnExit();
        }
    }

    public static class OnLineNumber extends FixInserterFactory
    {
        private int lineNumber;

        public OnLineNumber(final int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public FixInserter createFixInserter(final MethodVisitor mv, final int access, final String name, final String desc, final ASMFix fix, final FixInserterClassVisitor cv) {
            return new FixInserter.OnLineNumberInserter(mv, access, name, desc, fix, cv, this.lineNumber);
        }
    }
}
