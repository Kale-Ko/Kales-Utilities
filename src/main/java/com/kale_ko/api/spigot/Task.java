/**
    @license
    MIT License
    Copyright (c) 2021 Kale Ko
    See https://kaleko.ga/license.txt
*/

package com.kale_ko.api.spigot;

import org.bukkit.Bukkit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Task {
    private static final Map<UUID, Integer> TASKS = new HashMap<>();
    private final UUID uuid;

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