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

package org.magmafoundation.magma.patcher.impl;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.Material.MaterialType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.magmafoundation.magma.patcher.Patcher;
import org.magmafoundation.magma.patcher.Patcher.PatcherInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * EssentialsPatcher
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 29/07/2020 - 03:58 am
 */
@PatcherInfo(name = "Essentials", description = "Fixes Commands in Essentials")
public class EssentialsPatcher extends Patcher {

    @Override
    public byte[] transform(String className, byte[] clazz) {
        if (className.equals("com.earth2me.essentials.commands.Commandhat")) {
            return patchCommand(clazz);
        }
        return clazz;
    }

    private byte[] patchCommand(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("registerPermissionsIfNecessary") && method.desc.equals("(Lorg/bukkit/plugin/PluginManager;)V")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EssentialsPatcher.class), "registerPermissionsIfNecessary", "(Lorg/bukkit/plugin/PluginManager;)V", false));
                insnList.add(new InsnNode(Opcodes.RETURN));
                method.instructions = insnList;
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static void registerPermissionsIfNecessary(final PluginManager toRegister) {
        final Permission hatPerm = toRegister.getPermission("essentials.hat.prevent-type.*");
        if (hatPerm != null) {
            return;
        }
        final ImmutableMap.Builder<String, Boolean> children = ImmutableMap.builder();
        for (Material mat : Material.values()) {
            if (mat.getMaterialType() == MaterialType.VANILLA || mat.getMaterialType() == MaterialType.MOD_ITEM) {
                final String matPerm = "essentials.hat.prevent-type." + mat.name().toLowerCase();
                if (toRegister.getPermission(matPerm) == null) {
                    children.put(matPerm, true);
                    toRegister.addPermission(new Permission(matPerm, "Prevent using " + mat + " as a type of hat.", PermissionDefault.FALSE));
                }
            }
        }
        toRegister.addPermission(new Permission("essentials.hat.prevent-type.*", "Prevent all types of hats", PermissionDefault.FALSE, children.build()));
    }

}
