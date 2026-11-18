package com.example.rainfog;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

/** Mod apenas de cliente: não é carregado em servidores dedicados. */
@Mod(value = RainFogMod.MOD_ID, dist = Dist.CLIENT)
public class RainFogMod {
    public static final String MOD_ID = "rainfog";

    public RainFogMod(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, RainFogConfig.SPEC);
    }
}
