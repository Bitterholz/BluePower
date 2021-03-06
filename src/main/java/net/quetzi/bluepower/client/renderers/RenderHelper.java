package net.quetzi.bluepower.client.renderers;

import java.nio.DoubleBuffer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderHelper {
    
    public static void addVertex(double x, double y, double z) {
    
        GL11.glVertex3d(x, y, z);
    }
    
    public static void addVertexWithTexture(double x, double y, double z, double tx, double ty) {
    
        GL11.glTexCoord2d(tx, ty);
        GL11.glVertex3d(x, y, z);
    }
    
    private static RenderBlocks rb = new RenderBlocks();
    
    public static void renderRedstoneTorch(double x, double y, double z, double height, boolean state) {
    
        Block b = null;
        if (state) b = Blocks.redstone_torch;
        else b = Blocks.unlit_redstone_torch;
        
        GL11.glTranslated(x, y, z);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        
        GL11.glEnable(GL11.GL_CLIP_PLANE0);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE0, planeEquation(0, 0, 0, 0, 0, 1, 1, 0, 1));
        
        Tessellator t = Tessellator.instance;
        
        t.startDrawingQuads();
        t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        rb.renderTorchAtAngle(b, 0, y + height - 1, 0, 0, 0, 0);
        t.draw();
        
        GL11.glDisable(GL11.GL_CLIP_PLANE0);
        
        GL11.glTranslated(-x, -y, -z);
    }
    
    public static DoubleBuffer planeEquation(double x1, double y1, double z1,
            double x2, double y2, double z2, double x3, double y3, double z3) {
    
        double[] eq = new double[4];
        eq[0] = (y1 * (z2 - z3)) + (y2 * (z3 - z1)) + (y3 * (z1 - z2));
        eq[1] = (z1 * (x2 - x3)) + (z2 * (x3 - x1)) + (z3 * (x1 - x2));
        eq[2] = (x1 * (y2 - y3)) + (x2 * (y3 - y1)) + (x3 * (y1 - y2));
        eq[3] = -((x1 * ((y2 * z3) - (y3 * z2)))
                + (x2 * ((y3 * z1) - (y1 * z3))) + (x3 * ((y1 * z2) - (y2 * z1))));
        DoubleBuffer b = BufferUtils.createDoubleBuffer(8).put(eq);
        b.flip();
        return b;
    }
    
}
