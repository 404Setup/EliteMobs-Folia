package com.magmaguy.elitemobs.config.contentpackages.premade;

import com.magmaguy.elitemobs.config.contentpackages.ContentPackagesConfigFields;
import com.magmaguy.elitemobs.utils.DiscordLinks;
import org.bukkit.World;

import java.util.List;
import java.util.Map;

public class EnchantmentChallenge11 extends ContentPackagesConfigFields {
    public EnchantmentChallenge11() {
        super("enchantment_challenge_11_sanctum",
                true,
                "&2[lvl 000-200] &6Enchantment Challenge 11",
                List.of("&fAn enchantment challenge dungeon!"),
                DiscordLinks.premiumMinidungeons,
                ContentPackagesConfigFields.DungeonSizeCategory.SANCTUM,
                "em_id_enchantment_challenge_11",
                World.Environment.THE_END,
                true,
                "em_id_enchantment_challenge_11,-15.5,94,15.5,-135,0.0",
                "em_id_enchantment_challenge_11,14.5,65,-12.5,45,0",
                0,
                "Difficulty: &4solo hard content!",
                "&bChallenge time!",
                "&bYou have left the enchantment challenge!",
                List.of("filename=enchantment_boss_void_haze.yml"),
                "em_id_enchantment_challenge_11",
                1,
                false);
        setDifficulties(List.of(
                Map.of("name", "normal", "id", 0)));
        setEnchantmentChallenge(true);
        setMaxPlayerCount(1);
    }
}
