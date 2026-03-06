package net.tclproject.immersivecavegen.entities;

import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.potion.*;

public class EntityBrownSpiderLarge extends EntityBrownSpider
{
    public EntityBrownSpiderLarge(final World world) {
        super(world);
        this.func_70105_a(EntityBrownSpiderLarge.sizes[0][2], EntityBrownSpiderLarge.sizes[1][2]);
    }

    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(12.0 * EntityBrownSpiderLarge.sizes[0][2]);
        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(5.0);
    }

    @Override
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
                    if (this.field_70146_Z.nextInt(21 - b0 - 5) == 0) {
                        ((EntityLivingBase)p_70652_1_).func_70690_d(new PotionEffect(Potion.field_76440_q.field_76415_H, b0 * 5, 0));
                        ((EntityLivingBase)p_70652_1_).func_70690_d(new PotionEffect(Potion.field_76431_k.field_76415_H, b0 * 10, 0));
                    }
                }
            }
            return true;
        }
        return false;
    }
}
