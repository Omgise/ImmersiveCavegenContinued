package net.tclproject.mysteriumlib.asm.core;

import org.objectweb.asm.*;
import java.util.*;

public class TargetClassTransformer
{
    MiscUtils utils;
    public MiscUtils.LogHelper logger;
    protected HashMap<String, List<ASMFix>> fixesMap;
    private FixParser containerParser;
    protected MetaReader metaReader;

    public TargetClassTransformer() {
        this.utils = new MiscUtils();
        this.logger = this.utils.new SystemLogHelper();
        this.fixesMap = new HashMap<String, List<ASMFix>>();
        this.containerParser = new FixParser(this);
        this.metaReader = new MetaReader();
    }

    public void registerFix(final ASMFix fix) {
        if (this.fixesMap.containsKey(fix.getTargetClassName())) {
            this.fixesMap.get(fix.getTargetClassName()).add(fix);
        }
        else {
            final List<ASMFix> list = new ArrayList<ASMFix>(2);
            list.add(fix);
            this.fixesMap.put(fix.getTargetClassName(), list);
        }
    }

    public void registerClassWithFixes(final String className) {
        this.containerParser.parseForFixes(className);
    }

    public void registerClassWithFixes(final byte[] classBytes) {
        this.containerParser.parseForFixes(classBytes);
    }

    public byte[] transform(final String className, byte[] classBytes) {
        final List<ASMFix> fixes = this.fixesMap.get(className);
        if (fixes != null) {
            Collections.sort(fixes);
            this.logger.debug("Injecting fixes into class " + className + ".");
            try {
                final int javaVersion = (classBytes[6] & 0xFF) << 8 | (classBytes[7] & 0xFF);
                final boolean java7 = javaVersion > 50;
                final ClassReader classReader = new ClassReader(classBytes);
                final ClassWriter classWriter = this.createClassWriter(java7 ? 2 : 1);
                final FixInserterClassVisitor fixInserterVisitor = this.createInserterClassVisitor(classWriter, fixes);
                classReader.accept((ClassVisitor)fixInserterVisitor, java7 ? 4 : 8);
                classBytes = classWriter.toByteArray();
                for (final ASMFix fix : fixInserterVisitor.insertedFixes) {
                    this.logger.debug("Fixed method " + fix.getFullTargetMethodName());
                }
                fixes.removeAll(fixInserterVisitor.insertedFixes);
            }
            catch (Exception e) {
                this.logger.severe("A problem has occurred during transformation of class " + className + ".");
                this.logger.severe("Fixes to be applied to this class:");
                for (final ASMFix fix2 : fixes) {
                    this.logger.severe(fix2.toString());
                }
                this.logger.severe("Stack trace:", e);
            }
            for (final ASMFix notInserted : fixes) {
                if (notInserted.isMandatory()) {
                    throw new RuntimeException("Can not find the target method of fatal fix: " + notInserted);
                }
                this.logger.warning("Can not find the target method of fix: " + notInserted);
            }
        }
        return classBytes;
    }

    public FixInserterClassVisitor createInserterClassVisitor(final ClassWriter classWriter, final List<ASMFix> fixes) {
        return new FixInserterClassVisitor(this, classWriter, fixes);
    }

    public ClassWriter createClassWriter(final int flags) {
        return this.utils.new SafeCommonSuperClassWriter(this.metaReader, flags);
    }
}
