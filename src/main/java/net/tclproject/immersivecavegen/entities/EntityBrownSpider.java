package net.tclproject.immersivecavegen.entities;

import net.minecraft.entity.monster.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.potion.*;

public class EntityBrownSpider extends EntitySpider implements ICaveEntity
{
    public static float[][] sizes;

    public EntityBrownSpider(final World world) {
        super(world);
        this.func_70105_a(EntityBrownSpider.sizes[0][1], EntityBrownSpider.sizes[1][1]);
    }

    public boolean func_70601_bi() {
        return super.func_70601_bi() && this.field_70163_u < 60.0;
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(12.0 * EntityBrownSpider.sizes[0][1]);
    }

    public boolean func_70652_k(final Entity p_70652_1_) {
        if (super.func_70652_k(p_70652_1_)) {
            if (p_70652_1_ instanceof EntityLivingBase) {
                byte b0 = 0;
                if (this.field_70170_p.field_73013_u == EnumDifficulty.NORMAL) {
                    b0 = 7;
                }
                else if (this.field_70170_p.field_73013_u == EnumDifficulty.HARD) {
                    b0 = 15;
                }
                if (b0 > 0) {
                    ((EntityLivingBase)p_70652_1_).func_70690_d(new PotionEffect(Potion.field_76436_u.field_76415_H, b0 * 20, 0));
                    if (this.field_70146_Z.nextInt(21 - b0 - 2) == 0) {
                        ((EntityLivingBase)p_70652_1_).func_70690_d(new PotionEffect(Potion.field_76440_q.field_76415_H, b0 * 5, 0));
                        ((EntityLivingBase)p_70652_1_).func_70690_d(new PotionEffect(Potion.field_76431_k.field_76415_H, b0 * 10, 0));
                    }
                }
            }
            return true;
        }
        return false;
    }

    static {
        EntityBrownSpider.sizes = new float[][] { { 0.5f, 1.4f, 2.3f }, { 0.32f, 0.9f, 1.48f } };
    }
}
