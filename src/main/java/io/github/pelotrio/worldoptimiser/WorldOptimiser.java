package io.github.pelotrio.worldoptimiser;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ModMismatchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WorldOptimiser.MODID)
public class WorldOptimiser {

    public static final String MODID = "worldoptimiser";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean hasNewMods;


    public WorldOptimiser() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::onModMismatch);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onModMismatch(ModMismatchEvent event) {
        if (event.getResolved().findAny().isPresent() || event.getUnresolved().findAny().isPresent()) {
            hasNewMods = true;
            LOGGER.warn("Detected new mods world will be optimised on startup");
        }
    }
}
