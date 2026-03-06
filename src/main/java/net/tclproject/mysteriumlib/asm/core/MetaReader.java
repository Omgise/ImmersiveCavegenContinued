package net.tclproject.mysteriumlib.asm.core;

import java.lang.reflect.*;
import java.io.*;
import org.apache.commons.io.*;
import org.objectweb.asm.*;
import java.util.*;
import org.apache.logging.log4j.*;
import cpw.mods.fml.common.*;

public class MetaReader
{
    private static Method findLoadedClass;

    public List<String> getLocalVariables(final byte[] classBytes, final String methodName, final Type... argumentTypes) {
        final List<String> localVariables = new ArrayList<String>();
        final String methodDescriptor = Type.getMethodDescriptor(Type.VOID_TYPE, argumentTypes);
        final String methodDescriptorWithoutReturnType = methodDescriptor.substring(0, methodDescriptor.length() - 1);
        final ClassVisitor classVisitor = new ClassVisitor(327680) {
            public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
                if (methodName.equals(name) && descriptor.startsWith(methodDescriptorWithoutReturnType)) {
                    return new MethodVisitor(327680) {
                        public void visitLocalVariable(final String name, final String descriptor, final String signature, final Label start, final Label end, final int index) {
                            final String typeName = Type.getType(descriptor).getClassName();
                            final int fixedIndex = index + (((access & 0x8) != 0x0) ? 1 : 0);
                            localVariables.add(fixedIndex + ": " + typeName + " " + name);
                        }
                    };
                }
                return null;
            }
        };
        this.acceptVisitor(classBytes, classVisitor);
        return localVariables;
    }

    public List<String> getLocalVariables(final String className, final String methodName, final Type... argTypes) throws IOException {
        return this.getLocalVariables(this.classToBytes(className), methodName, argTypes);
    }

    public void printLocalVariables(final byte[] classBytes, final String methodName, final Type... argumentTypes) {
        final List<String> locals = this.getLocalVariables(classBytes, methodName, argumentTypes);
        for (final String str : locals) {
            System.out.println(str);
        }
    }

    public void printLocalVariables(final String className, final String methodName, final Type... argumentTypes) throws IOException {
        this.printLocalVariables(this.classToBytes(className), methodName, argumentTypes);
    }

    public static InputStream classToStream(final String name) {
        final String classResourceName = '/' + name.replace('.', '/') + ".class";
        return MetaReader.class.getResourceAsStream(classResourceName);
    }

    public byte[] classToBytes(final String name) throws IOException {
        final String classLocationName = '/' + name.replace('.', '/') + ".class";
        return IOUtils.toByteArray(MetaReader.class.getResourceAsStream(classLocationName));
    }

    public void acceptVisitor(final byte[] classBytes, final ClassVisitor visitor) {
        new ClassReader(classBytes).accept(visitor, 0);
    }

    public void acceptVisitor(final String name, final ClassVisitor visitor) throws IOException {
        this.acceptVisitor(this.classToBytes(name), visitor);
    }

    public static void acceptVisitor(final InputStream classStream, final ClassVisitor visitor) {
        try {
            final ClassReader reader = new ClassReader(classStream);
            reader.accept(visitor, 0);
            classStream.close();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public MethodReference findMethod(final String owner, final String methodName, final String descriptor) {
        final ArrayList<String> superClasses = this.getSuperClasses(owner);
        for (int i = superClasses.size() - 1; i > 0; --i) {
            final String className = superClasses.get(i);
            final MethodReference methodReference = this.getMethodReference(className, methodName, descriptor);
            if (methodReference != null) {
                return methodReference;
            }
        }
        return null;
    }

    public MethodReference getMethodReference(final String className, final String methodName, final String descriptor) {
        try {
            return this.getMethodReferenceASM(className, methodName, descriptor);
        }
        catch (Exception e) {
            return this.getMethodReferenceReflect(className, methodName, descriptor);
        }
    }

    public MethodReference getMethodReferenceASM(final String className, final String methodName, final String descriptor) throws IOException {
        final FindMethodClassVisitor cv = new FindMethodClassVisitor(methodName, descriptor);
        this.acceptVisitor(className, cv);
        if (cv.found) {
            return new MethodReference(className, cv.targetName, cv.targetDescriptor);
        }
        return null;
    }

    public MethodReference getMethodReferenceReflect(final String className, final String methodName, final String descriptor) {
        final Class loadedClass = this.getLoadedClass(className);
        if (loadedClass != null) {
            for (final Method m : loadedClass.getDeclaredMethods()) {
                if (this.checkSameMethod(methodName, descriptor, m.getName(), Type.getMethodDescriptor(m))) {
                    return new MethodReference(className, m.getName(), Type.getMethodDescriptor(m));
                }
            }
        }
        return null;
    }

    public boolean checkSameMethod(final String sourceName, final String sourceDesc, final String targetName, final String targetDesc) {
        return sourceName.equals(targetName) && sourceDesc.equals(targetDesc);
    }

    public ArrayList<String> getSuperClasses(String name) {
        final ArrayList<String> superClasses = new ArrayList<String>(1);
        superClasses.add(name);
        while ((name = this.getSuperClass(name)) != null) {
            superClasses.add(name);
        }
        Collections.reverse(superClasses);
        return superClasses;
    }

    public Class getLoadedClass(final String name) {
        if (MetaReader.findLoadedClass != null) {
            try {
                final ClassLoader classLoader = MetaReader.class.getClassLoader();
                return (Class)MetaReader.findLoadedClass.invoke(classLoader, name.replace('/', '.'));
            }
            catch (Exception e) {
                FMLLog.log("Mysterium Patches", Level.ERROR, "Error occured when getting a class from a name.", new Object[0]);
                FMLLog.log("Mysterium Patches", Level.ERROR, "THIS IS MOST LIKELY HAPPENING BECAUSE OF MOD CONFLICTS. PLEASE CONTACT ME TO LET ME KNOW.", new Object[0]);
                FMLLog.log("Mysterium Patches", Level.ERROR, e.getMessage(), new Object[0]);
            }
        }
        return null;
    }

    public String getSuperClass(final String name) {
        try {
            return this.getSuperClassASM(name);
        }
        catch (Exception e) {
            return this.getSuperClassReflect(name);
        }
    }

    public String getSuperClassASM(final String name) throws IOException {
        final CheckSuperClassVisitor cv = new CheckSuperClassVisitor();
        this.acceptVisitor(name, cv);
        return cv.superClassName;
    }

    public String getSuperClassReflect(final String name) {
        final Class loadedClass = this.getLoadedClass(name);
        if (loadedClass == null) {
            return "java/lang/Object";
        }
        if (loadedClass.getSuperclass() == null) {
            return null;
        }
        return loadedClass.getSuperclass().getName().replace('.', '/');
    }

    static {
        try {
            (MetaReader.findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class)).setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            FMLLog.log("Mysterium Patches", Level.ERROR, "Error occured when making findLoadedClass in ClassLoader usable.", new Object[0]);
            FMLLog.log("Mysterium Patches", Level.ERROR, "THIS IS MOST LIKELY HAPPENING BECAUSE OF MOD CONFLICTS. PLEASE CONTACT ME TO LET ME KNOW.", new Object[0]);
            FMLLog.log("Mysterium Patches", Level.ERROR, e.getMessage(), new Object[0]);
        }
    }

    protected class CheckSuperClassVisitor extends ClassVisitor
    {
        String superClassName;

        public CheckSuperClassVisitor() {
            super(327680);
        }

        public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
            this.superClassName = superName;
        }
    }

    protected class FindMethodClassVisitor extends ClassVisitor
    {
        public String targetName;
        public String targetDescriptor;
        public boolean found;

        public FindMethodClassVisitor(final String name, final String desctiptor) {
            super(327680);
            this.targetName = name;
            this.targetDescriptor = desctiptor;
        }

        public MethodVisitor visitMethod(final int access, final String name, final String desctiptor, final String signature, final String[] exceptions) {
            if ((access & 0x2) == 0x0 && MetaReader.this.checkSameMethod(name, desctiptor, this.targetName, this.targetDescriptor)) {
                this.found = true;
                this.targetName = name;
                this.targetDescriptor = desctiptor;
            }
            return null;
        }
    }

    public static class MethodReference
    {
        public final String owner;
        public final String name;
        public final String descriptor;

        public MethodReference(final String owner, final String name, final String descriptor) {
            this.owner = owner;
            this.name = name;
            this.descriptor = descriptor;
        }

        public Type getReturnType() {
            return Type.getMethodType(this.descriptor);
        }

        @Override
        public String toString() {
            return "MethodReference{owner='" + this.owner + '\'' + ", name='" + this.name + '\'' + ", desc='" + this.descriptor + '\'' + '}';
        }
    }
}
