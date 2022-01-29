package de.leonheuer.skycave.guard.enums;

import org.bukkit.ChatColor;

public enum Message {

    // default
    PREFIX("&e&l| &cGuard &8» "),
    UNKNOWN_COMMAND("&cDieser Befehl existiert nicht."),
    NOPERM("&cDu hast keine Rechte für diesen Befehl."),
    NOPLAYER("&cDu musst ein Spieler sein."),
    MISSING_PLAYER("&cBitte gib einen Spieler an!"),
    PLAYER_NOONLINE("&cDer Spieler %player ist nicht online."),
    PLAYER_UNKNOWN("&cDer Spieler %player ist nicht bekannt."),
    CONSOLE_ONLY("&cDieser Befehl ist nur über die Konsole ausführbar!"),
    BLOCKED("&cDieser Befehl wurde blockiert."),

    // kick reasons
    KICK_ANTIBOT("&cEs versuchen momentan zu viele Spieler zu joinen.\nBitte warte einen Augenblick."),
    KICK_FULL("&cDer Server ist voll!\n&cMit einem Rang kannst du den vollen Server betreten!"),
    KICK_WHITELIST("&cDu stehst nicht auf der Whitelist!"),

    // staff notifier
    NOTIFIER_FULL_SERVER("&c%p &7wurde gekickt, da der Server voll ist."),
    NOTIFIER_WHITELIST("&c%p &7wurde gekickt, da die Whitelist aktiviert ist."),
    NOTIFIER_ANTIBOT("&c%p &7wurde gekickt, da momentan zu viele Spieler versuchen zu joinen."),

    // notify command
    NOTIFY_AAC("&c&lAAC &8» &c%1 &7nutzt &c%2&7? &8(&7%3&7, %4&8)"),
    NOTIFY_AAC_VL("&c&lAAC &8» &c%1 &7nutzt &c%2 &6%5&7? &8(&7%3&7, %4&8)"),
    NOTIFY_MISSING_ARGS("&cArgumente gegeben: %0, Argumente erwartet: %1"),

    // kickall command
    KICKALL_PROCESS("&cKicke alle Spieler..."),
    KICKALL_REASON("&cBitte gib einen Grund an!"),

    // lookup command
    LOOKUP_ACCOUNTS("&eAccounts von &c%player&7: &7%accounts"),
    LOOKUP_UNKNOWN("&cDer Spieler %player ist unbekannt."),
    LOOKUP_MISSING("&cBitte gib einen Name an."),
    LOOKUP_IP_NOT_FOUND("&cDie IP-Adresse des Spielers %player ist unbekannt."),

    // kontrolle command
    KONTROLLE_NODATA("&cEs hat noch keine Kontrolle stattgefunden."),
    KONTROLLE_SETNOW("&aZeitpunkt der Kontrolle gesetzt."),
    KONTROLLE_TIME("&eLetzte Kontrolle: &7%time &8(&7vor &c%duration&8) &7von &c%player"),
    KONTROLLE_SPEC_ENTER("&bDu betrittst den Zuschauermodus."),
    KONTROLLE_SPEC_LEAVE("&eDu verlässt den Zuschauermodus."),
    KONTROLLE_WRONGARGS("&cUngültiges Argument. Verfügbar: /kontrolle spec, /kontrolle now"),

    // CountEntity command
    COUNTENTITY_MISSINGARG("§cBitte gib ein Entity oder eine Welt (-w:welt) an."),
    COUNTENTITY_HEADER("§e%entity §7Entities: §8(§fWelt§8: §fAnzahl§8)"),
    COUNTENTITY_OUTPUT("§8» §e%world§8: §c%count"),
    COUNTENTITY_INVALID("§c%entity ist ein ungültiges Entity."),

    // CountEntity region
    COUNTENTITY_REGION_HEADER("§7Entities in der Region §e%region§7: §8(§fEntity§8: §fAnzahl§8)"),
    COUNTENTITY_REGION_OUTPUT("§8» §e%entity§8: §c%count"),
    COUNTENTITY_REGION_INVALID("§c%region ist eine ungültige Region."),
    COUNTENTITY_REGION_INVALID_WORLD("§c%world ist eine ungültige Welt."),
    COUNTENTITY_REGION_MISSING("§cBitte gib eine Region an."),

    // GC command
    GC_TPS("§6TPS: %tps"),
    GC_RAM("§6RAM Auslastung: %ram%"),
    GC_WORLDS_HEADER("§8» §7Welt: §fEntities§8, §fTileEntities§8, §fSpieler§8, §fChunks"),
    GC_WORLDS("§8» §7%world: §f%entity§8, §f%tile§8, §f%players§8, §f%chunks"),

    // help command
    HELP_HEADER("&eDie wichtigsten Grundbefehle:"),
    HELP_HUB("&a/hub &8» &7Bringt dich zurück in die Lobby."),
    HELP_IS("&a/is &8» &7Teleportiert dich auf deine Insel. Falls du keine hast, wird eine neue erstellt."),
    HELP_SPAWN("&a/spawn &8» &7Teleportiert dich zum Spawn."),
    HELP_MSG("&a/msg <spieler> <nachricht> &8» &7Versendet eine private Nachricht."),
    HELP_WIKI("&eFür eine ausführliche Befehlsübersicht siehe: &b&nhttps://skybee.gitbook.io/"),
    ;

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage () {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getWithPrefix() {
        return PREFIX.getMessage() + ChatColor.translateAlternateColorCodes('&', message);
    }

}
