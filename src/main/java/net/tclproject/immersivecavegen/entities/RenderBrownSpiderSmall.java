package net.tclproject.immersivecavegen.entities;

import net.minecraft.client.renderer.entity.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.*;

@SideOnly(Side.CLIENT)
public class RenderBrownSpiderSmall extends RenderSpider
{
    private static final ResourceLocation brownSpiderTextures;
    private boolean shadowSizeChanged;

    public RenderBrownSpiderSmall() {
        this.shadowSizeChanged = false;
    }

    protected void preRenderCallback(final EntityBrownSpiderSmall p_77041_1_, final float p_77041_2_) {
        GL11.glScalef(EntityBrownSpider.sizes[0][0] / 1.4f, EntityBrownSpider.sizes[1][0] / 0.9f, EntityBrownSpider.sizes[0][0] / 1.4f);
        if (!this.shadowSizeChanged) {
            this.field_76989_e *= EntityBrownSpider.sizes[0][0];
            this.shadowSizeChanged = true;
        }
    }

    protected ResourceLocation getEntityTexture(final EntityBrownSpiderSmall p_110775_1_) {
        return RenderBrownSpiderSmall.brownSpiderTextures;
    }

    protected ResourceLocation func_110775_a(final EntitySpider p_110775_1_) {
        return this.getEntityTexture((EntityBrownSpiderSmall)p_110775_1_);
    }

    protected void func_77041_b(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.preRenderCallback((EntityBrownSpiderSmall)p_77041_1_, p_77041_2_);
    }

    protected ResourceLocation func_110775_a(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityBrownSpiderSmall)p_110775_1_);
    }

    static {
        brownSpiderTextures = new ResourceLocation("immersivecavegen:textures/entity/brown_spider.png");
    }
}
