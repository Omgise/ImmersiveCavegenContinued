package net.tclproject.mysteriumlib.asm.core;

import java.io.*;
import java.util.*;
import net.tclproject.mysteriumlib.asm.annotations.*;
import org.objectweb.asm.*;

public class FixParser
{
    private TargetClassTransformer transformer;
    private String fixesClassName;
    private String currentFixMethodName;
    private String currentFixMethodDescriptor;
    private boolean currentMethodIsPublicAndStatic;
    private HashMap<String, Object> annotationValues;
    private HashMap<Integer, Integer> argumentAnnotations;
    private boolean inFixAnnotation;
    private static final String fixDescriptor;
    private static final String localVariableDescriptor;
    private static final String returnedValueDescriptor;

    public FixParser(final TargetClassTransformer transformer) {
        this.argumentAnnotations = new HashMap<Integer, Integer>();
        this.transformer = transformer;
    }

    protected void parseForFixes(final String className) {
        this.transformer.logger.debug("Parsing class with fix methods " + className);
        try {
            this.transformer.metaReader.acceptVisitor(className, new FixClassVisitor());
        }
        catch (IOException e) {
            this.transformer.logger.severe("Can not parse class with fix methods " + className, e);
        }
    }

    protected void parseForFixes(final byte[] classBytes) {
        final FixClassVisitor fixMethodSearchClassVisitor = new FixClassVisitor();
        try {
            this.transformer.metaReader.acceptVisitor(classBytes, fixMethodSearchClassVisitor);
            this.transformer.logger.debug("Parsing class with fix methods " + fixMethodSearchClassVisitor.fixesClassName);
        }
        catch (Exception e) {
            this.transformer.logger.severe((fixMethodSearchClassVisitor.fixesClassName != "") ? ("Can not parse class with fix methods " + fixMethodSearchClassVisitor.fixesClassName) : "Can not create a class visitor to search a class for fix methods.", e);
        }
    }

    private void warnInvalidFix(final String message) {
        this.transformer.logger.warning("Found invalid fix " + this.fixesClassName + "#" + this.currentFixMethodName);
        this.transformer.logger.warning(message);
    }

    private void createAndRegisterFix(final String clsName) {
        final ASMFix.Builder builder = ASMFix.newBuilder();
        final Type methodType = Type.getMethodType(this.currentFixMethodDescriptor);
        final Type[] argumentTypes = methodType.getArgumentTypes();
        if (!this.currentMethodIsPublicAndStatic) {
            this.warnInvalidFix("Fix method must be public and static.");
            return;
        }
        if (argumentTypes.length < 1) {
            this.warnInvalidFix("Fix method has no arguments. First argument of a fix method must be a of the type of the target class.");
            return;
        }
        if (argumentTypes[0].getSort() != 10) {
            this.warnInvalidFix("First argument of the fix method is not an object. First argument of a fix method must be of the type of the target class.");
            return;
        }
        builder.setTargetClass(argumentTypes[0].getClassName());
        if (this.annotationValues.containsKey("targetMethod")) {
            builder.setTargetMethod(this.annotationValues.get("targetMethod"));
        }
        else {
            builder.setTargetMethod(this.currentFixMethodName);
        }
        builder.setFixesClass(clsName);
        builder.setFixMethod(this.currentFixMethodName);
        builder.addThisToFixMethodParameters();
        final boolean insertOnExit = Boolean.TRUE.equals(this.annotationValues.get("insertOnExit"));
        int currentParameterId = 1;
        for (int i = 1; i < argumentTypes.length; ++i) {
            final Type currentArgumentType = argumentTypes[i];
            if (this.argumentAnnotations.containsKey(i)) {
                final int stackIndexToBePassed = this.argumentAnnotations.get(i);
                if (stackIndexToBePassed == -1) {
                    builder.setTargetMethodReturnType(currentArgumentType);
                    builder.addReturnedValueToFixMethodParameters();
                }
                else {
                    builder.addFixMethodParameter(currentArgumentType, stackIndexToBePassed);
                }
            }
            else {
                builder.addTargetMethodParameters(currentArgumentType);
                builder.addFixMethodParameter(currentArgumentType, currentParameterId);
                currentParameterId += ((currentArgumentType == Type.LONG_TYPE || currentArgumentType == Type.DOUBLE_TYPE) ? 2 : 1);
            }
        }
        if (insertOnExit) {
            builder.setInjectorFactory(ASMFix.ON_EXIT_FACTORY);
        }
        if (this.annotationValues.containsKey("insertOnLine")) {
            final int lineToBeInsertedOn = this.annotationValues.get("insertOnLine");
            builder.setInjectorFactory(new FixInserterFactory.OnLineNumber(lineToBeInsertedOn));
        }
        if (this.annotationValues.containsKey("returnedType")) {
            builder.setTargetMethodReturnType(this.annotationValues.get("returnedType"));
        }
        EnumReturnSetting EnumReturnSetting = net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting.NEVER;
        if (this.annotationValues.containsKey("returnSetting")) {
            EnumReturnSetting = net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting.valueOf(this.annotationValues.get("returnSetting"));
            builder.setReturnSetting(EnumReturnSetting);
        }
        if (EnumReturnSetting != net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting.NEVER) {
            final Object primitiveConstant = this.getAlwaysReturnedValue();
            if (primitiveConstant != null) {
                builder.setReturnType(EnumReturnType.PRIMITIVE_CONSTANT);
                builder.setPrimitiveAlwaysReturned(primitiveConstant);
            }
            else if (Boolean.TRUE.equals(this.annotationValues.get("nullReturned"))) {
                builder.setReturnType(EnumReturnType.NULL);
            }
            else if (this.annotationValues.containsKey("anotherMethodReturned")) {
                builder.setReturnType(EnumReturnType.ANOTHER_METHOD_RETURN_VALUE);
                builder.setReturnMethod(this.annotationValues.get("anotherMethodReturned"));
            }
            else if (methodType.getReturnType() != Type.VOID_TYPE) {
                builder.setReturnType(EnumReturnType.FIX_METHOD_RETURN_VALUE);
            }
        }
        builder.setFixMethodReturnType(methodType.getReturnType());
        if (EnumReturnSetting == net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting.ON_TRUE && methodType.getReturnType() != Type.BOOLEAN_TYPE) {
            this.warnInvalidFix("Fix method must return boolean if returnSetting is ON_TRUE. (if we only return our custom value/ the original value if the fix method returns true, how do we know if it's true if it's not a boolean?)");
            return;
        }
        if ((EnumReturnSetting == net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting.ON_NULL || EnumReturnSetting == net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting.ON_NOT_NULL) && methodType.getReturnType().getSort() != 10 && methodType.getReturnType().getSort() != 9) {
            this.warnInvalidFix("Fix method must return object if returnSetting is ON_NULL or ON_NOT_NULL. (if we only return our custom value/ the original value if the fix method returns a null/ non null object, how do we know if it's a null/ not null object if it's not an object?)");
            return;
        }
        if (this.annotationValues.containsKey("order")) {
            builder.setPriority(FixOrder.valueOf(this.annotationValues.get("order")));
        }
        if (this.annotationValues.containsKey("createNewMethod")) {
            builder.setCreateMethod(Boolean.TRUE.equals(this.annotationValues.get("createNewMethod")));
        }
        if (this.annotationValues.containsKey("isFatal")) {
            builder.setFatal(Boolean.TRUE.equals(this.annotationValues.get("isFatal")));
        }
        this.transformer.registerFix(builder.build());
    }

    private Object getAlwaysReturnedValue() {
        for (final Map.Entry<String, Object> entry : this.annotationValues.entrySet()) {
            if (entry.getKey().endsWith("AlwaysReturned")) {
                return entry.getValue();
            }
        }
        return null;
    }

    static {
        fixDescriptor = Type.getDescriptor((Class)Fix.class);
        localVariableDescriptor = Type.getDescriptor((Class)LocalVariable.class);
        returnedValueDescriptor = Type.getDescriptor((Class)ReturnedValue.class);
    }

    private class FixClassVisitor extends ClassVisitor
    {
        String fixesClassName;

        public FixClassVisitor() {
            super(327680);
            this.fixesClassName = "";
        }

        public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
            this.fixesClassName = name.replace('/', '.');
        }

        public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
            FixParser.this.currentFixMethodName = name;
            FixParser.this.currentFixMethodDescriptor = desc;
            FixParser.this.currentMethodIsPublicAndStatic = ((access & 0x1) != 0x0 && (access & 0x8) != 0x0);
            return new FixMethodVisitor(this.fixesClassName);
        }
    }

    private class FixMethodVisitor extends MethodVisitor
    {
        String clsName;

        public FixMethodVisitor(final String className) {
            super(327680);
            this.clsName = className;
        }

        public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
            if (FixParser.fixDescriptor.equals(descriptor)) {
                FixParser.this.annotationValues = (HashMap<String, Object>)new HashMap();
                FixParser.this.inFixAnnotation = true;
            }
            return new FixAnnotationVisitor();
        }

        public AnnotationVisitor visitParameterAnnotation(final int indexOfArgument, final String descriptor, final boolean visible) {
            if (FixParser.returnedValueDescriptor.equals(descriptor)) {
                FixParser.this.argumentAnnotations.put(indexOfArgument, -1);
            }
            if (FixParser.localVariableDescriptor.equals(descriptor)) {
                return new AnnotationVisitor(327680) {
                    public void visit(final String name, final Object value) {
                        FixParser.this.argumentAnnotations.put(indexOfArgument, value);
                    }
                };
            }
            return null;
        }

        public void visitEnd() {
            if (FixParser.this.annotationValues != null) {
                FixParser.this.createAndRegisterFix(this.clsName);
            }
            FixParser.this.argumentAnnotations.clear();
            FixParser.this.currentFixMethodName = null;
            FixParser.this.currentFixMethodDescriptor = null;
            FixParser.this.currentMethodIsPublicAndStatic = false;
            FixParser.this.annotationValues = null;
        }
    }

    private class FixAnnotationVisitor extends AnnotationVisitor
    {
        public FixAnnotationVisitor() {
            super(327680);
        }

        public void visit(final String name, final Object value) {
            if (FixParser.this.inFixAnnotation) {
                FixParser.this.annotationValues.put(name, value);
            }
        }

        public void visitEnum(final String name, final String descriptor, final String value) {
            this.visit(name, value);
        }

        public void visitEnd() {
            FixParser.this.inFixAnnotation = false;
        }
    }
}
