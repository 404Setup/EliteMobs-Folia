package com.magmaguy.elitemobs.events.actionevents;

import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.EventsConfig;
import com.magmaguy.elitemobs.events.mobs.Kraken;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.concurrent.ThreadLocalRandom;

public class KrakenEvent implements Listener {

    @EventHandler
    public void onFishingStart(PlayerFishEvent event) {

        if (!ConfigValues.eventsConfig.getBoolean(EventsConfig.KRAKEN_ENABLED)) return;
        if (event.getHook().getLocation().getBlock().isEmpty() || event.getHook().getLocation().getBlock().isEmpty() &&
                !event.getHook().getLocation().getBlock().getType().equals(Material.WATER)) return;
        if (ThreadLocalRandom.current().nextDouble() > ConfigValues.eventsConfig.getDouble(EventsConfig.KRAKEN_CHANCE_ON_FISH))
            return;

        Kraken.spawnKraken(event.getHook().getLocation());

    }

}