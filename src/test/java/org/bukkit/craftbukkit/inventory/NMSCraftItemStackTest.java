package org.bukkit.craftbukkit.inventory;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class NMSCraftItemStackTest extends AbstractTestingBase {

    @Test
    public void testCloneEnchantedItem() throws Exception {
        net.minecraft.item.ItemStack nmsItemStack = new net.minecraft.item.ItemStack(Items.POTIONITEM);
        nmsItemStack.addEnchantment(Enchantments.SHARPNESS, 1);
        ItemStack itemStack = CraftItemStack.asCraftMirror(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assertThat(clone.getType(), is(itemStack.getType()));
        assertThat(clone.getAmount(), is(itemStack.getAmount()));
        assertThat(clone.getDurability(), is(itemStack.getDurability()));
        assertThat(clone.getEnchantments(), is(itemStack.getEnchantments()));
        assertThat(clone.getTypeId(), is(itemStack.getTypeId()));
        assertThat(clone.getData(), is(itemStack.getData()));
        assertThat(clone, is(itemStack));
    }

    @Test
    public void testCloneNullItem() throws Exception {
        net.minecraft.item.ItemStack nmsItemStack = null;
        ItemStack itemStack = CraftItemStack.asCraftMirror(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assertThat(clone, is(itemStack));
    }
}
