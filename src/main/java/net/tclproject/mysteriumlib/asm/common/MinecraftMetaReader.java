package net.tclproject.mysteriumlib.asm.common;

import net.tclproject.mysteriumlib.asm.core.*;
import java.lang.reflect.*;
import java.io.*;
import org.objectweb.asm.*;
import org.apache.logging.log4j.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.asm.transformers.deobf.*;
import net.minecraft.launchwrapper.*;

public class MinecraftMetaReader extends MetaReader
{
    private static Method runTransformers;

    @Override
    public byte[] classToBytes(final String name) throws IOException {
        final byte[] bytes = super.classToBytes(getRelevantName(name.replace('.', '/')));
        return deobfuscateClass(name, bytes);
    }

    @Override
    public boolean checkSameMethod(final String obfuscatedName, final String sourceDescriptor, final String mcpName, final String targetDescriptor) {
        return checkSameMethod(obfuscatedName, mcpName) && sourceDescriptor.equals(targetDescriptor);
    }

    @Override
    public MethodReference getMethodReferenceASM(final String ownerClass, final String methodName, final String descriptor) throws IOException {
        final FindMethodClassVisitor classVisitor = new FindMethodClassVisitor(methodName, descriptor);
        final byte[] bytes = getTransformedBytes(ownerClass);
        this.acceptVisitor(bytes, classVisitor);
        return classVisitor.found ? new MethodReference(ownerClass, classVisitor.targetName, classVisitor.targetDescriptor) : null;
    }

    public static byte[] deobfuscateClass(final String className, byte[] bytes) {
        if (CustomLoadingPlugin.getDeobfuscationTransformer() != null) {
            bytes = CustomLoadingPlugin.getDeobfuscationTransformer().transform(className, className, bytes);
        }
        return bytes;
    }

    public static byte[] getTransformedBytes(final String name) throws IOException {
        final String className = getRelevantName(name);
        byte[] bytes = Launch.classLoader.getClassBytes(className);
        if (bytes == null) {
            throw new RuntimeException("The byte representation of " + className + " cannot be found.");
        }
        try {
            bytes = (byte[])MinecraftMetaReader.runTransformers.invoke(Launch.classLoader, className, name, bytes);
        }
        catch (Exception e) {
            FMLLog.log("Mysterium Patches", Level.ERROR, "Error occured when making runTransformers in LaunchClassLoader usable.", new Object[0]);
            FMLLog.log("Mysterium Patches", Level.ERROR, "THIS IS MOST LIKELY HAPPENING BECAUSE OF MOD CONFLICTS. PLEASE CONTACT ME TO LET ME KNOW.", new Object[0]);
            FMLLog.log("Mysterium Patches", Level.ERROR, e.getMessage(), new Object[0]);
        }
        return bytes;
    }

    public static String getRelevantName(final String deobfName) {
        if (CustomLoadingPlugin.isObfuscated()) {
            return FMLDeobfuscatingRemapper.INSTANCE.unmap(deobfName);
        }
        return deobfName;
    }

    public static boolean checkSameMethod(final String srgName, final String mcpName) {
        if (CustomLoadingPlugin.isObfuscated() && CustomClassTransformer.instance != null) {
            final int methodId = CustomClassTransformer.getMethodIndex(srgName);
            final String remappedName = CustomClassTransformer.instance.getMethodNames().get(methodId);
            if (remappedName != null && remappedName.equals(mcpName)) {
                return true;
            }
        }
        return srgName.equals(mcpName);
    }

    static {
        try {
            (MinecraftMetaReader.runTransformers = LaunchClassLoader.class.getDeclaredMethod("runTransformers", String.class, String.class, byte[].class)).setAccessible(true);
        }
        catch (Exception e) {
            FMLLog.log("Mysterium Patches", Level.ERROR, "Error occured when making runTransformers in LaunchClassLoader usable.", new Object[0]);
            FMLLog.log("Mysterium Patches", Level.ERROR, "THIS IS MOST LIKELY HAPPENING BECAUSE OF MOD CONFLICTS. PLEASE CONTACT ME TO LET ME KNOW.", new Object[0]);
            FMLLog.log("Mysterium Patches", Level.ERROR, e.getMessage(), new Object[0]);
        }
    }
}
