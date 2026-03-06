package net.tclproject.mysteriumlib.asm.core;

import org.objectweb.asm.*;
import java.util.*;

public class FixInserterClassVisitor extends ClassVisitor
{
    List<ASMFix> fixes;
    List<ASMFix> insertedFixes;
    boolean visitingFix;
    TargetClassTransformer transformer;
    String superName;

    public FixInserterClassVisitor(final TargetClassTransformer transformer, final ClassWriter cv, final List<ASMFix> fixs) {
        super(327680, (ClassVisitor)cv);
        this.insertedFixes = new ArrayList<ASMFix>(1);
        this.fixes = fixs;
        this.transformer = transformer;
    }

    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, this.superName = superName, interfaces);
    }

    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        for (final ASMFix fix : this.fixes) {
            if (this.isTheTarget(fix, name, desc) && !this.insertedFixes.contains(fix)) {
                mv = (MethodVisitor)fix.getInjectorFactory().createFixInserter(mv, access, name, desc, fix, this);
                this.insertedFixes.add(fix);
            }
        }
        return mv;
    }

    public void visitEnd() {
        for (final ASMFix fix : this.fixes) {
            if (fix.getCreateMethod() && !this.insertedFixes.contains(fix)) {
                fix.createMethod(this);
            }
        }
        super.visitEnd();
    }

    protected boolean isTheTarget(final ASMFix fix, final String name, final String desc) {
        return fix.isTheTarget(name, desc);
    }
}
