package com.onlysole.modest_magic.integration.jei;

import com.onlysole.modest_magic.ModestMagic;
import com.onlysole.modest_magic.block.ModBlocks;
import com.onlysole.modest_magic.recipe.custom.SummoningRecipe;
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
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class SummoningRecipeCategory extends AbstractRecipeCategory<SummoningRecipe> {

    public static final RecipeType<SummoningRecipe> TYPE =
            RecipeType.create(ModestMagic.MOD_ID, "summoning", SummoningRecipe.class);

    private static final ResourceLocation BACKGROUND =
            new ResourceLocation(ModestMagic.MOD_ID, "textures/gui/emi_background.png");
    private static final ResourceLocation LETTERS =
            new ResourceLocation(ModestMagic.MOD_ID, "textures/gui/enchanted_letters.png");

    public SummoningRecipeCategory(IGuiHelper guiHelper) {
        super(
                TYPE,
                Component.translatable("emi.category.modest_magic.summoning"),
                guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.ALTAR.get())),
                140,
                80
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SummoningRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();

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

        builder.addInputSlot(getWidth() - 18, getHeight() - 18)
                .addItemStack(new ItemStack(ModBlocks.PEDESTAL.get(), originalSize));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, SummoningRecipe recipe, IFocusGroup focuses) {
        builder.addRecipeArrowWidget().setPosition(75, 32);
    }

    @Override
    public void draw(SummoningRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        guiGraphics.blit(BACKGROUND, 0, 0, 0, 0, getWidth(), getHeight(), 256, 256);

        int cx = 35;
        int cy = getHeight() / 2;
        int radius = 24;

        RotatingLettersRenderer.render(guiGraphics, LETTERS, cx, cy, radius + 6);

        int slotX = cx + radius + 51;
        int slotY = cy - 24;

        Font font = Minecraft.getInstance().font;
        Component pedestalText = Component.translatable("jei.category.modest_magic.pedestals", recipe.getIngredients().size());
        int textWidth = font.width(pedestalText);
        guiGraphics.drawString(font, pedestalText, slotX + 9 - textWidth / 2, slotY + 20, 0x808080, false);

        List<Entity> cachedEntities = getCachedEntities(recipe);
        if (!cachedEntities.isEmpty()) {
            int index = (int) ((System.currentTimeMillis() / 1500L) % cachedEntities.size());
            Entity currentEntity = cachedEntities.get(index);

            if (currentEntity instanceof LivingEntity living) {
                double width = living.getBbWidth();
                double height = living.getBbHeight();

                float maxDim = (float) Math.max(width, height);
                float scale = 24.0f / Math.max(maxDim, 0.5f);

                int entityX = slotX + 9;
                int entityY = (int) (slotY + 18 + (height * scale) / 2.0f);

                InventoryScreen.renderEntityInInventoryFollowsMouse(
                        guiGraphics,
                        entityX, entityY, (int) scale,
                        entityX - (int) mouseX, entityY - (int) mouseY - (scale * 1.5f),
                        living
                );
            }
        }
    }

    @Override
    public void onDisplayedIngredientsUpdate(SummoningRecipe recipe, List<IRecipeSlotDrawable> recipeSlots,
                                              IFocusGroup focuses) {
    }

    private static List<Entity> getCachedEntities(SummoningRecipe recipe) {
        List<Entity> entities = new ArrayList<>();
        if (Minecraft.getInstance().level != null) {
            for (SummoningRecipe.SummonOutcome outcome : recipe.getOutcomes()) {
                Entity entity = outcome.entity().create(Minecraft.getInstance().level);
                if (entity != null && !outcome.nbt().isEmpty()) {
                    entity.load(outcome.nbt());
                }
                if (entity != null) {
                    entities.add(entity);
                }
            }
        }
        return entities;
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