package com.onlysole.modest_magic.integration.jei;

import com.onlysole.modest_magic.recipe.custom.TabletSmithingRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.*;

public class TabletSmithingExtension implements ISmithingCategoryExtension<TabletSmithingRecipe> {

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(TabletSmithingRecipe recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(recipe.getTemplate());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(TabletSmithingRecipe recipe, T ingredientAcceptor) {
        List<Enchantment> enchants = recipe.getEnchantments();

        List<ItemStack> validBases = new ArrayList<>();

        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack testStack = new ItemStack(item);
            boolean isValid = false;

            for (Enchantment e : enchants) {
                if (e.canEnchant(testStack) || testStack.is(Items.BOOK)) {
                    isValid = true;
                    break;
                }
            }

            if (isValid) {
                validBases.add(testStack);
            }
        }

        if (validBases.isEmpty()) {
            validBases.add(new ItemStack(Items.BOOK));
        }

        ingredientAcceptor.addItemStacks(validBases);
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(TabletSmithingRecipe recipe, T ingredientAcceptor) {
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setOutput(TabletSmithingRecipe recipe, T ingredientAcceptor) {
        List<Enchantment> enchants = recipe.getEnchantments();

        List<ItemStack> validBases = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack testStack = new ItemStack(item);
            boolean isValid = false;

            for (Enchantment e : enchants) {
                if (e.canEnchant(testStack) || testStack.is(Items.BOOK)) {
                    isValid = true;
                    break;
                }
            }

            if (isValid) {
                validBases.add(testStack);
            }
        }

        if (validBases.isEmpty()) {
            validBases.add(new ItemStack(Items.BOOK));
        }

        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack baseStack : validBases) {
            ItemStack outStack = baseStack.copy();
            if (outStack.getItem() == Items.BOOK) {
                outStack = new ItemStack(Items.ENCHANTED_BOOK);
            }

            Map<Enchantment, Integer> map = new LinkedHashMap<>();
            for (Enchantment e : enchants) {
                if (e.canEnchant(baseStack) || baseStack.is(Items.BOOK)) {
                    map.put(e, 1);
                }
            }

            if (!map.isEmpty()) {
                EnchantmentHelper.setEnchantments(map, outStack);
                outputs.add(outStack);
            }
        }

        ingredientAcceptor.addItemStacks(outputs);
    }

    @Override
    public void onDisplayedIngredientsUpdate(TabletSmithingRecipe recipe, IRecipeSlotDrawable templateSlot,
                                              IRecipeSlotDrawable baseSlot,
                                              IRecipeSlotDrawable additionSlot,
                                              IRecipeSlotDrawable outputSlot,
                                              IFocusGroup focuses) {
    }
}