package com.onlysole.modest_magic.integration.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public final class WheelTooltipHelper {

    private WheelTooltipHelper() {
    }

    public static List<Component> buildTooltip(List<Ingredient> consolidated) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal("§6Required Items:"));

        for (Ingredient ing : consolidated) {
            ItemStack[] stacks = ing.getItems();
            if (stacks.length == 0) continue;

            ItemStack first = stacks[0];
            int count = first.getCount();

            Component name = first.getHoverName();
            Component line = Component.literal("§7- " + count + "x ").append(name);
            tooltip.add(line);
        }

        return tooltip;
    }
}