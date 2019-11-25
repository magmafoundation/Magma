package org.magmafoundation.magma.api.core.advancement;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.PlayerAdvancements;
import org.bukkit.advancement.Advancement;

public class MagmaAdvancementProgress implements org.bukkit.advancement.AdvancementProgress {

    private final MagmaAdvancement advancement;
    private final PlayerAdvancements playerAdvancements;
    private final net.minecraft.advancements.AdvancementProgress handle;

    public MagmaAdvancementProgress(
        MagmaAdvancement advancement, PlayerAdvancements playerAdvancements,
        AdvancementProgress handle) {
        this.advancement = advancement;
        this.playerAdvancements = playerAdvancements;
        this.handle = handle;
    }

    @Override
    public Advancement getAdvancement() {
        return advancement;
    }

    @Override
    public boolean isDone() {
        return handle.isDone();
    }

    @Override
    public boolean awardCriteria(String criteria) {
        return playerAdvancements.grantCriterion(advancement.getHandle(), criteria);
    }

    @Override
    public boolean revokeCriteria(String criteria) {
        return playerAdvancements.revokeCriterion(advancement.getHandle(), criteria);
    }

    @Override
    public Date getDateAwarded(String criteria) {
        CriterionProgress criterion = handle.getCriterionProgress(criteria);
        return (criterion == null) ? null : criterion.getObtained();
    }

    @Override
    public Collection<String> getRemainingCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(handle.getRemaningCriteria()));
    }

    @Override
    public Collection<String> getAwardedCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(handle.getCompletedCriteria()));
    }
}
