package com.magmaguy.elitemobs.commands;

import com.magmaguy.elitemobs.api.PlayerPreTeleportEvent;
import com.magmaguy.elitemobs.config.DefaultConfig;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.SenderType;

import java.util.List;

public class SpawnTeleportCommand extends AdvancedCommand {
    public SpawnTeleportCommand() {
        super(List.of("spawntp"));
        setPermission("elitemobs.spawntp");
        setUsage("/em spawntp");
        setDescription("Teleports players to the server spawn.");
        setSenderType(SenderType.PLAYER);
    }

    @Override
    public void execute() {
        if (DefaultConfig.getDefaultSpawnLocation() != null)
            PlayerPreTeleportEvent.teleportPlayer(getCurrentPlayerSender(), DefaultConfig.getDefaultSpawnLocation());
    }
}