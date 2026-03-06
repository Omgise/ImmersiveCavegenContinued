package net.tclproject.mysteriumlib.asm.common;

import net.minecraft.launchwrapper.*;
import java.io.*;
import org.objectweb.asm.*;
import net.tclproject.mysteriumlib.asm.core.*;
import java.util.*;

public class CustomClassTransformer extends TargetClassTransformer implements IClassTransformer
{
    static CustomClassTransformer instance;
    private Map<Integer, String> methodsMap;
    private static List<IClassTransformer> postTransformers;

    public CustomClassTransformer() {
        CustomClassTransformer.instance = this;
        if (CustomLoadingPlugin.isObfuscated()) {
            try {
                final long timeStart = System.currentTimeMillis();
                this.methodsMap = this.loadMethods();
                final long time = System.currentTimeMillis() - timeStart;
                this.logger.debug("Methods dictionary loaded in " + time + " ms");
            }
            catch (IOException e) {
                this.logger.severe("Can not load obfuscated method names", e);
            }
        }
        this.metaReader = CustomLoadingPlugin.getMetaReader();
        this.fixesMap.putAll(FirstClassTransformer.instance.getFixesMap());
        FirstClassTransformer.instance.getFixesMap().clear();
        FirstClassTransformer.instance.registeredBuiltinFixes = true;
    }

    private HashMap<Integer, String> loadMethods() throws IOException {
        final InputStream resourceStream = this.getClass().getResourceAsStream("/methods.bin");
        if (resourceStream == null) {
            throw new IOException("Methods dictionary not found.");
        }
        final DataInputStream input = new DataInputStream(new BufferedInputStream(resourceStream));
        final int numMethods = input.readInt();
        final HashMap<Integer, String> map = new HashMap<Integer, String>(numMethods);
        for (int i = 0; i < numMethods; ++i) {
            map.put(input.readInt(), input.readUTF());
        }
        input.close();
        return map;
    }

    public byte[] transform(final String name, final String deobfName, byte[] bytecode) {
        bytecode = this.transform(deobfName, bytecode);
        for (int i = 0; i < CustomClassTransformer.postTransformers.size(); ++i) {
            bytecode = CustomClassTransformer.postTransformers.get(i).transform(name, deobfName, bytecode);
        }
        return bytecode;
    }

    @Override
    public FixInserterClassVisitor createInserterClassVisitor(final ClassWriter classWriter, final List<ASMFix> fixes) {
        return new FixInserterClassVisitor(this, classWriter, fixes) {
            @Override
            protected boolean isTheTarget(final ASMFix fix, final String name, final String descriptor) {
                if (CustomLoadingPlugin.isObfuscated()) {
                    final String deobfName = CustomClassTransformer.this.methodsMap.get(CustomClassTransformer.getMethodIndex(name));
                    if (deobfName != null && super.isTheTarget(fix, deobfName, descriptor)) {
                        return true;
                    }
                }
                return super.isTheTarget(fix, name, descriptor);
            }
        };
    }

    public Map<Integer, String> getMethodNames() {
        return this.methodsMap;
    }

    public static int getMethodIndex(final String srgName) {
        if (srgName.startsWith("func_")) {
            final int first = srgName.indexOf(95);
            final int second = srgName.indexOf(95, first + 1);
            return Integer.valueOf(srgName.substring(first + 1, second));
        }
        return -1;
    }

    public static void registerPostTransformer(final IClassTransformer transformer) {
        CustomClassTransformer.postTransformers.add(transformer);
    }

    static {
        CustomClassTransformer.postTransformers = new ArrayList<IClassTransformer>();
    }
}
