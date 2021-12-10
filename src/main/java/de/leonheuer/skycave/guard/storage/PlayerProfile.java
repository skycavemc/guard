package de.leonheuer.skycave.guard.storage;

import java.net.InetAddress;
import java.util.UUID;

public class PlayerProfile {

    private final UUID uuid;
    private final InetAddress ip;
    private final DataManager dm;
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

    public void addAfk() {
        afk++;
        dm.writePlayerData(this);
    }

    public boolean removeAfk() {
        if (afk > 0) {
            afk--;
            dm.writePlayerData(this);
            return true;
        } else {
            return false;
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public InetAddress getIp() {
        return ip;
    }

}
