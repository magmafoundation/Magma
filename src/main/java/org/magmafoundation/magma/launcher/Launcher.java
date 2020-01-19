package org.magmafoundation.magma.launcher;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

/**
 * Launcher
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 02:38 am
 */
public class Launcher implements IMixinConnector {

    @Override
    public void connect() {
        MixinBootstrap.init();

        Mixins.addConfiguration("mixins.magma.bridge.json");
        Mixins.addConfiguration("mixins.magma.core.json");
    }
}
