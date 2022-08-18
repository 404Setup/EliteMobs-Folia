package com.magmaguy.elitemobs.api.utils;

import com.magmaguy.elitemobs.combatsystem.CombatSystem;
import com.magmaguy.elitemobs.items.ItemTagger;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class EliteItemManager {
    //This is a utility class for easy and fast interfacing with EliteMobs items! Use it as a shortcut to reading source.

    /**
     * Calculates the Elite DPS of an item aka EDPS. This is the damage per second dealt to Elites, which may or may not
     * reflect the DPS dealt to other entities.
     *
     * @param itemStack ItemStack to analyze. Should be an {@ItemStack} with a GENERIC_ATTACK_SPEED and GENERIC_ATTACK_DAMAGE {@Attribute} but will handle any {@ItemStack}
     * @return The EDPS of the ItemStack. Defaults to 4 as that is the unarmed DPS!
     */
    public static double getDPS(@Nullable ItemStack itemStack) {
        return getDPS(getAttackSpeed(itemStack), getBaseDamage(itemStack)) + getDPS(getAttackSpeed(itemStack), getEliteDamage(itemStack));
    }

    /**
     * Calculates the DPS of any item
     *
     * @param attackSpeed Attack speed from item attribute
     * @param damage      Damage from attribute
     * @return DPS value
     */
    public static double getDPS(Double attackSpeed, Double damage) {
        if (attackSpeed == null || damage == null) return 4;
        //Mojang sees attack speed as the number of attacks per 20 ticks (1 second).
        double attackSpeedPerSecond = 1 / attackSpeed;
        return damage / attackSpeedPerSecond;
    }

    /**
     * Gets either the "real" damage of an {@ItemStack} based on the material attributes and further attribute modifications.
     *
     * @param itemStack {@ItemStack} to check the damage on.
     * @return Damage attribute or calculated stored damage. Defaults to 1.
     */
    public static double getBaseDamage(ItemStack itemStack) {
        //Default as per Minecraft defaults. This is the same as being unarmed.
        double defaultDamage = 1.0;
        if (itemStack == null) return defaultDamage;
        //Ranged damage works differently. 6.0 damage is the best case scenario for the average arrow so that is the value used.
        if (itemStack.getType() == Material.BOW || itemStack.getType() == Material.CROSSBOW) return 6.0;
        //Check the default modifiers - these are usually the only modifiers
        for (AttributeModifier attributeModifier : itemStack.getType().getDefaultAttributeModifiers(EquipmentSlot.HAND).get(Attribute.GENERIC_ATTACK_DAMAGE))
            defaultDamage = attributeCrawler(defaultDamage, attributeModifier);
        //Check custom modifiers - this should only happen when third party plugins step in
        if (itemStack.getItemMeta() != null &&
                itemStack.getItemMeta().getAttributeModifiers() != null &&
                itemStack.getItemMeta().getAttributeModifiers(EquipmentSlot.HAND).containsKey(Attribute.GENERIC_ATTACK_DAMAGE))
            for (AttributeModifier attributeModifier : itemStack
                    .getItemMeta()
                    .getAttributeModifiers()
                    .get(Attribute.GENERIC_ATTACK_SPEED))
                defaultDamage = attributeCrawler(defaultDamage, attributeModifier);
        return defaultDamage;
    }

    /**
     * Gets the GENERIC_ATTACK_SPEED {@Attribute} from any item - for players assuming a default {@Player} attack speed!
     *
     * @param itemStack ItemStack to analyze
     * @return Attack speed value
     */
    public static double getAttackSpeed(@Nullable ItemStack itemStack) {
        //Default as per Minecraft defaults. This is 1/default for the dps
        double defaultAttackSpeed = 4.0;
        if (itemStack == null) return defaultAttackSpeed;
        //Ranged damage works differently. Bows take 1s to draw and crossbows take 1.25s to draw.
        if (itemStack.getType() == Material.BOW) return 1.0;
        else if (itemStack.getType() == Material.CROSSBOW) return 0.8;
        //Check the default modifiers - these are usually the only modifiers
        for (AttributeModifier attributeModifier : itemStack.getType().getDefaultAttributeModifiers(EquipmentSlot.HAND).get(Attribute.GENERIC_ATTACK_SPEED))
            defaultAttackSpeed = attributeCrawler(defaultAttackSpeed, attributeModifier);
        //Check custom modifiers - this should only happen when third party plugins step in
        if (itemStack.getItemMeta() != null &&
                itemStack.getItemMeta().getAttributeModifiers() != null &&
                itemStack.getItemMeta().getAttributeModifiers(EquipmentSlot.HAND).containsKey(Attribute.GENERIC_ATTACK_SPEED))
            for (AttributeModifier attributeModifier : itemStack
                    .getItemMeta()
                    .getAttributeModifiers()
                    .get(Attribute.GENERIC_ATTACK_SPEED))
                defaultAttackSpeed = attributeCrawler(defaultAttackSpeed, attributeModifier);
        return defaultAttackSpeed;
    }

    /**
     * This goes through the attribute modifiers and applies them to a base value
     *
     * @param baseValue         Default value. For damage, it's 1. For attack speed, it's 4, as set by Minecraft.
     * @param attributeModifier Modifier to check against.
     * @return Value after being appropriately modified.
     */
    private static double attributeCrawler(double baseValue, AttributeModifier attributeModifier) {
        switch (attributeModifier.getOperation()) {
            case ADD_NUMBER:
                baseValue += attributeModifier.getAmount();
                break;
            case MULTIPLY_SCALAR_1:
            case ADD_SCALAR:
                //Yes, these two do the same thing. No, they shouldn't. This is a spigot thing.
                baseValue += baseValue * attributeModifier.getAmount();
                break;
        }
        return baseValue;
    }

    /**
     * Calculates the item level of a weapon.
     *
     * @param itemStack ItemStack to analyze.
     * @return Item level. Can't be lower than 0.
     */
    public static double getWeaponLevel(@Nullable ItemStack itemStack) {
        //The weapon level takes the base stats of the item and the elite damage attribute into account. Elite sharpness
        //is a part of the rarity, so it does not figure into the level math
        return Math.max(Math.round(
                (getDPS(getAttackSpeed(itemStack), getBaseDamage(itemStack)) +
                        getDPS(getAttackSpeed(itemStack), ItemTagger.getEliteDamageAttribute(itemStack)))
                        / CombatSystem.DPS_PER_LEVEL
        ), 0);
    }

    /**
     * Calculates the damage specific to EliteMobs. More specifically, scans for the elite enchantment level of sharpness
     * or power and adds the elite damage stored on the item. Note that this only adds elite enchantments, not vanilla ones.
     * Those get handled through vanilla means!
     *
     * @return The bonus damage
     */
    public static double getEliteDamage(@Nullable ItemStack itemStack) {
        if (itemStack == null) return 0;
        double eliteDamage = ItemTagger.getEliteDamageAttribute(itemStack);
        //Elite Items may have elite enchantments associated to an item
        int enchantmentLevel = ItemTagger.getEnchantment(itemStack.getItemMeta(), Enchantment.DAMAGE_ALL.getKey());
        if (enchantmentLevel > 0) {
            enchantmentLevel -= Enchantment.DAMAGE_ALL.getMaxLevel();
        } else {
            enchantmentLevel = ItemTagger.getEnchantment(itemStack.getItemMeta(), Enchantment.ARROW_DAMAGE.getKey());
            enchantmentLevel -= Enchantment.ARROW_DAMAGE.getMaxLevel();
        }

        if (enchantmentLevel < 1)
            //Note: this means sharpness works on bows and that power works on weapons. By default, this state is not reachable, so it doesn't really matter.
            enchantmentLevel += itemStack.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) + itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        if (enchantmentLevel == 0) return eliteDamage;
        //This is how vanilla sharpness works. Might as well use it for everything.
        return eliteDamage + 1 + enchantmentLevel * 0.5;

    }

    /**
     * Calculates the total DPS. This adds base damage to elite damage.
     *
     * @param itemStack ItemStack to scan
     * @return EDPS value
     */
    public static double getTotalDPS(@Nullable ItemStack itemStack) {
        if (itemStack == null) return 0;
        double bonusDamage = getEliteDamage(itemStack);
        if (bonusDamage == 0) return 0;
        return getDPS(getAttackSpeed(itemStack), bonusDamage);
    }

    /**
     * Calculates the elite protection value. This value is completely divorced from vanilla Minecraft as the vanilla method does not scale well for MMORPG-like progression.
     *
     * @param itemStack ItemStack to analyze
     * @return Elite protection value
     */
    public static double getEliteDefense(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null) return 0D;
        //Since there are 4 pieces of armor, this gets divided by 4
        double materialValue = CombatSystem.getMaterialTier(itemStack.getType()) / 4D;
        double defenseAttribute = ItemTagger.getEliteDefenseAttribute(itemStack);
        return materialValue + defenseAttribute;
    }

    public static double getArmorLevel(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null) return 0D;
        //Each piece of armor lowers damage by .25, and .25 damage represents 1 level, so the level is the protection * 4
        return getEliteDefense(itemStack) * 4D;
    }

    public static double getBonusEliteDefense(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null) return 0D;
        int enchantmentLevel = 0;
        enchantmentLevel = ItemTagger.getEnchantment(itemStack.getItemMeta(), Enchantment.PROTECTION_ENVIRONMENTAL.getKey());
        return enchantmentLevel * .25;
    }

    /**
     * Calculates the item level of any given {@ItemStack}
     *
     * @param itemStack ItemStack to analyze
     * @return Item level
     */
    public static double getItemLevel(@Nullable ItemStack itemStack) {
        double value = getWeaponLevel(itemStack);
        if (value > 3.0) return value;
        return getArmorLevel(itemStack);
    }

    public static int getRoundedItemLevel(@Nullable ItemStack itemStack) {
        return (int) Math.round(getItemLevel(itemStack));
    }

    public static boolean isWeapon(@Nullable ItemStack itemStack) {
        return getWeaponLevel(itemStack) > 3.0;
    }

    public static boolean isArmor(@Nullable ItemStack itemStack) {
        return getArmorLevel(itemStack) > 0;
    }

    public static double getDamageAtNextItemLevel(@Nullable ItemStack itemStack) {
        double attackDamage = getBaseDamage(itemStack);
        double attackSpeed = getAttackSpeed(itemStack);
        return CombatSystem.DPS_PER_LEVEL / 1 / attackSpeed + attackDamage;
    }

    public static void setEliteLevel(@Nullable ItemStack itemStack, int level) {
        if (itemStack == null) return;
        if (isWeapon(itemStack)) {
            double damage = level * CombatSystem.DPS_PER_LEVEL / 1 / getAttackSpeed(itemStack) - getBaseDamage(itemStack);
            if (damage > 0)
                ItemTagger.setEliteDamageAttribute(itemStack, damage);
        } else if (isArmor(itemStack)) {
            double defense = (level - CombatSystem.getMaterialTier(itemStack.getType())) / 4D;
            if (defense > 0)
                ItemTagger.setEliteDefenseAttribute(itemStack, defense);
        }
    }

    public static void tagArrow(@Nullable Projectile projectile) {
        if (projectile == null) return;
        if (!(projectile.getShooter() instanceof Player)) return;
        ItemTagger.setEliteDamageAttribute(projectile, getEliteDamage(((Player) projectile.getShooter()).getInventory().getItemInMainHand()));
    }

    public static double getArrowEliteDamage(@Nullable Projectile projectile) {
        return ItemTagger.getEliteDamageAttribute(projectile);
    }

}
