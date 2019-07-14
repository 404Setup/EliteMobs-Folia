package com.magmaguy.elitemobs.mobconstructor.mobdata.aggressivemobs;

import com.magmaguy.elitemobs.config.mobproperties.MobPropertiesConfig;
import org.bukkit.entity.EntityType;

public class EliteDrowned extends EliteMobProperties {

    public EliteDrowned() {

        this.name = MobPropertiesConfig.getMobProperties().get(EntityType.DROWNED).getName();

        this.entityType = EntityType.DROWNED;

        this.defaultMaxHealth = 20;

        this.isEnabled = MobPropertiesConfig.getMobProperties().get(EntityType.DROWNED).isEnabled();

        if (this.isEnabled)
            eliteMobData.add(this);

    }

}
