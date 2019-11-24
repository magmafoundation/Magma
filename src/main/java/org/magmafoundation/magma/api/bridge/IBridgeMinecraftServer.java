package org.magmafoundation.magma.api.bridge;

import java.util.List;
import joptsimple.OptionSet;
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

}
