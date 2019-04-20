package com.magmaguy.elitemobs.commands.shops;

import com.magmaguy.elitemobs.ChatColorConverter;
import com.magmaguy.elitemobs.commands.guiconfig.SignatureItem;
import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.TranslationConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BuyOrSellMenu implements Listener {

    private static final String SHOP_NAME = "Shop menu";

    public static void constructBuyOrSellMenu(Player player, String buyString) {

        Inventory shopInventory = Bukkit.createInventory(player, 18, SHOP_NAME);
        shopInventory.setItem(4, SignatureItem.SIGNATURE_ITEMSTACK);
        ItemStack sellItem = new ItemStack(Material.REDSTONE);
        ItemMeta sellMeta = sellItem.getItemMeta();
        sellMeta.setDisplayName(ChatColorConverter.convert(ConfigValues.translationConfig.getString(TranslationConfig.BUY_OR_SELL_SELL_ITEMS)));
        sellMeta.setLore(Arrays.asList("Sell items you looted from", "Elite Mobs! These should", "show their worth on", "the lore."));
        sellItem.setItemMeta(sellMeta);

        ItemStack buyItem = new ItemStack(Material.EMERALD);
        ItemMeta buyMeta = buyItem.getItemMeta();
        buyMeta.setDisplayName(buyString);
        buyMeta.setLore(Arrays.asList(buyString));
        buyItem.setItemMeta(buyMeta);

        shopInventory.setItem(11, buyItem);
        shopInventory.setItem(15, sellItem);

        player.openInventory(shopInventory);

    }

    @EventHandler
    public void onInventoryInteraction(InventoryClickEvent event) {

        if (!SharedShopElements.inventoryNullPointerPreventer(event)) return;
        if (!event.getInventory().getName().equals(SHOP_NAME)) return;

        //reroll loot button
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColorConverter.convert(SignatureItem.SIGNATURE_ITEMSTACK.getItemMeta().getDisplayName()))) {
            event.setCancelled(true);
            return;
        }

        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColorConverter.convert(ConfigValues.translationConfig.getString(TranslationConfig.BUY_OR_SELL_CUSTOM_ITEMS)))) {
            CustomShopHandler.customShopConstructor((Player) event.getWhoClicked());
            event.setCancelled(true);
            return;
        }

        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColorConverter.convert(ConfigValues.translationConfig.getString(TranslationConfig.BUY_OR_SELL_DYNAMIC_ITEMS)))) {
            ShopHandler.shopConstructor((Player) event.getWhoClicked());
            event.setCancelled(true);
            return;
        }

        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColorConverter.convert(ConfigValues.translationConfig.getString(TranslationConfig.BUY_OR_SELL_SELL_ITEMS)))) {
            SellMenu sellMenu = new SellMenu();
            sellMenu.constructSellMenu((Player) event.getWhoClicked());
            event.setCancelled(true);
            return;
        }

    }

}