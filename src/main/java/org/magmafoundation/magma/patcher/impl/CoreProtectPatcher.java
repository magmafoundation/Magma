package org.magmafoundation.magma.patcher.impl;

import java.util.ListIterator;
import org.magmafoundation.magma.patcher.Patcher;
import org.magmafoundation.magma.patcher.Patcher.PatcherInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * CoreProtectPatcher
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 20/03/2020 - 06:08 pm
 */
@PatcherInfo(name = "CoreProtect", description = "Changes Material to Magma's getBlockMaterial that has modded block materials ")
public class CoreProtectPatcher extends Patcher {

    @Override
    public byte[] transform(String className, byte[] clazz) {
        if (className.equals("net.coreprotect.CoreProtectAPI") || className.equals("net.coreprotect.Functions")) {
            return remapMaterial(clazz);
        }
        return clazz;
    }

    private byte[] remapMaterial(byte[] clazz) {
        ClassReader classReader = new ClassReader(clazz);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        for (MethodNode methodNode : classNode.methods) {
            ListIterator<AbstractInsnNode> insnNodeListIterator = methodNode.instructions.iterator();
            while (insnNodeListIterator.hasNext()) {
                AbstractInsnNode nextNode = insnNodeListIterator.next();
                if (nextNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) nextNode;
                    if (methodInsnNode.owner.equals("org/bukkit/Material") && methodInsnNode.name.equals("getMaterial")) {
                        methodInsnNode.name = "getBlockMaterial";
                    }
                }
            }
        }

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
