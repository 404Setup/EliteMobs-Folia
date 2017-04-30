/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmaguy.elitemobs.minorpowers;

import com.magmaguy.elitemobs.EliteMobs;
import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.powerstances.MinorPowerPowerStance;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

/**
 * Created by MagmaGuy on 28/04/2017.
 */
public class AttackWeb extends MinorPowers implements Listener {

    private EliteMobs plugin;
    MetadataHandler metadataHandler = new MetadataHandler(plugin);
    String powerMetadata = metadataHandler.attackWebMD;
    private int processID;

    public AttackWeb(Plugin plugin) {

        this.plugin = (EliteMobs) plugin;

    }

    @Override
    public void applyPowers(Entity entity) {

        entity.setMetadata(powerMetadata, new FixedMetadataValue(plugin, true));
        MinorPowerPowerStance minorPowerPowerStance = new MinorPowerPowerStance(plugin);
        minorPowerPowerStance.itemEffect(entity);

    }

    @Override
    public boolean existingPowers(Entity entity) {

        return entity.hasMetadata(powerMetadata);

    }

    @EventHandler
    public void attackWeb(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        Block block = damagee.getLocation().getBlock();
        Material originalMaterial = block.getType();

        if (damager.hasMetadata(metadataHandler.attackFreezeMD) || damager instanceof Projectile &&
                ProjectileMetadataDetector.projectileMetadataDetector((Projectile)damager , metadataHandler.attackFreezeMD)) {

            return;

        }

        if (damagee.hasMetadata("WebCooldown")) {

            return;

        }

        if (damager.hasMetadata(powerMetadata)) {

            //if a block spawned by the plugin is already in place, skip effect
            if (block.hasMetadata("TemporaryBlock")) {

                return;

            }

            processID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

                int counter = 0;

                @Override
                public void run() {

                    webEffectApplier(counter, damagee, originalMaterial, block);
                    counter++;

                }

            }, 2, 1);

        }

        if (damager instanceof Projectile) {

            if (ProjectileMetadataDetector.projectileMetadataDetector((Projectile) damager, powerMetadata)) {

                processID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

                    int counter = 0;

                    @Override
                    public void run() {

                        webEffectApplier(counter, damagee, originalMaterial, block);
                        counter++;

                    }

                }, 1, 1);

            }

        }


    }

    private void webEffectApplier(int counter, Entity damagee, Material originalMaterial, Block block) {

        if (counter == 0) {

            damagee.setMetadata("WebCooldown", new FixedMetadataValue(plugin, true));
            //make sure the block isn't already placed by the plugin
            originalMaterial = block.getType();
            block.setType(Material.WEB);

            block.setMetadata("TemporaryBlock", new FixedMetadataValue(plugin, true));

        }

        if (counter == 20 * 4) {

            block.setType(originalMaterial);

            block.removeMetadata("TemporaryBlock", plugin);

        }

        if (counter == 20 * 7) {

            damagee.removeMetadata("WebCooldown", plugin);
            Bukkit.getScheduler().cancelTask(processID);

        }

    }

}