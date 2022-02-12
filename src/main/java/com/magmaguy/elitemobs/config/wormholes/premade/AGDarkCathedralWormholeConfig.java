package com.magmaguy.elitemobs.config.wormholes.premade;

import com.magmaguy.elitemobs.config.wormholes.WormholeConfigFields;
import wormhole.Wormhole;

public class AGDarkCathedralWormholeConfig extends WormholeConfigFields {
    public AGDarkCathedralWormholeConfig() {
        super("ag_dark_cathedral_wormhole",
                true,
                "em_adventurers_guild,290.5,101.5,293.5,-40,0",
                "dark_cathedral_lair.yml",
                Wormhole.WormholeStyle.CUBE);
        setBlindPlayer(true);
        setLocation1Text("&0『Dark Cathedral Lair』 &6Lvl 40");
        setParticleColor(0x000000);
    }
}
