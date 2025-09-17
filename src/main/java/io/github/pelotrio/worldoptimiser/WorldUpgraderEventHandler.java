package io.github.pelotrio.worldoptimiser;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.worldupdate.WorldUpgrader;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = WorldOptimiser.MODID)
public class WorldUpgraderEventHandler {

    private static final Logger logger = LogManager.getLogger("worldoptimiser/worldupgrader");

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        LevelStorageSource.LevelStorageAccess session = server.storageSource;

        if (!WorldOptimiser.hasNewMods) {
            Map<String, String> previousMods = read(session);
            if (previousMods.isEmpty()) {
                return;
            }

            Map<String, String> currentMods = ModList.get().getMods().stream().collect(Collectors.toMap(IModInfo::getModId, m -> m.getVersion().toString()));

            // Check for any mismatch: additions, removals, or version changes
            boolean hasMismatch = !currentMods.equals(previousMods);
            if (!hasMismatch) {
                return;
            }
        }
        logger.info("World will be optimised it may take a while, please wait...");

        WorldUpgrader updater = new WorldUpgrader(session, server.getFixerUpper(), server.registries().compositeAccess().registryOrThrow(Registries.LEVEL_STEM), true);
        while (!updater.isFinished()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
        logger.info("World optimisation complete");
    }

    public static Map<String, String> read(LevelStorageSource.LevelStorageAccess session) {
        Path levelDat = session.getLevelPath(LevelResource.LEVEL_DATA_FILE); // points to level.dat

        try {
            CompoundTag root = NbtIo.readCompressed(levelDat.toFile());

            CompoundTag fml = root.getCompound("fml");
            if (!fml.contains("LoadingModList", CompoundTag.TAG_LIST)) return Map.of();

            ListTag list = fml.getList("LoadingModList", CompoundTag.TAG_COMPOUND);

            Map<String, String> mods = new LinkedHashMap<>();
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                String id = entry.getString("ModId");
                String ver = entry.getString("ModVersion");
                if (!id.isEmpty()) mods.put(id, ver);
            }
            return mods;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read LoadingModList from " + levelDat, e);
        }
    }
}
