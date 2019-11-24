package org.magmafoundation.magma.api.core;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.server.management.IPBanList;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.magmafoundation.magma.api.bridge.IBridgeBanEntry;

/**
 * MagmaBanEntry
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 04:39 am
 */
public class MagmaIPBanEntry implements BanEntry {

    private final IPBanList banList;
    private final String target;
    private Date created;
    private String source;
    private Date expiration;
    private String reason;

    public MagmaIPBanEntry(IPBanList banList, String target, IPBanEntry entry) {
        this.banList = banList;
        this.target = target;
        this.created = ((IBridgeBanEntry) entry).getBanStartDate() != null ? new Date(((IBridgeBanEntry) entry).getBanStartDate().getTime()) : null;
        this.source = entry.getBannedBy();
        this.expiration = entry.getBanEndDate() != null ? new Date(entry.getBanEndDate().getTime()) : null;
        this.reason = entry.getBanReason();
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public Date getCreated() {
        return this.created == null ? null : (Date) this.created.clone();
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public Date getExpiration() {
        return this.expiration == null ? null : (Date) this.expiration.clone();
    }

    @Override
    public void setExpiration(Date expiration) {
        if (expiration != null && expiration.getTime() == new Date(0, 0, 0, 0, 0, 0).getTime()) {
            expiration = null; // Makes a ban forever
        }
        this.expiration = expiration;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void save() {
        IPBanEntry entry = new IPBanEntry(target, this.created, this.source, this.expiration, this.reason);
        this.banList.addEntry(entry);
        try {
            this.banList.writeChanges();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-ips.json, {0}", e.getMessage());
        }
    }
}
