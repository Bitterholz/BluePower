/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemStackUtils {

    public static boolean isItemFuzzyEqual(ItemStack stack1, ItemStack stack2) {

        if (stack1.getItem() != stack2.getItem() && !isSameOreDictStack(stack1, stack2)) return false;
        return stack1.getItemDamage() == stack2.getItemDamage() || stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE
                || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }

    public static boolean isSameOreDictStack(ItemStack stack1, ItemStack stack2) {

        List<ItemStack> oreDictStacks = OreDictionary.getOres(OreDictionary.getOreID(stack1));
        for (ItemStack oreDictStack : oreDictStacks) {
            if (OreDictionary.itemMatches(oreDictStack, stack2, false)) {
                return true;
            }
        }
        return false;
    }
}
