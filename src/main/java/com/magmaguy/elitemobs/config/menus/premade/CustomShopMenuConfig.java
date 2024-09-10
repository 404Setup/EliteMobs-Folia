package com.magmaguy.elitemobs.config.menus.premade;

import com.magmaguy.elitemobs.config.ConfigurationEngine;
import com.magmaguy.elitemobs.config.menus.MenusConfigFields;
import com.magmaguy.elitemobs.utils.ItemStackSerializer;
import com.magmaguy.magmacore.util.ItemStackGenerator;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class CustomShopMenuConfig extends MenusConfigFields {
    public static String shopName;
    public static ItemStack rerollItem;
    public static int rerollSlot;
    public static List<Integer> storeSlots;
    public static String messageFullInventory;

    public CustomShopMenuConfig() {
        super("custom_shop_menu", true);
    }

    @Override
    public void processAdditionalFields() {
        shopName = ConfigurationEngine.setString(file, fileConfiguration, "Shop name", "[EM] Custom Shop", true);
        ItemStackSerializer.serialize("Reroll button",
                ItemStackGenerator.generateSkullItemStack("magmaguy",
                        "&4&lEliteMobs &r&cby &4&lMagmaGuy",
                        List.of("&8Support the plugins you enjoy!")),
                fileConfiguration);
        rerollItem = ItemStackSerializer.deserialize("Reroll button", fileConfiguration);
        rerollSlot = ConfigurationEngine.setInt(fileConfiguration, "Reroll button slot", 4);
        storeSlots = ConfigurationEngine.setList(file, fileConfiguration, "Store item slots", new ArrayList<>(List.of(9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42,
                43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53)), false);
        messageFullInventory = ConfigurationEngine.setString(file, fileConfiguration, "Full inventory message",
                "[EliteMobs] &4Your inventory is full! You can't buy items until you get some free space.", true);
    }

}
