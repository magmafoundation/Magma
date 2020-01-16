package org.magmafoundation.magma.api.core.util;

/**
 * Cooldown
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 08:58 am
 */
public class Cooldown {

    public final int createTicks;
    public final int expireTicks;

    private Cooldown(int createTicksIn, int expireTicksIn) {
        this.createTicks = createTicksIn;
        this.expireTicks = expireTicksIn;
    }
}
