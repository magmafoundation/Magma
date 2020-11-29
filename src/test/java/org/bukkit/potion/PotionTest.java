package org.bukkit.potion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class PotionTest extends AbstractTestingBase {

    @Test
    public void testEffectCompleteness() throws Throwable {
        Map<PotionType, String> effects = new EnumMap(PotionType.class);
        for (Object reg : net.minecraft.potion.PotionType.REGISTRY) {
            List<PotionEffect> eff = ((net.minecraft.potion.PotionType) reg).getEffects();
            if (eff.size() != 1) {
                continue;
            }
            int id = Potion.getIdFromPotion(eff.get(0).getPotion());
            PotionEffectType type = PotionEffectType.getById(id);
            assertNotNull(String.valueOf(id), PotionEffectType.getById(id));

            PotionType enumType = PotionType.getByEffect(type);
            assertNotNull(type.getName(), enumType);

            effects.put(enumType, enumType.name());
        }

        assertEquals(effects.entrySet().size(), PotionType.values().length - /* PotionTypes with no Effects */ 5);
    }
}
