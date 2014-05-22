package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.references.Refs;

public class BlockBasaltCobble extends Block {
    public BlockBasaltCobble() {
        super(Material.rock);
        this.textureName = Refs.MODID + ":" + Refs.BASALTCOBBLE_NAME;
        this.setCreativeTab(BluePower.creativeTab);
        this.setStepSound(soundTypeStone);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
    }
    
    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon(Refs.MODID + ":" + Refs.BASALTCOBBLE_NAME);
    }
}
