package org.magmafoundation.magma.bridge.minecraft.entity;

/**
 * BridgeMobEntity
 *
 * @author Redned
 * @since 18/01/2020 - 03:08 pm
 */
public interface BridgeMobEntity {

    boolean bridge$isPersistenceRequired();

    void bridge$setPersistenceRequired(boolean persistenceRequired);
}
