package net.tclproject.immersivecavegen.items;

import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.tclproject.immersivecavegen.entities.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;

public class SpawnEggBrownSpiderLarge extends Item
{
    public boolean func_77648_a(final ItemStack p_77648_1_, final EntityPlayer p_77648_2_, final World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, final int p_77648_7_, final float p_77648_8_, final float p_77648_9_, final float p_77648_10_) {
        if (p_77648_3_.field_72995_K) {
            return true;
        }
        final Block block = p_77648_3_.func_147439_a(p_77648_4_, p_77648_5_, p_77648_6_);
        p_77648_4_ += Facing.field_71586_b[p_77648_7_];
        p_77648_5_ += Facing.field_71587_c[p_77648_7_];
        p_77648_6_ += Facing.field_71585_d[p_77648_7_];
        double d0 = 0.0;
        if (p_77648_7_ == 1 && block.func_149645_b() == 11) {
            d0 = 0.5;
        }
        final Entity entity = spawnCreature(p_77648_3_, p_77648_1_.func_77960_j(), p_77648_4_ + 0.5, p_77648_5_ + d0, p_77648_6_ + 0.5);
        if (entity != null) {
            if (entity instanceof EntityLivingBase && p_77648_1_.func_82837_s()) {
                ((EntityLiving)entity).func_94058_c(p_77648_1_.func_82833_r());
            }
            if (!p_77648_2_.field_71075_bZ.field_75098_d) {
                --p_77648_1_.field_77994_a;
            }
        }
        return true;
    }

    public ItemStack func_77659_a(final ItemStack p_77659_1_, final World p_77659_2_, final EntityPlayer p_77659_3_) {
        if (p_77659_2_.field_72995_K) {
            return p_77659_1_;
        }
        final MovingObjectPosition movingobjectposition = this.func_77621_a(p_77659_2_, p_77659_3_, true);
        if (movingobjectposition == null) {
            return p_77659_1_;
        }
        if (movingobjectposition.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
            final int i = movingobjectposition.field_72311_b;
            final int j = movingobjectposition.field_72312_c;
            final int k = movingobjectposition.field_72309_d;
            if (!p_77659_2_.func_72962_a(p_77659_3_, i, j, k)) {
                return p_77659_1_;
            }
            if (!p_77659_3_.func_82247_a(i, j, k, movingobjectposition.field_72310_e, p_77659_1_)) {
                return p_77659_1_;
            }
            if (p_77659_2_.func_147439_a(i, j, k) instanceof BlockLiquid) {
                final Entity entity = spawnCreature(p_77659_2_, p_77659_1_.func_77960_j(), i, j, k);
                if (entity != null) {
                    if (entity instanceof EntityLivingBase && p_77659_1_.func_82837_s()) {
                        ((EntityLiving)entity).func_94058_c(p_77659_1_.func_82833_r());
                    }
                    if (!p_77659_3_.field_71075_bZ.field_75098_d) {
                        --p_77659_1_.field_77994_a;
                    }
                }
            }
        }
        return p_77659_1_;
    }

    public static Entity spawnCreature(final World p_77840_0_, final int p_77840_1_, final double p_77840_2_, final double p_77840_4_, final double p_77840_6_) {
        final EntityBrownSpiderLarge entity = new EntityBrownSpiderLarge(p_77840_0_);
        entity.func_70012_b(p_77840_2_, p_77840_4_, p_77840_6_, MathHelper.func_76142_g(p_77840_0_.field_73012_v.nextFloat() * 360.0f), 0.0f);
        entity.field_70759_as = entity.field_70177_z;
        entity.field_70761_aq = entity.field_70177_z;
        entity.func_110161_a((IEntityLivingData)null);
        p_77840_0_.func_72838_d((Entity)entity);
        entity.func_70642_aH();
        return (Entity)entity;
    }
}
