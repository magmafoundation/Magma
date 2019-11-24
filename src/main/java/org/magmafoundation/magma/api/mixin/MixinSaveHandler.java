package org.magmafoundation.magma.api.mixin;

import com.mojang.datafixers.DataFixer;
import java.io.File;
import java.io.FileInputStream;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.datafix.DefaultTypeReferences;
import net.minecraft.world.storage.SaveHandler;
import org.apache.logging.log4j.Logger;
import org.magmafoundation.magma.api.bridge.IBridgeSaveHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinSaveHandler
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 07:22 pm
 */
@Mixin(SaveHandler.class)
public class MixinSaveHandler implements IBridgeSaveHandler {

    @Shadow
    @Final
    private static Logger field_215773_b;

    @Shadow
    @Final
    protected DataFixer field_215772_a;

    @Shadow
    @Final
    private File playersDirectory;

    @Override
    public CompoundNBT getPlayerNBT(String name) {
        try {
            File file1 = new File(this.playersDirectory, name + ".dat");
            if (file1.exists() && file1.isFile()) {
                CompoundNBT nbt = CompressedStreamTools.readCompressed(new FileInputStream(file1));
                if (nbt != null) {
                    nbt = NBTUtil.update(this.field_215772_a, DefaultTypeReferences.PLAYER, nbt,
                        nbt.contains("DataVersion", 3) ? nbt.getInt("DataVersion") : -1);
                }
                return nbt;
            }
        } catch (Exception exception) {
            field_215773_b.warn("Failed to load player data for " + name);
        }
        return null;
    }
}
