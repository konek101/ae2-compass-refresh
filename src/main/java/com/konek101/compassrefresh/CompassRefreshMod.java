package com.konek101.compassrefresh;

import com.konek101.compassrefresh.config.CompassRefreshConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("compassrefresh")
public class CompassRefreshMod {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompassRefreshMod.class);

    public CompassRefreshMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        
        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CompassRefreshConfig.SPEC);
        
        MinecraftForge.EVENT_BUS.register(this);
        
        LOGGER.info("AE2 Compass Refresh mod initialized");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("AE2 Compass Refresh common setup");
    }
}
