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

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.RETURN;

import net.minecraftforge.fml.common.FMLLog;
import org.magmafoundation.magma.modPatcher.ModPatcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * FTBQuestPatch
 *
 * @author Hexeption admin@hexeption.co.uk - Jasper
 * @since 02/01/2021 - 04:29 pm
 */
@ModPatcher.ModPatcherInfo(name = "FTB-Quest")
public class FTBQuestPatch extends ModPatcher {

	@Override
	public byte[] transform(String name, String transformedName, byte[] clazz) {
		if (clazz == null) return null;

		if ("com.feed_the_beast.ftbquests.quest.reward.CommandReward".equals(transformedName)) {
			return patchClass(clazz);
		}

		return clazz;
	}

	private byte[] patchClass(byte[] clazz) {
	FMLLog.bigWarning("Starting Patching FTBQuest");
		ClassReader classReader = new ClassReader(clazz);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);


		MethodNode claimMethod = null;
		for (MethodNode node : classNode.methods) {
			if (node.name.equals("claim")) {
				claimMethod = node;
				break;
			}
		}


		if (claimMethod == null) {
			FMLLog.warning("Failed to find method CommandReward#claim");
			return clazz;
		}


		classNode.methods.remove(claimMethod);

		// pls dont look ugly
		{
			MethodVisitor mv = classNode.visitMethod(claimMethod.access, claimMethod.name, claimMethod.desc, claimMethod.signature, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(89, l0);
			mv.visitTypeInsn(NEW, "java/util/HashMap");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
			mv.visitVarInsn(ASTORE, 3);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(90, l1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("p");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayerMP", "func_70005_c_", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(92, l2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayerMP", "func_180425_c", "()Lnet/minecraft/util/math/BlockPos;", false);
			mv.visitVarInsn(ASTORE, 4);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(93, l3);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("x");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "func_177958_n", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(94, l4);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("y");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "func_177956_o", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(95, l5);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("z");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "func_177952_p", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(97, l6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/feed_the_beast/ftbquests/quest/reward/CommandReward", "getQuestChapter", "()Lcom/feed_the_beast/ftbquests/quest/Chapter;", false);
			mv.visitVarInsn(ASTORE, 5);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(99, l7);
			mv.visitVarInsn(ALOAD, 5);
			Label l8 = new Label();
			mv.visitJumpInsn(IFNULL, l8);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(101, l9);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("chapter");
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			mv.visitLabel(l8);
			mv.visitLineNumber(104, l8);
			mv.visitFrame(Opcodes.F_APPEND, 3, new Object[] {"java/util/Map", "net/minecraft/util/math/BlockPos", "com/feed_the_beast/ftbquests/quest/Chapter"}, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("quest");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/feed_the_beast/ftbquests/quest/reward/CommandReward", "quest", "Lcom/feed_the_beast/ftbquests/quest/Quest;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(105, l10);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("team");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayerMP", "func_110124_au", "()Ljava/util/UUID;", false);
			mv.visitMethodInsn(INVOKESTATIC, "com/feed_the_beast/ftblib/lib/data/FTBLibAPI", "getTeam", "(Ljava/util/UUID;)Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(107, l11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/feed_the_beast/ftbquests/quest/reward/CommandReward", "command", "Ljava/lang/String;");
			mv.visitLdcInsn("/");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
			Label l12 = new Label();
			mv.visitJumpInsn(IFEQ, l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/feed_the_beast/ftbquests/quest/reward/CommandReward", "command", "Ljava/lang/String;");
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "substring", "(I)Ljava/lang/String;", false);
			Label l13 = new Label();
			mv.visitJumpInsn(GOTO, l13);
			mv.visitLabel(l12);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/feed_the_beast/ftbquests/quest/reward/CommandReward", "command", "Ljava/lang/String;");
			mv.visitLabel(l13);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/String"});
			mv.visitVarInsn(ASTORE, 6);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLineNumber(109, l14);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "entrySet", "()Ljava/util/Set;", true);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;", true);
			mv.visitVarInsn(ASTORE, 7);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitFrame(Opcodes.F_APPEND, 2, new Object[] {"java/lang/String", "java/util/Iterator"}, 0, null);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			Label l16 = new Label();
			mv.visitJumpInsn(IFEQ, l16);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/util/Map$Entry");
			mv.visitVarInsn(ASTORE, 8);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(111, l17);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;", true);
			Label l18 = new Label();
			mv.visitJumpInsn(IFNULL, l18);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitLineNumber(113, l19);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
			mv.visitLdcInsn("@");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getKey", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/lang/String");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;", true);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "replace", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", false);
			mv.visitVarInsn(ASTORE, 6);
			mv.visitLabel(l18);
			mv.visitLineNumber(115, l18);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitJumpInsn(GOTO, l15);
			mv.visitLabel(l16);
			mv.visitLineNumber(117, l16);
			mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/player/EntityPlayerMP", "field_71133_b", "Lnet/minecraft/server/MinecraftServer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/MinecraftServer", "func_71187_D", "()Lnet/minecraft/command/ICommandManager;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/feed_the_beast/ftbquests/quest/reward/CommandReward", "playerCommand", "Z");
			Label l20 = new Label();
			mv.visitJumpInsn(IFEQ, l20);
			mv.visitVarInsn(ALOAD, 1);
			Label l21 = new Label();
			mv.visitJumpInsn(GOTO, l21);
			mv.visitLabel(l20);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"net/minecraft/command/ICommandManager"});
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/player/EntityPlayerMP", "field_71133_b", "Lnet/minecraft/server/MinecraftServer;");
			mv.visitLabel(l21);
			mv.visitFrame(Opcodes.F_FULL, 7, new Object[] {"com/feed_the_beast/ftbquests/quest/reward/CommandReward", "net/minecraft/entity/player/EntityPlayerMP", Opcodes.INTEGER, "java/util/Map", "net/minecraft/util/math/BlockPos", "com/feed_the_beast/ftbquests/quest/Chapter", "java/lang/String"}, 2, new Object[] {"net/minecraft/command/ICommandManager", "net/minecraft/command/ICommandSender"});
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/command/ICommandManager", "func_71556_a", "(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)I", true);
			mv.visitInsn(POP);
			Label l22 = new Label();
			mv.visitLabel(l22);
			mv.visitLineNumber(118, l22);
			mv.visitInsn(RETURN);
			Label l23 = new Label();
			mv.visitLabel(l23);
			mv.visitLocalVariable("entry", "Ljava/util/Map$Entry;", "Ljava/util/Map$Entry<Ljava/lang/String;Ljava/lang/Object;>;", l17, l18, 8);
			mv.visitLocalVariable("this", "Lcom/feed_the_beast/ftbquests/quest/reward/CommandReward;", null, l0, l23, 0);
			mv.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayerMP;", null, l0, l23, 1);
			mv.visitLocalVariable("notify", "Z", null, l0, l23, 2);
			mv.visitLocalVariable("overrides", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;", l1, l23, 3);
			mv.visitLocalVariable("pos", "Lnet/minecraft/util/math/BlockPos;", null, l3, l23, 4);
			mv.visitLocalVariable("chapter", "Lcom/feed_the_beast/ftbquests/quest/Chapter;", null, l7, l23, 5);
			mv.visitLocalVariable("s", "Ljava/lang/String;", null, l14, l23, 6);
			mv.visitMaxs(3, 9);
			mv.visitEnd();
		}

		ClassWriter classWriter = new ClassWriter(0);
		classNode.accept(classWriter);
		return classWriter.toByteArray();
	}
}
