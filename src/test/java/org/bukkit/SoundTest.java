package org.bukkit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.bukkit.craftbukkit.v1_12_R1.CraftSound;
import org.junit.Test;

public class SoundTest {

    @Test
    public void testGetSound() {
        for (Sound sound : Sound.values()) {
            assertThat(sound.name(), CraftSound.getSound(sound), is(not(nullValue())));
        }
    }

    @Test
    public void testReverse() {
        for (ResourceLocation effect : SoundEvent.REGISTRY.getKeys()) {
            assertNotNull(effect + "", Sound.valueOf(effect.getResourcePath().replace('.', '_').toUpperCase(java.util.Locale.ENGLISH)));
        }
    }

    @Test
    public void testCategory() {
        for (SoundCategory category : SoundCategory.values()) {
            assertNotNull(category + "", SoundCategory.valueOf(category.name()));
        }
    }

    @Test
    public void testCategoryReverse() {
        for (SoundCategory category : SoundCategory.values()) {
            assertNotNull(category + "", SoundCategory.valueOf(category.name()));
        }
    }
}
