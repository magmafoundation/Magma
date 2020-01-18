package org.magmafoundation.magma.mixin.core.minecraft.advancement;

import net.minecraft.advancements.CriterionProgress;

import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * MixinAdvancementProgress
 *
 * @author Redned
 * @since 18/01/2019 - 11:46 am
 */
@Mixin(net.minecraft.advancements.AdvancementProgress.class)
@Implements(@Interface(iface = AdvancementProgress.class, prefix = "progress$"))
public abstract class MixinAdvancementProgress implements AdvancementProgress {

    @Shadow @Final private Map<String, CriterionProgress> criteria;
    @Shadow public abstract boolean shadow$isDone();
    @Shadow public abstract boolean grantCriterion(String criterionIn);
    @Shadow public abstract boolean revokeCriterion(String criterionIn);
    @Shadow public abstract Iterable<String> getRemaningCriteria();
    @Shadow public abstract Iterable<String> getCompletedCriteria();

    @Override
    public Advancement getAdvancement() {
        return null; // TODO
    }

    @Intrinsic
    public boolean progress$isDone() {
        return shadow$isDone();
    }

    @Override
    public boolean awardCriteria(String criteria) {
        return grantCriterion(criteria);
    }

    @Override
    public boolean revokeCriteria(String criteria) {
        return revokeCriterion(criteria);
    }

    @Override
    public Date getDateAwarded(String criteria) {
        if (!this.criteria.containsKey(criteria))
            return null;

        return this.criteria.get(criteria).getObtained();
    }

    @Override
    public Collection<String> getRemainingCriteria() {
        Collection<String> criteria = new ArrayList<>();
        getRemaningCriteria().iterator().forEachRemaining(criteria::add);
        return criteria;
    }

    @Override
    public Collection<String> getAwardedCriteria() {
        Collection<String> criteria = new ArrayList<>();
        getCompletedCriteria().iterator().forEachRemaining(criteria::add);
        return criteria;
    }
}
