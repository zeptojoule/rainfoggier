package com.example.rainfog;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class RainFogConfig {
    private static final ModConfigSpec.Builder B = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLED = B
            .comment("Liga/desliga o efeito de neblina na chuva.")
            .define("enabled", true);

    public static final ModConfigSpec.DoubleValue RAIN_MULTIPLIER = B
            .comment("Fração da distância de neblina normal durante chuva forte (0.6 = 60%).")
            .defineInRange("rainDistanceMultiplier", 0.6, 0.05, 1.0);

    public static final ModConfigSpec.DoubleValue THUNDER_MULTIPLIER = B
            .comment("Fração da distância de neblina normal durante tempestade (0.3 = 30%).",
                     "Se for maior que o valor da chuva, será limitado a ele.")
            .defineInRange("thunderDistanceMultiplier", 0.3, 0.05, 1.0);

    public static final ModConfigSpec.DoubleValue MIN_DISTANCE = B
            .comment("Distância mínima da neblina, em blocos (a neblina nunca fica mais perto que isso).")
            .defineInRange("minimumDistance", 24.0, 4.0, 512.0);

    public static final ModConfigSpec.DoubleValue START_RATIO = B
            .comment("Onde a neblina começa, como fração da distância final.",
                     "Perto de 1.0 = corte seco (como render distance menor); perto de 0.0 = névoa espessa e gradual.")
            .defineInRange("fogStartRatio", 0.5, 0.0, 0.95);

    public static final ModConfigSpec.BooleanValue REDUCE_UNDER_COVER = B
            .comment("Reduz o efeito suavemente quando você está debaixo de teto / em cavernas (pouca luz do céu).")
            .define("reduceUnderCover", true);

    public static final ModConfigSpec SPEC = B.build();

    private RainFogConfig() {}
}
