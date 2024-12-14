package com.magmaguy.elitemobs.powers.scripts;

import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.powers.scripts.caching.ScriptParticlesBlueprint;
import one.tranic.irs.PluginSchedulerBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class ScriptParticles {

    ScriptParticlesBlueprint particlesBlueprint;

    public ScriptParticles(ScriptParticlesBlueprint particlesBlueprint) {
        this.particlesBlueprint = particlesBlueprint;
    }

    public void visualize(ScriptActionData scriptActionData, Location location, EliteScript eliteScript) {
        particlesBlueprint.getParticleBlueprints().forEach(particleBlueprint -> new ScriptParticle(particleBlueprint).visualize(scriptActionData, location, eliteScript));
    }

    private class ScriptParticle {

        private final ScriptParticlesBlueprint.ScriptParticleBlueprint particleBlueprint;

        public ScriptParticle(ScriptParticlesBlueprint.ScriptParticleBlueprint scriptParticlesBlueprint) {
            this.particleBlueprint = scriptParticlesBlueprint;
        }

        private void visualize(ScriptActionData scriptActionData, Location location, EliteScript eliteScript) {
            double x = particleBlueprint.getX();
            double y = particleBlueprint.getY();
            double z = particleBlueprint.getZ();
            int amount = particleBlueprint.getAmount();

            if (particleBlueprint.getRelativeVectorBlueprint() != null) {
                ScriptRelativeVector scriptRelativeVector = null;
                scriptRelativeVector = new ScriptRelativeVector(this.particleBlueprint.getRelativeVectorBlueprint(), eliteScript, location);

                Vector movementVector = scriptRelativeVector.getVector(scriptActionData);
                amount = 0;
                x = movementVector.getX();
                y = movementVector.getY();
                z = movementVector.getZ();
            }

            final int amount2 = amount;
            final double x2 = x;
            final double y2 = y;
            final double z2 = z;


            if (particleBlueprint.getParticle().equals(Particle.DUST)) {
                PluginSchedulerBuilder.builder(MetadataHandler.PLUGIN)
                        .sync(location)
                        .task(() -> location.getWorld().spawnParticle(
                                particleBlueprint.getParticle(),
                                location,
                                amount2,
                                x2,
                                y2,
                                z2,
                                particleBlueprint.getSpeed(),
                                new Particle.DustOptions(
                                        Color.fromRGB(
                                                particleBlueprint.getRed(),
                                                particleBlueprint.getGreen(),
                                                particleBlueprint.getBlue()),
                                        1))).run();
            } else if (particleBlueprint.getParticle().equals(Particle.DUST_COLOR_TRANSITION)) {
                PluginSchedulerBuilder.builder(MetadataHandler.PLUGIN)
                        .sync(location)
                        .task(() -> location.getWorld().spawnParticle(
                                particleBlueprint.getParticle(),
                                location,
                                amount2,
                                x2,
                                y2,
                                z2,
                                particleBlueprint.getSpeed(),
                                new Particle.DustTransition(
                                        Color.fromRGB(
                                                particleBlueprint.getRed(),
                                                particleBlueprint.getGreen(),
                                                particleBlueprint.getBlue()),
                                        Color.fromRGB(
                                                particleBlueprint.getToRed(),
                                                particleBlueprint.getToGreen(),
                                                particleBlueprint.getToBlue()),
                                        1))).run();
            } else if (particleBlueprint.getParticle().equals(Particle.WITCH)
                //|| particleBlueprint.getParticle().equals(Particle.WITCH_AMBIENT) todo: 1.20.6 changed this name
            ) {
                PluginSchedulerBuilder.builder(MetadataHandler.PLUGIN)
                        .sync(location)
                        .task(() -> location.getWorld().spawnParticle(
                                particleBlueprint.getParticle(),
                                x2,
                                y2,
                                z2,
                                amount2,
                                particleBlueprint.getRed(),
                                particleBlueprint.getGreen(),
                                particleBlueprint.getBlue())).run();
            } else
                PluginSchedulerBuilder.builder(MetadataHandler.PLUGIN)
                        .sync(location)
                        .task(() -> location.getWorld().spawnParticle(
                                particleBlueprint.getParticle(),
                                location,
                                amount2,
                                x2,
                                y2,
                                z2,
                                particleBlueprint.getSpeed())).run();
        }
    }
}
