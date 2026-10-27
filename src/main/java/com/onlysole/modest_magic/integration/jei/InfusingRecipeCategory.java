package com.onlysole.modest_magic.integration.jei;

import com.onlysole.modest_magic.ModestMagic;
import com.onlysole.modest_magic.block.ModBlocks;
import com.onlysole.modest_magic.recipe.custom.InfusingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class InfusingRecipeCategory extends AbstractRecipeCategory<InfusingRecipe> {

    public static final RecipeType<InfusingRecipe> TYPE =
            RecipeType.create(ModestMagic.MOD_ID, "infusing", InfusingRecipe.class);

    private static final ResourceLocation BACKGROUND =
            new ResourceLocation(ModestMagic.MOD_ID, "textures/gui/emi_background.png");
    private static final ResourceLocation LETTERS =
            new ResourceLocation(ModestMagic.MOD_ID, "textures/gui/enchanted_letters.png");

    public InfusingRecipeCategory(IGuiHelper guiHelper) {
        super(
                TYPE,
                Component.translatable("emi.category.modest_magic.infusing"),
                guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.ALTAR.get())),
                140,
                80
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InfusingRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();
        List<ItemStack> results = recipe.getResults();

        int cx = 35;
        int cy = getHeight() / 2;
        int radius = 24;

        builder.addInputSlot(cx - 9, cy - 9)
                .addIngredients(recipe.getBase());

        int originalSize = ingredients.size();
        int n = Math.min(originalSize, 6);
        List<Ingredient> displayIngredients = ingredients;
        if (originalSize > 6) {
            displayIngredients = consolidateIngredients(ingredients);
            n = displayIngredients.size();
        }

        List<Component> consolidatedTooltip = null;
        if (originalSize > 6) {
            consolidatedTooltip = WheelTooltipHelper.buildTooltip(displayIngredients);
        }

        for (int i = 0; i < n; i++) {
            double angle = (360.0 / n) * i - 90.0;
            int x = (int) (cx + Math.cos(Math.toRadians(angle)) * radius) - 9;
            int y = (int) (cy + Math.sin(Math.toRadians(angle)) * radius) - 9;

            IRecipeSlotBuilder slot = builder.addInputSlot(x, y);
            if (i < displayIngredients.size()) {
                slot.addIngredients(displayIngredients.get(i));
            }
            if (consolidatedTooltip != null) {
                List<Component> tip = consolidatedTooltip;
                slot.addTooltipCallback((slotView, tooltip) -> {
                    tooltip.clear();
                    tooltip.addAll(tip);
                });
            }
        }

        if (!results.isEmpty()) {
            builder.addOutputSlot(cx + radius + 51, cy - 9)
                    .addItemStacks(results);
        }

        builder.addInputSlot(getWidth() - 18, getHeight() - 18)
                .addItemStack(new ItemStack(ModBlocks.PEDESTAL.get(), originalSize));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, InfusingRecipe recipe, IFocusGroup focuses) {
        builder.addRecipeArrowWidget().setPosition(75, 32);
    }

    @Override
    public void draw(InfusingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        guiGraphics.blit(BACKGROUND, 0, 0, 0, 0, getWidth(), getHeight(), 256, 256);

        int cx = 35;
        int cy = getHeight() / 2;
        int radius = 24;

        RotatingLettersRenderer.render(guiGraphics, LETTERS, cx, cy, radius + 6);

        int outputSlotX = cx + radius + 51;
        int outputSlotY = cy - 9;
        Font font = Minecraft.getInstance().font;
        Component pedestalText = Component.translatable("jei.category.modest_magic.pedestals", recipe.getIngredients().size());
        int textWidth = font.width(pedestalText);
        guiGraphics.drawString(font, pedestalText, outputSlotX + 9 - textWidth / 2, outputSlotY + 20, 0x808080, false);
    }

    @Override
    public void onDisplayedIngredientsUpdate(InfusingRecipe recipe, List<IRecipeSlotDrawable> recipeSlots,
                                              IFocusGroup focuses) {
    }

    private static List<Ingredient> consolidateIngredients(List<Ingredient> ingredients) {
        List<Ingredient> unique = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (Ingredient ing : ingredients) {
            boolean found = false;
            for (int i = 0; i < unique.size(); i++) {
                if (ingredientsEqual(unique.get(i), ing)) {
                    counts.set(i, counts.get(i) + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                unique.add(ing);
                counts.add(1);
            }
        }

        List<Ingredient> result = new ArrayList<>();
        for (int i = 0; i < unique.size(); i++) {
            ItemStack[] originalItems = unique.get(i).getItems();
            ItemStack[] countedItems = new ItemStack[originalItems.length];
            for (int j = 0; j < originalItems.length; j++) {
                countedItems[j] = originalItems[j].copy();
                countedItems[j].setCount(counts.get(i));
            }
            result.add(Ingredient.of(countedItems));
        }
        return result;
    }

    private static boolean ingredientsEqual(Ingredient a, Ingredient b) {
        ItemStack[] stacksA = a.getItems();
        ItemStack[] stacksB = b.getItems();
        if (stacksA.length == 0 || stacksB.length == 0) return false;
        return ItemStack.isSameItem(stacksA[0], stacksB[0]);
    }
}