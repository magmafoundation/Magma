package org.magmafoundation.magma.api.bridge;

import com.mojang.authlib.GameProfile;

/**
 * IBridgeDedicatedPlayerList
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 07:32 pm
 */
public interface IBridgeDedicatedPlayerList {

    void removePlayerFromWhitelist(GameProfile profile);

    void addPlayerToWhitelist(GameProfile profile);

}
