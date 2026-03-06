package net.tclproject.immersivecavegen.entities;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.monster.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;

public class RenderGlowSlime extends RenderSlime
{
    private static final ResourceLocation slimeTextures;
    private ModelBase scaleAmount;

    public RenderGlowSlime(final ModelBase p_i1267_1_, final ModelBase p_i1267_2_, final float p_i1267_3_) {
        super(p_i1267_1_, p_i1267_2_, p_i1267_3_);
        this.scaleAmount = p_i1267_2_;
    }

    protected ResourceLocation func_110775_a(final EntitySlime p_110775_1_) {
        return RenderGlowSlime.slimeTextures;
    }

    protected int func_77032_a(final EntitySlime p_77032_1_, final int p_77032_2_, final float p_77032_3_) {
        if (p_77032_1_.func_82150_aj()) {
            return 0;
        }
        if (p_77032_2_ == 0) {
            this.func_77042_a(this.scaleAmount);
            OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 100.0f, 100.0f);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            return 1;
        }
        if (p_77032_2_ == 1) {
            GL11.glDisable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        return -1;
    }

    static {
        slimeTextures = new ResourceLocation("immersivecavegen:textures/entity/glowslime.png");
    }
}
