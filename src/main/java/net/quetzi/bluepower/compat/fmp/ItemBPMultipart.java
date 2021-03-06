/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.compat.fmp;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TMultiPart;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.part.ItemBPPart;
import net.quetzi.bluepower.references.Refs;

import java.util.List;

public class ItemBPMultipart extends JItemMultiPart {

    public ItemBPMultipart() {

        super();
        setUnlocalizedName(Refs.MODID + ".part");
        setCreativeTab(CustomTabs.tabBluePowerCircuits);
    }

    private BPPart p;

    @Override
    public TMultiPart newPart(ItemStack is, EntityPlayer player, World w, BlockCoord b, int unused, Vector3 unused1) {

        MultipartBPPart part = (MultipartBPPart) RegisterMultiparts.createPart_(p);

        return part;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float f, float f2, float f3) {

        p = PartRegistry.createPart(PartRegistry.getPartIdFromItem(stack));

        if (!p.canPlacePart(stack, player, new net.quetzi.bluepower.api.vec.Vector3(x, y, z, w),
                new MovingObjectPosition(x, y, z, side, Vec3.createVectorHelper(x + f, y + f2, z + f3), true))) return false;

        if (super.onItemUse(stack, player, w, x, y, z, side, f, f2, f3)) {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundTypeStone.getBreakSound(), Block.soundTypeStone.getVolume(),
                    Block.soundTypeStone.getPitch());
            return true;
        }
        return false;
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return ItemBPPart.getUnlocalizedName_(item);
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void getSubItems(Item unused, CreativeTabs tab, List l) {

        ItemBPPart.getSubItems(l);
    }

}
