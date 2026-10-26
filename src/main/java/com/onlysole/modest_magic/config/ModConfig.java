package com.onlysole.modest_magic.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onlysole.modest_magic.ModestMagic;
import com.onlysole.modest_magic.platform.Services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = Services.PLATFORM.getConfigDirectory().resolve("modest_magic.json").toFile();
    private static ModConfig INSTANCE;

    public boolean throwItemsOnPedestals = true;
    public boolean reducedEmiMotion = false;

    public static ModConfig get() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ModConfig.class);
            } catch (Exception e) {
                ModestMagic.LOGGER.error("Failed to load modestmagic.json", e);
                INSTANCE = new ModConfig();
                save();
            }
        } else {
            INSTANCE = new ModConfig();
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            ModestMagic.LOGGER.error("Failed to save modestmagic.json", e);
        }
    }
}