package net.tclproject.immersivecavegen.items;

import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.potion.*;
import net.minecraft.init.*;

public class GlowSoup extends ItemSoup
{
    public GlowSoup(final int p_i45330_1_) {
        super(p_i45330_1_);
    }

    public ItemStack func_77654_b(final ItemStack p_77654_1_, final World p_77654_2_, final EntityPlayer p_77654_3_) {
        p_77654_3_.func_70690_d(new PotionEffect(Potion.field_76428_l.field_76415_H, 150, 1));
        super.func_77654_b(p_77654_1_, p_77654_2_, p_77654_3_);
        return new ItemStack(Items.field_151054_z);
    }
}
