package com.onlysole.modest_magic.block;

import com.onlysole.modest_magic.ModestMagic;
import com.onlysole.modest_magic.block.custom.AltarBlock;
import com.onlysole.modest_magic.block.custom.PedestalBlock;
import com.onlysole.modest_magic.item.ModItems;
import com.onlysole.modest_magic.platform.Services;
import com.onlysole.modest_magic.platform.services.IRegistryHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {
    public static final IRegistryHelper.IRegistryProvider<Block> BLOCKS =
            Services.REGISTRIES.create(Registries.BLOCK, ModestMagic.MOD_ID);

    public static final Map<Supplier<Item>, Integer> FUEL_ITEMS = new HashMap<>();

    public static final Supplier<Block> ALTAR = registerBlock("altar",
            () -> new AltarBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noOcclusion()), false, 0);

    public static final Supplier<Block> PEDESTAL = registerBlock("pedestal",
            () -> new PedestalBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).noOcclusion()), false, 0);

    private static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block, boolean isFuel, int fuelAmount) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, isFuel, fuelAmount);
        return toReturn;
    }

    private static <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block, boolean isFuel, int fuelAmount) {
        Supplier<Item> item = ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

        if (isFuel) {
            FUEL_ITEMS.put(item, fuelAmount);
        }
        return item;
    }

    public static void init() {
    }
}