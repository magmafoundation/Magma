package org.bukkit;

import net.minecraft.util.text.TextFormatting;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.junit.Assert;
import org.junit.Test;

public class ChatTest {

    @Test
    public void testColors() {
        for (ChatColor color : ChatColor.values()) {
            Assert.assertNotNull(CraftChatMessage.getColor(color));
            Assert.assertEquals(color, CraftChatMessage.getColor(CraftChatMessage.getColor(color)));
        }

        for (TextFormatting format : TextFormatting.values()) {
            Assert.assertNotNull(CraftChatMessage.getColor(format));
            Assert.assertEquals(format, CraftChatMessage.getColor(CraftChatMessage.getColor(format)));
        }
    }
}
