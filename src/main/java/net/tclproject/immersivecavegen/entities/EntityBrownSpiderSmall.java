package net.tclproject.immersivecavegen.entities;

import net.minecraft.entity.monster.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;

public class EntityBrownSpiderSmall extends EntitySpider implements ICaveEntity
{
    public static float[][] sizes;

    public EntityBrownSpiderSmall(final World world) {
        super(world);
        this.func_70105_a(EntityBrownSpiderSmall.sizes[0][0], EntityBrownSpiderSmall.sizes[1][0]);
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(12.0 * EntityBrownSpiderSmall.sizes[0][0]);
        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(1.0);
    }

    public boolean func_70601_bi() {
        return super.func_70601_bi() && this.field_70163_u < 60.0;
    }

    public boolean func_70652_k(final Entity p_70652_1_) {
        return super.func_70652_k(p_70652_1_);
    }

    static {
        EntityBrownSpiderSmall.sizes = new float[][] { { 0.5f, 1.4f, 2.3f }, { 0.32f, 0.9f, 1.48f } };
    }
}
