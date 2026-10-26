package com.onlysole.modest_magic.recipe;

import com.onlysole.modest_magic.ModestMagic;
import com.onlysole.modest_magic.platform.Services;
import com.onlysole.modest_magic.platform.services.IRegistryHelper;
import com.onlysole.modest_magic.recipe.custom.EnchantingRecipe;
import com.onlysole.modest_magic.recipe.custom.InfusingRecipe;
import com.onlysole.modest_magic.recipe.custom.SummoningRecipe;
import com.onlysole.modest_magic.recipe.custom.TabletSmithingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class ModRecipes {

    public static final IRegistryHelper.IRegistryProvider<RecipeType<?>> TYPES =
            Services.REGISTRIES.create(Registries.RECIPE_TYPE, ModestMagic.MOD_ID);

    public static final IRegistryHelper.IRegistryProvider<RecipeSerializer<?>> SERIALIZERS =
            Services.REGISTRIES.create(Registries.RECIPE_SERIALIZER, ModestMagic.MOD_ID);

    public static final Supplier<RecipeType<InfusingRecipe>> INFUSING_TYPE =
            TYPES.register("infusing", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "infusing";
                }
            });

    public static final Supplier<RecipeSerializer<InfusingRecipe>> INFUSING_SERIALIZER =
            SERIALIZERS.register("infusing", () -> InfusingRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<EnchantingRecipe>> ENCHANTING_TYPE =
            TYPES.register("enchanting", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "enchanting";
                }
            });
    public static final Supplier<RecipeSerializer<EnchantingRecipe>> ENCHANTING_SERIALIZER =
            SERIALIZERS.register("enchanting", EnchantingRecipe.Serializer::new);

    public static final Supplier<RecipeType<SummoningRecipe>> SUMMONING_TYPE =
            TYPES.register("summoning", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "summoning";
                }
            });

    public static final Supplier<RecipeSerializer<SummoningRecipe>> SUMMONING_SERIALIZER =
            SERIALIZERS.register("summoning", () -> SummoningRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<TabletSmithingRecipe>> TABLET_SMITHING_SERIALIZER =
            SERIALIZERS.register("tablet_smithing", () -> TabletSmithingRecipe.Serializer.INSTANCE);

    public static void init() {
    }
}