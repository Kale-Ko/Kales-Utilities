package com.kale_ko.api.spigot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

public class Task {
    private static final Map<UUID, Integer> TASKS = new HashMap<>();
    private final UUID uuid;

    public int run = 1;

    public Task(UUID uuid) {
        this.uuid = uuid;
    }

    public void setId(int id) {
        TASKS.put(uuid, id);
    }

    public int getId() {
        return TASKS.get(uuid);
    }

    public boolean hasId() {
        return TASKS.containsKey(uuid);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(TASKS.get(uuid));
        TASKS.remove(uuid);
    }
}