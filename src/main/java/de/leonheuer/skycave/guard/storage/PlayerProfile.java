package de.leonheuer.skycave.guard.storage;

import java.net.InetAddress;
import java.util.UUID;

public class PlayerProfile {

    private final UUID uuid;
    private final DataManager dm;
    private InetAddress ip;
    private int afk;

    public PlayerProfile(int afk, UUID uuid, InetAddress ip, DataManager dm) {
        this.afk = afk;
        this.uuid = uuid;
        this.ip = ip;
        this.dm = dm;
        dm.writePlayerData(this);
    }

    public int getAfk() {
        return afk;
    }

    public void setAfk(int afk) {
        this.afk = afk;
        dm.writePlayerData(this);
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
        dm.writePlayerData(this);
    }

    public UUID getUuid() {
        return uuid;
    }
}
