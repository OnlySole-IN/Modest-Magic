package com.onlysole.modest_magic.integration.jei;

import com.onlysole.modest_magic.ModestMagic;
import com.onlysole.modest_magic.block.ModBlocks;
import com.onlysole.modest_magic.recipe.ModRecipes;
import com.onlysole.modest_magic.recipe.custom.TabletSmithingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ModestMagicJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID = new ResourceLocation(ModestMagic.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(
                new InfusingRecipeCategory(guiHelper),
                new EnchantingRecipeCategory(guiHelper),
                new SummoningRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var recipeManager = Minecraft.getInstance().level;
        if (recipeManager == null) return;

        var rm = recipeManager.getRecipeManager();

        registration.addRecipes(
                InfusingRecipeCategory.TYPE,
                rm.getAllRecipesFor(ModRecipes.INFUSING_TYPE.get())
        );

        registration.addRecipes(
                EnchantingRecipeCategory.TYPE,
                rm.getAllRecipesFor(ModRecipes.ENCHANTING_TYPE.get())
        );

        registration.addRecipes(
                SummoningRecipeCategory.TYPE,
                rm.getAllRecipesFor(ModRecipes.SUMMONING_TYPE.get())
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(
                InfusingRecipeCategory.TYPE,
                ModBlocks.ALTAR.get()
        );
        registration.addRecipeCatalysts(
                EnchantingRecipeCategory.TYPE,
                ModBlocks.ALTAR.get()
        );
        registration.addRecipeCatalysts(
                SummoningRecipeCategory.TYPE,
                ModBlocks.ALTAR.get()
        );
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getSmithingCategory().addExtension(
                TabletSmithingRecipe.class,
                new TabletSmithingExtension()
        );
    }
}