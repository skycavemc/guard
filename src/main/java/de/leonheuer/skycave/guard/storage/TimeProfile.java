package de.leonheuer.skycave.guard.storage;

import java.time.LocalDateTime;

public class TimeProfile {

    private final DataManager dm;
    private LocalDateTime time;
    private String name;

    public TimeProfile(DataManager dm, LocalDateTime time, String name) {
        this.dm = dm;
        this.time = time;
        this.name = name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
