/*
 * Magma Server
 * Copyright (C) 2019-2020.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.magmafoundation.magma.remapper.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

/**
 * ASMUtils
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 07:55 am
 */
public class ASMUtils {

    private static final Map<Integer, String> opcodeMap = new HashMap<>();
    private static final Map<Integer, String> typeMap = new HashMap<>();
    private static final Map<Integer, BiConsumer<String, AbstractInsnNode>> printerMap = new HashMap<>();
    private static final Pattern illegalSignaturePattern = Pattern.compile("^[0-9a-zA-Z_/();<>\\[]+$");

    static {
        for (Field field : Opcodes.class.getDeclaredFields()) {
            if (field.getType() == int.class && Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    opcodeMap.put((Integer) field.get(null), field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Field field : AbstractInsnNode.class.getDeclaredFields()) {
            if (field.getType() == int.class && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    typeMap.put((Integer) field.get(null), field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        printerMap.put(AbstractInsnNode.FIELD_INSN, (prefix, item) -> {
            FieldInsnNode node = (FieldInsnNode) item;
            System.out.println(prefix + " " + node.owner + " " + node.name + " " + node.desc);
        });
        printerMap.put(AbstractInsnNode.VAR_INSN, (prefix, item) -> {
            VarInsnNode node = (VarInsnNode) item;
            System.out.println(prefix + " " + node.var);
        });
        printerMap.put(AbstractInsnNode.TYPE_INSN, (prefix, item) -> {
            TypeInsnNode node = (TypeInsnNode) item;
            System.out.println(prefix + " " + node.desc);
        });

        printerMap.put(AbstractInsnNode.METHOD_INSN, (prefix, item) -> {
            MethodInsnNode node = (MethodInsnNode) item;
            System.out.println(prefix + " " + node.owner + " " + node.name + " " + node.desc);
        });
        printerMap.put(AbstractInsnNode.INVOKE_DYNAMIC_INSN, (prefix, item) -> {
            InvokeDynamicInsnNode node = (InvokeDynamicInsnNode) item;
            System.out.println(prefix + " " + node.name + " " + node.desc);
            if (node.bsm != null) {
                print(prefix, node.bsm);
            }
            if (node.bsmArgs != null) {
                for (Object bsmArg : node.bsmArgs) {
                    if (bsmArg instanceof Type) {
                        print(prefix, (Type) bsmArg);
                    } else if (bsmArg instanceof Handle) {
                        print(prefix, (Handle) bsmArg);
                    } else if (bsmArg instanceof String) {
                        System.out.println(prefix + " String " + bsmArg);
                    } else {
                        System.out.println(prefix + " " + bsmArg.getClass().getSimpleName() + " " + bsmArg);
                    }
                }
            }
        });
    }

    public static String toDescriptorV1(String className) {
        if (className.startsWith("[")) {
            return className.replace('.', '/');
        }
        switch (className) {
            case "byte":
                return "B";
            case "short":
                return "S";
            case "int":
                return "I";
            case "long":
                return "J";
            case "float":
                return "F";
            case "double":
                return "D";
            case "boolean":
                return "Z";
            case "char":
                return "C";
            case "void":
                return "V";
            default:
                return "L" + className.replace('.', '/') + ";";
        }
    }

    public static String toDescriptorV2(String internalName) {
        if (internalName.startsWith("[")) {
            return internalName;
        }
        if (internalName.length() == 1) {
            return internalName;
        }
        return "L" + internalName.replace('.', '/') + ";";
    }

    public static String toClassName(String internalName) {
        return internalName.replace('/', '.');
    }

    public static Type toType(Class clazz) {
        return Type.getType(clazz);
    }

    public static String toDescriptor(Class clazz) {
        return Type.getDescriptor(clazz);
    }

    public static String toInternalName(Class clazz) {
        return Type.getInternalName(clazz);
    }

    public static String toInternalName(String className) {
        return className.replace('.', '/');
    }

    public static String toArgumentDescriptor(Class... classes) {
        StringJoiner sj = new StringJoiner("", "(", ")");
        for (Class aClass : classes) {
            sj.add(Type.getDescriptor(aClass));
        }
        return sj.toString();
    }

    public static String toArgumentDescriptor(String methodDescriptor) {
        return methodDescriptor.substring(0, methodDescriptor.lastIndexOf(')'));
    }

    public static String toMethodDescriptor(Class returnType, Class... classes) {
        StringJoiner sj = new StringJoiner("", "(", ")");
        for (Class aClass : classes) {
            sj.add(toDescriptor(aClass));
        }
        return sj.toString() + toDescriptor(returnType);
    }

    public static void dump(Path dir, byte[] bs) throws IOException {
        ClassReader classReader = new ClassReader(bs);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        File file = dir.resolve(classNode.name + ".class").toFile();
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        Files.write(file.toPath(), bs);
    }

    public static String getOpcodeName(int opcode) {
        return opcodeMap.get(opcode);
    }

    public static String getTypeName(int type) {
        return opcodeMap.get(type);
    }

    public static void printClass(ClassNode classNode) {
        System.out.println("============ " + classNode.name + " ============");
        if (classNode.fields != null) {
            for (FieldNode field : classNode.fields) {
                System.out.println("  field " + field.desc + " " + field.name + " " + field.signature);
            }
        }
        if (classNode.methods != null) {
            for (MethodNode method : classNode.methods) {
                System.out.println("  method " + method.name + " " + method.desc);
                if (method.instructions == null) {
                    continue;
                }
                ListIterator<AbstractInsnNode> it = method.instructions.iterator();
                while (it.hasNext()) {
                    print("    insn", it.next());
                }
                if (method.localVariables == null) {
                    continue;
                }
                for (LocalVariableNode localVariable : method.localVariables) {
                    System.out.println("    localVariable " + localVariable.getClass().getSimpleName() + " " + localVariable.name + " " + localVariable.desc + " " + localVariable.signature);
                }
            }
        }
    }

    public static void printClass(byte[] bs) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bs);
        classReader.accept(classNode, 0);
        printClass(classNode);
    }

    public static boolean isValidSingnature(String signature) {
        return signature != null && !signature.isEmpty() && illegalSignaturePattern.matcher(signature).matches();
    }

    public static String getInternalName(Type type) {
        if (type.getSort() <= 8) {
            return type.getDescriptor();
        }
        return type.getInternalName();
    }

    private static void print(String prefix, Handle o) {
        System.out.println(prefix + " Handle " + o.getOwner() + " " + o.getName() + " " + o.getDesc());
    }

    private static void print(String prefix, Type o) {
        System.out.println(prefix + " Type " + o.getDescriptor());
    }

    private static void print(String prefix, AbstractInsnNode insn) {
        prefix = prefix + " " + insn.getClass().getSimpleName() + " " + getTypeName(insn.getType()) + " " + getOpcodeName(insn.getOpcode());
        BiConsumer<String, AbstractInsnNode> printer = printerMap.get(insn.getType());
        if (printer != null) {
            printer.accept(prefix, insn);
        } else {
            System.out.println(prefix);
        }
    }
}
