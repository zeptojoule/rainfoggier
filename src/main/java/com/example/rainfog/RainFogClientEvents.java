package com.example.rainfog;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = RainFogMod.MOD_ID, value = Dist.CLIENT)
public final class RainFogClientEvents {
    /** 0 = totalmente coberto (sem efeito), 1 = céu aberto. Suavizado ao longo do tempo. */
    private static float exposure = 1.0F;
    private static long lastMillis = -1L;

    private RainFogClientEvents() {}

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (!RainFogConfig.ENABLED.get()) return;
        // Só neblina do terreno, e não debaixo d'água / lava / neve em pó
        if (event.getMode() != FogRenderer.FogMode.FOG_TERRAIN) return;
        if (event.getType() != FogType.NONE) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        float partial = (float) event.getPartialTick();

        // --- exposição ao céu (suavizada) ---
        float target = 1.0F;
        if (RainFogConfig.REDUCE_UNDER_COVER.get()) {
            int sky = level.getBrightness(LightLayer.SKY, event.getCamera().getBlockPosition());
            target = Mth.clamp((sky - 3) / 12.0F, 0.0F, 1.0F);
        }
        long now = Util.getMillis();
        float dt = lastMillis < 0 ? 1.0F : Math.min((now - lastMillis) / 1000.0F, 0.25F);
        lastMillis = now;
        exposure += (target - exposure) * Math.min(1.0F, dt * 1.5F);

        // getRainLevel é global (não depende do bioma), então vale em desertos, savanas etc.
        float r = level.getRainLevel(partial) * exposure;
        if (r <= 0.001F) return;
        // getThunderLevel já é proporcional à chuva
        float t = level.getThunderLevel(partial) * exposure;

        float rainMul = RainFogConfig.RAIN_MULTIPLIER.get().floatValue();
        float thunderMul = Math.min(RainFogConfig.THUNDER_MULTIPLIER.get().floatValue(), rainMul);
        float factor = Mth.lerp(t, Mth.lerp(r, 1.0F, rainMul), thunderMul);

        float vanillaFar = event.getFarPlaneDistance();
        float vanillaNear = event.getNearPlaneDistance();

        float minFar = Math.min(vanillaFar, RainFogConfig.MIN_DISTANCE.get().floatValue());
        float far = Math.max(vanillaFar * factor, minFar);

        float nearTarget = far * RainFogConfig.START_RATIO.get().floatValue();
        float near = Mth.lerp(r, vanillaNear * factor, nearTarget);
        near = Mth.clamp(near, 0.0F, far - 1.0F);

        event.setFarPlaneDistance(far);
        event.setNearPlaneDistance(near);
        // Sem cancelar, o NeoForge ignora as alterações
        event.setCanceled(true);
    }
}
