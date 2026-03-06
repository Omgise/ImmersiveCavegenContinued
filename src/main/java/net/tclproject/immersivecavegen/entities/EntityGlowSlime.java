package net.tclproject.immersivecavegen.entities;

import net.minecraft.entity.monster.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.tclproject.immersivecavegen.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import cpw.mods.fml.relauncher.*;

public class EntityGlowSlime extends EntitySlime implements ICaveEntity
{
    public EntityGlowSlime(final World world) {
        super(world);
    }

    protected String func_70801_i() {
        return "water";
    }

    public boolean func_70601_bi() {
        return this.getCanSpawnHereOriginal() && this.field_70163_u < 60.0;
    }

    public boolean getCanSpawnHereOriginal() {
        return this.field_70170_p.func_72855_b(this.field_70121_D) && this.field_70170_p.func_72945_a((Entity)this, this.field_70121_D).isEmpty() && !this.field_70170_p.func_72953_d(this.field_70121_D);
    }

    public void func_70645_a(final DamageSource p_70645_1_) {
        super.func_70645_a(p_70645_1_);
        if (WGConfig.glowSlimesSpawnWater) {
            if (this.field_70170_p.func_147437_c((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v)) {
                this.field_70170_p.func_147449_b((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v, (Block)Blocks.field_150358_i);
            }
            else if (this.field_70170_p.func_147437_c((int)this.field_70165_t - 1, (int)this.field_70163_u, (int)this.field_70161_v)) {
                this.field_70170_p.func_147449_b((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v, (Block)Blocks.field_150358_i);
            }
            else if (this.field_70170_p.func_147437_c((int)this.field_70165_t + 1, (int)this.field_70163_u, (int)this.field_70161_v)) {
                this.field_70170_p.func_147449_b((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v, (Block)Blocks.field_150358_i);
            }
            else if (this.field_70170_p.func_147437_c((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v - 1)) {
                this.field_70170_p.func_147449_b((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v, (Block)Blocks.field_150358_i);
            }
            else if (this.field_70170_p.func_147437_c((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v + 1)) {
                this.field_70170_p.func_147449_b((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v, (Block)Blocks.field_150358_i);
            }
        }
    }

    public void func_70106_y() {
        final int i = this.func_70809_q();
        if (!this.field_70170_p.field_72995_K && i > 1 && this.func_110143_aJ() <= 0.0f) {
            for (int j = 2 + this.field_70146_Z.nextInt(3), k = 0; k < j; ++k) {
                final float f = (k % 2 - 0.5f) * i / 4.0f;
                final float f2 = (k / 2 - 0.5f) * i / 4.0f;
                final EntityGlowSlime entityslime = this.createInstance();
                entityslime.func_70799_a(i / 2);
                entityslime.func_70012_b(this.field_70165_t + f, this.field_70163_u + 0.5, this.field_70161_v + f2, this.field_70146_Z.nextFloat() * 360.0f, 0.0f);
                this.field_70170_p.func_72838_d((Entity)entityslime);
            }
        }
        super.func_70106_y();
    }

    protected EntityGlowSlime createInstance() {
        return new EntityGlowSlime(this.field_70170_p);
    }

    @SideOnly(Side.CLIENT)
    public int func_70070_b(final float p_70070_1_) {
        return 16;
    }

    protected int func_70806_k() {
        return this.field_70146_Z.nextInt(20);
    }

    public float func_70013_c(final float p_70013_1_) {
        return 16.0f;
    }
}
