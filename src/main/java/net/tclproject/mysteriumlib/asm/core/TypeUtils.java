package net.tclproject.mysteriumlib.asm.core;

import org.objectweb.asm.*;
import java.util.*;

public class TypeUtils
{
    private static final Map<String, Type> primitives;

    public static Type getType(final String name) {
        return getArrayType(name, 0);
    }

    public static Type getArrayType(final String name) {
        return getArrayType(name, 1);
    }

    public static Type getArrayType(final String name, final int arrayDimensions) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrayDimensions; ++i) {
            stringBuilder.append("[");
        }
        final Type primitive = TypeUtils.primitives.get(name);
        if (primitive == null) {
            stringBuilder.append("L");
            stringBuilder.append(name.replace(".", "/"));
            stringBuilder.append(";");
        }
        else {
            stringBuilder.append(primitive.getDescriptor());
        }
        return Type.getType(stringBuilder.toString());
    }

    public static Object getStackMapFormat(final Type type) {
        if (type == Type.BOOLEAN_TYPE || type == Type.BYTE_TYPE || type == Type.SHORT_TYPE || type == Type.CHAR_TYPE || type == Type.INT_TYPE) {
            return Opcodes.INTEGER;
        }
        if (type == Type.FLOAT_TYPE) {
            return Opcodes.FLOAT;
        }
        if (type == Type.DOUBLE_TYPE) {
            return Opcodes.DOUBLE;
        }
        if (type == Type.LONG_TYPE) {
            return Opcodes.LONG;
        }
        return type.getInternalName();
    }

    static {
        (primitives = new HashMap<String, Type>(9)).put("void", Type.VOID_TYPE);
        TypeUtils.primitives.put("boolean", Type.BOOLEAN_TYPE);
        TypeUtils.primitives.put("byte", Type.BYTE_TYPE);
        TypeUtils.primitives.put("short", Type.SHORT_TYPE);
        TypeUtils.primitives.put("char", Type.CHAR_TYPE);
        TypeUtils.primitives.put("int", Type.INT_TYPE);
        TypeUtils.primitives.put("float", Type.FLOAT_TYPE);
        TypeUtils.primitives.put("long", Type.LONG_TYPE);
        TypeUtils.primitives.put("double", Type.DOUBLE_TYPE);
    }
}
