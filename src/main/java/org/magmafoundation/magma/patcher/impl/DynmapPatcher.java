package org.magmafoundation.magma.patcher.impl;

import java.util.ListIterator;
import org.magmafoundation.magma.Magma;
import org.magmafoundation.magma.patcher.Patcher;
import org.magmafoundation.magma.patcher.Patcher.PatcherInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * DynmapPatcher
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 05/03/2020 - 11:59 pm
 */
@PatcherInfo(name = "dynmap", description = "Remaps BukkitVersionHelperCB and BukkitVersionHelperGeneric to support Magma version")
public class DynmapPatcher extends Patcher {

    @Override
    public byte[] transform(String className, byte[] clazz) {
        if (className.equals("org.dynmap.bukkit.helper.BukkitVersionHelperCB")) {
            return arrayRemap(patchBukkitVersionHelperCB(clazz));
        }
        if (className.equals("org.dynmap.bukkit.helper.BukkitVersionHelperGeneric")) {
            return arrayRemap(clazz);
        }
        return clazz;
    }

    private byte[] arrayRemap(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();
                if (next instanceof LdcInsnNode) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) next;
                    if (ldcInsnNode.cst instanceof String) {
                        String str = (String) ldcInsnNode.cst;
                        if ("[Lnet.minecraft.server.Block;".equals(str)) {
                            ldcInsnNode.cst = "[Lnet.minecraft.block.Block;";
                        } else if ("[Lnet.minecraft.server.BiomeBase;".equals(str)) {
                            ldcInsnNode.cst = "[Lnet.minecraft.world.biome.Biome;";
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchBukkitVersionHelperCB(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("getNMSPackage")) {
                InsnList insnList = new InsnList();
                insnList.add(new LdcInsnNode("net.minecraft.server." + Magma.getBukkitVersion()));
                insnList.add(new InsnNode(Opcodes.ARETURN));
                method.instructions = insnList;
                method.tryCatchBlocks.clear();
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
