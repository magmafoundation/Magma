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

package org.magmafoundation.magma.modPatcher.patches;

import java.util.ListIterator;

import org.magmafoundation.magma.modPatcher.ModPatcher;
import org.magmafoundation.magma.modPatcher.utils.ModPatchUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * FirstAidPatch
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 01/01/2021 - 03:38 pm
 */
@ModPatcher.ModPatcherInfo(name = "FirstAid")
public class FirstAidPatch extends ModPatcher {

	@Override
	public byte[] transform(String name, String transformedName, byte[] clazz) {
		if (clazz == null) return null;

		if ("ichttt.mods.firstaid.common.DataManagerWrapper".equals(transformedName)) {
			return patchClass(clazz);
		}

		return clazz;
	}

	private byte[] patchClass(byte[] clazz) {
		ClassReader classReader = new ClassReader(clazz);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);

		retry:
		for (MethodNode methodNode : classNode.methods) {
			if ("set_impl".equals(methodNode.name) && "(Lnet/minecraft/network/datasync/DataParameter;Ljava/lang/Object;)V".equals(methodNode.desc)) {
				ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
				while (iterator.hasNext()) {
					AbstractInsnNode insnNode = iterator.next();
					if (insnNode instanceof MethodInsnNode) {
						if ("ichttt/mods/firstaid/common/DataManagerWrapperHook".equals(((MethodInsnNode) insnNode).owner)) {
							break retry;
						}
					}
				}

				InsnList insnList = new InsnList();
				insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
				insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "ichttt/mods/firstaid/common/DataManagerWrapper", "player", "Lnet/minecraft/entity/player/EntityPlayer;"));
				insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
				insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
				insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ModPatchUtil.class), "patchedHealthUpdate", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/network/datasync/DataParameter;Ljava/lang/Object;)V", false));
				methodNode.instructions.insert(insnList);
			}
		}


		ClassWriter classWriter = new ClassWriter(0);
		classNode.accept(classWriter);
		return classWriter.toByteArray();
	}

}
