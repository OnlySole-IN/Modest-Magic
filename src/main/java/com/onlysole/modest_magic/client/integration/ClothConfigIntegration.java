package com.onlysole.modest_magic.client.integration;

import com.onlysole.modest_magic.config.ModConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigIntegration {

    public static Screen createScreen(Screen parent) {
        ModConfig config = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.modest_magic.title"));

        builder.setSavingRunnable(ModConfig::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.modest_magic.category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.modest_magic.option.throwItemsOnPedestals"), config.throwItemsOnPedestals)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.throwItemsOnPedestals = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.modest_magic.option.reducedEmiMotion"), config.reducedEmiMotion)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.reducedEmiMotion = newValue)
                .build());

        return builder.build();
    }
}