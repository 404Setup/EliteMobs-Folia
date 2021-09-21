package com.magmaguy.elitemobs.config.dungeonpackager.premade;

import com.magmaguy.elitemobs.config.dungeonpackager.DungeonPackagerConfigFields;
import com.magmaguy.elitemobs.utils.DiscordLinks;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class AirShipMinidungeon extends DungeonPackagerConfigFields {
    public AirShipMinidungeon() {
        super(
                "airship_minidungeon",
                false,
                "&6The Airship",
                DungeonLocationType.SCHEMATIC,
                Arrays.asList("&fAn awesome airship full of undead pirates!",
                        "&6Credits: MagmaGuy, 69OzCanOfBepis",
                        "&6and Realm of Lotheridon"),
                Arrays.asList(
                        "airship_tier_75_boss.yml:0.0,31.5,112.0",
                        "airship_tier_65_silverfish.yml:-8.0,21.5,102.0",
                        "airship_tier_65_silverfish.yml:-10.0,21.5,97.0",
                        "airship_tier_65_silverfish.yml:-13.0,21.5,93.0",
                        "airship_tier_65_silverfish.yml:-14.0,7.5,98.0",
                        "airship_tier_65_silverfish.yml:-13.0,9.5,86.0",
                        "airship_tier_65_silverfish.yml:-12.0,9.5,76.0",
                        "airship_tier_65_silverfish.yml:-11.0,9.5,62.0",
                        "airship_tier_65_silverfish.yml:-9.0,9.5,53.0",
                        "airship_tier_65_silverfish.yml:0.0,2.5,31.0",
                        "airship_tier_65_silverfish.yml:0.0,2.5,46.0",
                        "airship_tier_65_silverfish.yml:0.0,1.5,61.0",
                        "airship_tier_65_silverfish.yml:0.0,1.5,78.0",
                        "airship_tier_65_silverfish.yml:0.0,1.5,91.0",
                        "airship_tier_65_silverfish.yml:0.0,2.5,105.0",
                        "airship_tier_70_miniboss.yml:0.0,6.5,102.0",
                        "airship_tier_70_miniboss_2.yml:0.0,6.5,38.0",
                        "airship_tier_65_silverfish.yml:15.0,7.5,96.0",
                        "airship_tier_65_silverfish.yml:13.0,10.5,83.0",
                        "airship_tier_65_silverfish.yml:12.0,10.5,74.0",
                        "airship_tier_65_silverfish.yml:11.0,9.5,62.0",
                        "airship_tier_65_silverfish.yml:9.0,10.5,52.0",
                        "airship_tier_65_silverfish.yml:8.0,10.5,40.0",
                        "airship_tier_65_silverfish.yml:13.0,21.5,93.0",
                        "airship_tier_65_silverfish.yml:10.0,21.5,97.0",
                        "airship_tier_65_silverfish.yml:8.0,21.5,104.0",
                        "airship_tier_45_knockback.yml:7.0,16.5,42.0",
                        "airship_tier_45_knockback.yml:-8.0,16.5,42.0",
                        "airship_tier_45_witherblind.yml:-3.0,16.5,42.0",
                        "airship_tier_45_witherblind.yml:3.0,16.5,42.0",
                        "airship_tier_45_witherblind.yml:-6.0,14.5,59.0",
                        "airship_tier_45_knockback.yml:-6.0,14.5,53.0",
                        "airship_tier_45_knockback.yml:7.0,14.5,59.0",
                        "airship_tier_45_knockback.yml:2.0,14.5,75.0",
                        "airship_tier_45_witherblind.yml:-3.0,14.5,74.0",
                        "airship_tier_50_scourge.yml:2.0,14.5,89.0",
                        "airship_tier_50_scourge.yml:-2.0,14.5,89.0",
                        "airship_tier_60_mess_hall_mate.yml:8.0,14.5,89.0",
                        "airship_tier_60_mess_hall_mate.yml:10.0,14.5,90.0",
                        "airship_tier_60_mess_hall_mate.yml:8.0,14.5,97.0",
                        "airship_tier_60_mess_hall_mate.yml:-8.0,14.5,84.0",
                        "airship_tier_60_mess_hall_mate.yml:-9.0,14.5,87.0",
                        "airship_tier_60_mess_hall_mate.yml:-9.0,14.5,101.0",
                        "airship_tier_50_scourge.yml:0.0,14.5,98.0",
                        "airship_tier_65_bodyguard.yml:3.0,14.5,109.0",
                        "airship_tier_50_powder_monkeys.yml:10.0,5.5,88.0",
                        "airship_tier_50_powder_monkeys.yml:4.0,5.5,85.0",
                        "airship_tier_50_powder_monkeys.yml:6.0,5.5,77.0",
                        "airship_tier_50_powder_monkeys.yml:3.0,5.5,72.0",
                        "airship_tier_65_boatswain_boss.yml:3.0,5.5,74.0",
                        "airship_tier_65_boatswain_boss.yml:3.0,5.5,54.0",
                        "airship_tier_50_powder_monkeys.yml:0.0,5.5,59.0",
                        "airship_tier_50_powder_monkeys.yml:-4.0,5.5,53.0",
                        "airship_tier_50_powder_monkeys.yml:-4.0,5.5,67.0",
                        "airship_tier_50_powder_monkeys.yml:-6.0,5.5,73.0",
                        "airship_tier_50_powder_monkeys.yml:-5.0,5.5,85.0",
                        "airship_tier_50_powder_monkeys.yml:-9.0,5.5,86.0",
                        "airship_tier_65_boatswain_boss.yml:-7.0,5.5,80.0",
                        "airship_tier_70_miniboss_wayfinder.yml:-2.0,21.5,115.0",
                        "airship_tier_65_bodyguard.yml:-3.0,21.5,109.0",
                        "airship_tier_65_bodyguard.yml:3.0,21.5,109.0",
                        "airship_tier_45_witherblind.yml:8.0,21.5,82.0",
                        "airship_tier_45_witherblind.yml:-5.0,21.5,84.0",
                        "airship_tier_45_knockback.yml:11.0,26.5,96.0",
                        "airship_tier_45_knockback.yml:-11.0,26.5,96.0",
                        "airship_tier_45_knockback.yml:1.0,21.5,98.0",
                        "airship_tier_65_bodyguard.yml:-3.0,14.5,109.0"),
                new ArrayList<>(),
                DiscordLinks.premiumMinidungeons,
                DungeonSizeCategory.MINIDUNGEON,
                null,
                "em_airship.schem",
                null,
                true,
                new Vector(38, -2, 0),
                new Vector(-40, 70, 146),
                new Vector(12, 14, 56),
                0D,
                0D,
                0,
                "Difficulty: &6Medium\n" +
                        "$bossCount bosses, from level $lowestTier to $highestTier\n" +
                        "&6An awesome airship full of undead pirates\n" +
                        "&6for experienced players!",
                "&8[EM] &cEntering hostile air zone!",
                "&8[EM] &cLeaving hostile air zone!");
    }
}
