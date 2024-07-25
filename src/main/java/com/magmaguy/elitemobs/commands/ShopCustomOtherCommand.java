package com.magmaguy.elitemobs.commands;

import com.magmaguy.elitemobs.menus.CustomShopMenu;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.util.Logger;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class ShopCustomOtherCommand extends AdvancedCommand {
    public ShopCustomOtherCommand() {
        super(List.of("shop"));
        addLiteral("custom");
        addArgument("player", new ArrayList<>());
        setUsage("/em shop custom <player>");
        setPermission("elitemobs.*");
        setDescription("Opens the EliteMobs shop for custom items.");
    }

    @Override
    public void execute() {
        try {
            CustomShopMenu.customShopConstructor(Bukkit.getPlayer(getStringArgument("player")));
        } catch (Exception ex) {
            Logger.sendMessage(getCurrentCommandSender(), "Failed to get player with that username!");
        }
    }
}