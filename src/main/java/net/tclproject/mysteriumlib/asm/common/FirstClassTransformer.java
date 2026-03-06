package net.tclproject.mysteriumlib.asm.common;

import net.minecraft.launchwrapper.*;
import net.tclproject.mysteriumlib.asm.core.*;
import java.util.*;
import org.objectweb.asm.*;
import cpw.mods.fml.common.asm.transformers.deobf.*;

public class FirstClassTransformer extends TargetClassTransformer implements IClassTransformer
{
    public static FirstClassTransformer instance;
    boolean registeredBuiltinFixes;

    public FirstClassTransformer() {
        this.metaReader = CustomLoadingPlugin.getMetaReader();
        if (FirstClassTransformer.instance != null) {
            this.fixesMap.putAll(FirstClassTransformer.instance.getFixesMap());
            FirstClassTransformer.instance.getFixesMap().clear();
        }
        else {
            this.registerClassWithFixes(BuiltinFixes.class.getName());
        }
        FirstClassTransformer.instance = this;
    }

    public byte[] transform(final String name, final String deobfName, final byte[] bytes) {
        return this.transform(deobfName, bytes);
    }

    @Override
    public FixInserterClassVisitor createInserterClassVisitor(final ClassWriter classWriter, final List<ASMFix> fixes) {
        return new FixInserterClassVisitor(this, classWriter, fixes) {
            @Override
            protected boolean isTheTarget(final ASMFix fix, final String name, final String descriptor) {
                return super.isTheTarget(fix, name, FirstClassTransformer.obfuscateDescriptor(descriptor));
            }
        };
    }

    public HashMap<String, List<ASMFix>> getFixesMap() {
        return this.fixesMap;
    }

    static String obfuscateDescriptor(final String descriptor) {
        if (!CustomLoadingPlugin.isObfuscated()) {
            return descriptor;
        }
        final Type methodType = Type.getMethodType(descriptor);
        final Type mappedReturnType = map(methodType.getReturnType());
        final Type[] argTypes = methodType.getArgumentTypes();
        final Type[] mappedArgTypes = new Type[argTypes.length];
        for (int i = 0; i < mappedArgTypes.length; ++i) {
            mappedArgTypes[i] = map(argTypes[i]);
        }
        return Type.getMethodDescriptor(mappedReturnType, mappedArgTypes);
    }

    static Type map(final Type type) {
        if (!CustomLoadingPlugin.isObfuscated()) {
            return type;
        }
        if (type.getSort() < 9) {
            return type;
        }
        if (type.getSort() == 9) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < type.getDimensions(); ++i) {
                sb.append("[");
            }
            final boolean isPrimitiveArray = type.getSort() < 9;
            if (!isPrimitiveArray) {
                sb.append("L");
            }
            sb.append(map(type.getElementType()).getInternalName());
            if (!isPrimitiveArray) {
                sb.append(";");
            }
            return Type.getType(sb.toString());
        }
        if (type.getSort() == 10) {
            final String unmappedName = FMLDeobfuscatingRemapper.INSTANCE.map(type.getInternalName());
            return Type.getType("L" + unmappedName + ";");
        }
        throw new IllegalArgumentException("Can not map method type!");
    }

    static {
        FirstClassTransformer.instance = new FirstClassTransformer();
    }
}
