/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.part.gate;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.quetzi.bluepower.api.part.BPPartFace;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.references.Refs;

import org.lwjgl.opengl.GL11;

public abstract class GateBase extends BPPartFace {
    
    private static Vector3Cube HITBOX    = new Vector3Cube(0, 0, 0, 1, 1D / 8D, 1);
    private static Vector3Cube OCCLUSION = new Vector3Cube(1D / 8D, 0, 1D / 8D, 7D / 8D, 1D / 8D, 7D / 8D);
    
    public GateBase() {
    
        for (int i = 0; i < 4; i++)
            connections[i] = new RedstoneConnection(this, i + "", true, false);
        
        initializeConnections(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT));
    }
    
    public abstract void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right);
    
    @Override
    public void setFace(int face) {
    
        super.setFace(face);
    }
    
    @Override
    public String getType() {
    
        return getGateID();
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "gate." + getGateID();
    }
    
    public abstract String getGateID();
    
    @Override
    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(HITBOX.clone().toAABB());
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(OCCLUSION.clone().toAABB());
    }
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(HITBOX.clone().toAABB());
    }
    
    @Override
    public final void renderDynamic(Vector3 loc, int pass, float frame) {
    
        GL11.glPushMatrix();
        {
            super.renderDynamic(loc, pass, frame);
            
            /* Top */
            renderTop(frame);
            
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/bottom.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* Bottom */
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 0, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
            GL11.glEnd();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/side.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* East */
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0, 1, 0, 1);
            /* West */
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            /* North */
            GL11.glNormal3d(0, 0, -1);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0, 0, 0, 1);
            /* South */
            GL11.glNormal3d(0, 0, 1);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 0);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
    }
    
    public void renderTopTexture(String texture) {
    
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(texture));
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3d(0, 1, 0);
        RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 0, 0);
        RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 0, 1);
        RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
        RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
        GL11.glEnd();
    }
    
    @Override
    public final boolean renderStatic(Vector3 loc, int pass) {
    
        return super.renderStatic(loc, pass);
    }
    
    @Override
    public final void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    
        GL11.glPushMatrix();
        {
            
            /* Top */
            renderTopItem();
            
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/bottom.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* Bottom */
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 0, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
            GL11.glEnd();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/side.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* East */
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0, 1, 0, 1);
            /* West */
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            /* North */
            GL11.glNormal3d(0, 0, -1);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0, 0, 0, 1);
            /* South */
            GL11.glNormal3d(0, 0, 1);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 0);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
    }
    
    public abstract void renderTop(float frame);
    
    public abstract void renderTopItem();
    
    @Override
    public void update() {
    
        super.update();
        doLogic(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT));
    }
    
    public abstract void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right);
    
    @Override
    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {
    
        if (item != null && item.getItem() == BPItems.screwdriver) {
            if (player.isSneaking()) {
                if (!world.isRemote) {
                    if (changeMode(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT))) {
                        notifyUpdate();
                        sendUpdatePacket();
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                setRotation(getRotation() + 1);
            }
            return true;
        }
        return super.onActivated(player, mop, item);
    }
    
    protected boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        return false;
    }
    
}
