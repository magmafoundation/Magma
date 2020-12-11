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

package org.magmafoundation.magma.remapper.remappers;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.magmafoundation.magma.remapper.proxy.DelegateClassLoader;
import org.magmafoundation.magma.remapper.proxy.DelegateURLClassLoder;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * MagmaSuperClassRemapper
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 20/11/2020 - 04:16 am
 */
public class MagmaSuperClassRemapper {

    public static Map<String, Class<?>> superClassMap = Maps.newHashMap();
    public static Map<String, Class<?>> defineClassMap = Maps.newHashMap();

    public static void initialize(ClassNode classNode) {

        superClassMap.put("java/net/URLClassLoader", DelegateURLClassLoder.class);
        superClassMap.put("java/lang/ClassLoader", DelegateClassLoader.class);

        AtomicBoolean definedClass = new AtomicBoolean(false);
        Class<?> superClass = superClassMap.get(classNode.superName);
        if (superClass != null) {
            if (superClass == DelegateClassLoader.class) {
                defineClassMap.put(classNode.name + ";defineClass", DelegateClassLoader.class);
            }
            classNode.superName = superClass.getName().replace('.', '/');
            definedClass.set(true);
        }
        for (MethodNode methodNode : classNode.methods) {
            methodNode.instructions.iterator().forEachRemaining(nextNode -> {
                if (nextNode instanceof TypeInsnNode && nextNode.getOpcode() == Opcodes.NEW) {
                    TypeInsnNode typeInsnNode = (TypeInsnNode) nextNode;
                    Class<?> remappedClass = superClassMap.get(typeInsnNode.desc);
                    if (remappedClass != null) {
                        typeInsnNode.desc = Type.getInternalName(remappedClass);
                        definedClass.set(true);
                    }
                }

                if (nextNode instanceof MethodInsnNode) {
                    MethodInsnNode insnNode = (MethodInsnNode) nextNode;
                    switch (insnNode.getOpcode()) {
                        case Opcodes.INVOKEVIRTUAL:
                            Class<?> virtualMethodClass = ReflectionMethodRemapper.getVirtualMethods().get(insnNode.owner + ";" + insnNode.name);
                            if (virtualMethodClass != null) {
                                Type returnType = Type.getReturnType(insnNode.desc);
                                ArrayList<Type> args = new ArrayList<>();
                                args.add(Type.getObjectType(insnNode.owner));
                                args.addAll(Arrays.asList(Type.getArgumentTypes(insnNode.desc)));
                                insnNode.setOpcode(Opcodes.INVOKESTATIC);
                                insnNode.owner = Type.getInternalName(virtualMethodClass);
                                insnNode.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
                            } else {
                                virtualMethodClass = defineClassMap.get(insnNode.owner + ";" + insnNode.name);
                                if (virtualMethodClass != null) {
                                    insnNode.name += "Magma";
                                    insnNode.owner = Type.getInternalName(virtualMethodClass);
                                }
                            }
                            break;
                        case Opcodes.INVOKESPECIAL:
                            if (definedClass.get()) {
                                Class<?> superClassClass = superClassMap.get(insnNode.owner);
                                if (superClassClass != null && insnNode.name.equals("<init>")) {
                                    insnNode.owner = Type.getInternalName(superClassClass);
                                }
                            }
                            if ((insnNode.owner + ";" + insnNode.name).equals("javax/script/ScriptEngineManager;<init>") && insnNode.desc.equals("()V")) {
                                insnNode.desc = "(Ljava/lang/ClassLoader;)V";
                                methodNode.instructions.insertBefore(insnNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;", false));
                                methodNode.maxStack++;
                            }
                    }
                }

            });

        }


    }


}
