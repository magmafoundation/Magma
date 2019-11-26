package org.magmafoundation.magma.api.bridge.server;

import java.io.File;
import java.util.List;
import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import org.magmafoundation.magma.api.core.MagmaServer;

/**
 * IBridgeMinecraftServer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 05:58 am
 */
public interface IBridgeMinecraftServer {

    MagmaServer getMagmaServer();

    void setMagmaServer(MagmaServer magmaServer);

    OptionSet getOptions();

    List<ServerWorld> getServerWorldList();

    MinecraftServer getInstance();

    File getBukkitDataPackFolder();

}
