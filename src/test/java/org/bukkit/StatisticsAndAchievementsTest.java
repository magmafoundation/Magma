package org.bukkit;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import com.google.common.collect.HashMultiset;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import org.bukkit.craftbukkit.v1_12_R1.CraftStatistic;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class StatisticsAndAchievementsTest extends AbstractTestingBase {

    @Test
    @SuppressWarnings("unchecked")
    public void verifyStatisticMapping() throws Throwable {
        HashMultiset<Statistic> statistics = HashMultiset.create();
        for (StatBase statistic : StatList.ALL_STATS) {
            String name = statistic.statId;

            String message = String.format("org.bukkit.Statistic is missing: '%s'", name);

            Statistic subject = CraftStatistic.getBukkitStatistic(statistic);
            assertThat(message, subject, is(not(nullValue())));

            statistics.add(subject);
        }

        for (Statistic statistic : Statistic.values()) {
            String message = String.format("org.bukkit.Statistic.%s does not have a corresponding minecraft statistic", statistic.name());
            assertThat(message, statistics.remove(statistic, statistics.count(statistic)), is(greaterThan(0)));
        }
    }
}
