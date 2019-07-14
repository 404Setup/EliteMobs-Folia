package com.magmaguy.elitemobs.mobconstructor.mobdata.aggressivemobs;

import com.magmaguy.elitemobs.config.mobproperties.MobPropertiesConfig;
import org.bukkit.entity.EntityType;

public class EliteSilverfish extends EliteMobProperties {

    public EliteSilverfish() {

        this.name = MobPropertiesConfig.getMobProperties().get(EntityType.SILVERFISH).getName();

        this.entityType = EntityType.SILVERFISH;

        this.defaultMaxHealth = 8;

        this.isEnabled = MobPropertiesConfig.getMobProperties().get(EntityType.SILVERFISH).isEnabled();

        if (this.isEnabled)
            eliteMobData.add(this);

    }

}
