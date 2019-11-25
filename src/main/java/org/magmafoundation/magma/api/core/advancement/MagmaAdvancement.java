package org.magmafoundation.magma.api.core.advancement;


import java.util.Collection;
import java.util.Collections;
import net.minecraft.advancements.Advancement;
import org.bukkit.NamespacedKey;
import org.magmafoundation.magma.api.core.util.MagmaNamespacedLocation;

public class MagmaAdvancement implements org.bukkit.advancement.Advancement {

    private Advancement handle;

    public MagmaAdvancement(Advancement handle) {
        this.handle = handle;
    }

    @Override
    public Collection<String> getCriteria() {
        return Collections.unmodifiableCollection(handle.getCriteria().keySet());
    }

    @Override
    public NamespacedKey getKey() {
        return MagmaNamespacedLocation.fromMinecraft(handle.getId());
    }

    public Advancement getHandle() {
        return handle;
    }
}
