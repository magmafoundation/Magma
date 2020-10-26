package com.destroystokyo.paper.loottable;

import java.util.UUID;
import org.apache.commons.lang.Validate;

public interface CraftLootableInventory extends CraftLootable, LootableInventory {

    CraftLootableInventoryData getLootableData();

    LootableInventory getAPILootableInventory();

    @Override
    default boolean isRefillEnabled() {
        return getNMSWorld().paperConfig.autoReplenishLootables;
    }

    @Override
    default boolean hasBeenFilled() {
        return getLastFilled() != -1;
    }

    @Override
    default String getLootTableName() {
        return getLootableData().getLootable().getLootTableName();
    }

    @Override
    default String setLootTable(String name, long seed) {
        Validate.notNull(name);
        String prevLootTable = getLootTableName();
        getLootableData().getLootable().setLootTable(name, seed);
        return prevLootTable;
    }

    @Override
    default long getLootTableSeed() {
        return getLootableData().getLootable().getLootTableSeed();
    }

    @Override
    default void clearLootTable() {
        getLootableData().getLootable().clearLootTable();
    }

    @Override
    default boolean hasPlayerLooted(UUID player) {
        return getLootableData().hasPlayerLooted(player);
    }

    @Override
    default Long getLastLooted(UUID player) {
        return getLootableData().getLastLooted(player);
    }

    @Override
    default boolean setHasPlayerLooted(UUID player, boolean looted) {
        final boolean hasLooted = hasPlayerLooted(player);
        if (hasLooted != looted) {
            getLootableData().setPlayerLootedState(player, looted);
        }
        return hasLooted;
    }

    @Override
    default boolean hasPendingRefill() {
        long nextRefill = getLootableData().getNextRefill();
        return nextRefill != -1 && nextRefill > getLootableData().getLastFill();
    }

    @Override
    default long getLastFilled() {
        return getLootableData().getLastFill();
    }

    @Override
    default long getNextRefill() {
        return getLootableData().getNextRefill();
    }

    @Override
    default long setNextRefill(long refillAt) {
        if (refillAt < -1) {
            refillAt = -1;
        }
        return getLootableData().setNextRefill(refillAt);
    }
}
