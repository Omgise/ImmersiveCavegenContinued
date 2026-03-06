package net.tclproject.immersivecavegen.blocks;

import net.tclproject.immersivecavegen.misc.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.block.material.*;

public class BlockGlowlily extends BlockLilyPad
{
    public BlockGlowlily() {
        final float f = 0.5f;
        final float f2 = 0.015625f;
        this.func_149676_a(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f2, 0.5f + f);
        this.func_149647_a(GamemodeTab.tabCaves).func_149711_c(0.0f).func_149715_a(0.3f).func_149672_a(BlockGlowlily.field_149779_h).func_149663_c("glowLily");
    }

    @SideOnly(Side.CLIENT)
    public int func_149720_d(final IBlockAccess p_149720_1_, final int p_149720_2_, final int p_149720_3_, final int p_149720_4_) {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public int func_149635_D() {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public int func_149741_i(final int p_149741_1_) {
        return 16777215;
    }

    public int func_149645_b() {
        return 23;
    }

    public void func_149743_a(final World p_149743_1_, final int p_149743_2_, final int p_149743_3_, final int p_149743_4_, final AxisAlignedBB p_149743_5_, final List p_149743_6_, final Entity p_149743_7_) {
        if (p_149743_7_ == null || !(p_149743_7_ instanceof EntityBoat)) {
            super.func_149743_a(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
        }
    }

    public AxisAlignedBB func_149668_a(final World p_149668_1_, final int p_149668_2_, final int p_149668_3_, final int p_149668_4_) {
        return AxisAlignedBB.func_72330_a(p_149668_2_ + this.field_149759_B, p_149668_3_ + this.field_149760_C, p_149668_4_ + this.field_149754_D, p_149668_2_ + this.field_149755_E, p_149668_3_ + this.field_149756_F, p_149668_4_ + this.field_149757_G);
    }

    public boolean func_149854_a(final Block p_149854_1_) {
        return p_149854_1_ == Blocks.field_150355_j;
    }

    public boolean func_149718_j(final World p_149718_1_, final int p_149718_2_, final int p_149718_3_, final int p_149718_4_) {
        if (p_149718_1_.func_147439_a(p_149718_2_, p_149718_3_ + 1, p_149718_4_) == this || p_149718_1_.func_147439_a(p_149718_2_, p_149718_3_ + 2, p_149718_4_) == this || p_149718_1_.func_147439_a(p_149718_2_, p_149718_3_, p_149718_4_) == this) {
            return true;
        }
        if (p_149718_3_ >= 0 && p_149718_3_ < 256 && p_149718_1_.func_147439_a(p_149718_2_, p_149718_3_, p_149718_4_).func_149688_o() == Material.field_151586_h && p_149718_1_.func_72805_g(p_149718_2_, p_149718_3_, p_149718_4_) == 0) {
            if (p_149718_1_.func_147439_a(p_149718_2_, p_149718_3_ + 1, p_149718_4_).func_149688_o() == Material.field_151586_h && p_149718_1_.func_72805_g(p_149718_2_, p_149718_3_ + 1, p_149718_4_) == 0) {
                p_149718_1_.func_147449_b(p_149718_2_, p_149718_3_ + 2, p_149718_4_, (Block)this);
            }
            else {
                p_149718_1_.func_147449_b(p_149718_2_, p_149718_3_ + 1, p_149718_4_, (Block)this);
            }
        }
        return false;
    }
}
