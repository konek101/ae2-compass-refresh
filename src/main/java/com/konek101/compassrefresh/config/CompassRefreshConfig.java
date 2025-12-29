package com.konek101.compassrefresh.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CompassRefreshConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue SCAN_RADIUS;
    public static final ForgeConfigSpec.IntValue COOLDOWN_TICKS;
    public static final ForgeConfigSpec.BooleanValue ONLY_LOADED_CHUNKS;
    public static final ForgeConfigSpec.BooleanValue SHOW_CHAT_FEEDBACK;

    static {
        BUILDER.push("compassrefresh");

        SCAN_RADIUS = BUILDER
            .comment("Chunk radius to scan around the player (0-3)")
            .defineInRange("scanRadius", 1, 0, 3);

        COOLDOWN_TICKS = BUILDER
            .comment("Cooldown between uses in ticks (0-1200, 20 ticks = 1 second)")
            .defineInRange("cooldownTicks", 40, 0, 1200);

        ONLY_LOADED_CHUNKS = BUILDER
            .comment("Only scan loaded chunks to prevent chunk loading")
            .define("onlyLoadedChunks", true);

        SHOW_CHAT_FEEDBACK = BUILDER
            .comment("Show chat feedback messages when rescanning")
            .define("showChatFeedback", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
