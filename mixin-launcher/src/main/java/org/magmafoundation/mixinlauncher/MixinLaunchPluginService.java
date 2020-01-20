package org.magmafoundation.mixinlauncher;

import cpw.mods.modlauncher.TransformingClassLoader;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.stream.Stream;

/**
 * MixinLaunchPluginService
 *
 * @author Redned
 * @since 19/01/2020 - 12:30 am
 */
public class MixinLaunchPluginService implements ILaunchPluginService {

    @Override
    public String name() {
        return MixinTransformationService.NAME;
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        throw new UnsupportedOperationException("Unsupported ModLauncher version");
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty, String reason) {
        return EnumSet.noneOf(Phase.class);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
        throw new UnsupportedOperationException("Unsupported ModLauncher version");
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType, String reason) {
        return false;
    }

    @Override
    public void initializeLaunch(ITransformerLoader transformerLoader, Path[] specialPaths) {
        TransformingClassLoader classLoader = (TransformingClassLoader) Thread.currentThread().getContextClassLoader();
        classLoader.addTargetPackageFilter(name -> Stream.of(
                "org.objectweb.asm.",
                "org.spongepowered.asm.launch.",
                "org.spongepowered.asm.lib.",
                "org.spongepowered.asm.mixin.",
                "org.spongepowered.asm.service.",
                "org.spongepowered.asm.util.")
                .noneMatch(name::startsWith)
        );
    }

    @Override
    public <T> T getExtension() {
        return null;
    }
}
