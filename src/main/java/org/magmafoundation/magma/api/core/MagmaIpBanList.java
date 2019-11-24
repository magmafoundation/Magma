package org.magmafoundation.magma.api.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.server.management.IPBanList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;

/**
 * MagmaIpBanList
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 05:20 am
 */
public class MagmaIpBanList implements BanList {

    private final IPBanList list;

    public MagmaIpBanList(IPBanList list) {
        this.list = list;
    }

    @Override
    public BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");

        IPBanEntry entry = (IPBanEntry) list.getEntry(target);
        if (entry == null) {
            return null;
        }

        return new MagmaBanEntry(list, target, entry);
    }

    @Override
    public BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");

        IPBanEntry entry = new IPBanEntry(target, new Date(),
            StringUtils.isBlank(source) ? null : source, expires,
            StringUtils.isBlank(reason) ? null : reason);

        list.addEntry(entry);

        try {
            list.writeChanges();
        } catch (IOException e) {
            Bukkit.getLogger()
                .log(Level.SEVERE, "Failed to save banned-ips.json, {0}", e.getMessage());
        }

        return new MagmaBanEntry(list, target, entry);
    }

    @Override
    public Set<BanEntry> getBanEntries() {
        Builder<BanEntry> builder = ImmutableSet.builder();
        for (String target : list.getKeys()) {
            builder.add(new MagmaBanEntry(list, target, (IPBanEntry) list.getEntry(target)));
        }
        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");
        return list.isBanned(InetSocketAddress.createUnresolved(target, 0));
    }

    @Override
    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");
        list.removeEntry(target);
    }
}
