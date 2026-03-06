package net.tclproject.immersivecavegen.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.tclproject.immersivecavegen.misc.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

public class ScorchedLavaStone extends Block
{
    public ScorchedLavaStone() {
        super(Material.field_151576_e);
        this.func_149647_a(GamemodeTab.tabCaves).func_149711_c(1.5f).func_149752_b(10.0f).func_149672_a(ScorchedLavaStone.field_149780_i).func_149663_c("scorchedLavaStoneBlock").func_149658_d("immersivecavegen:scorched_lava_stone");
    }

    public Item func_149650_a(final int p_149650_1_, final Random p_149650_2_, final int p_149650_3_) {
        return Item.func_150898_a(Blocks.field_150347_e);
    }

    public void func_149749_a(final World p_149749_1_, final int x, final int y, final int z, final Block p_149749_5_, final int p_149749_6_) {
        super.func_149749_a(p_149749_1_, x, y, z, p_149749_5_, p_149749_6_);
        p_149749_1_.func_147465_d(x, y, z, (Block)Blocks.field_150356_k, 0, 3);
    }

    public AxisAlignedBB func_149668_a(final World p_149668_1_, final int p_149668_2_, final int p_149668_3_, final int p_149668_4_) {
        return null;
    }

    public void func_149670_a(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            this.func_149749_a(world, x, y, z, this, 0);
        }
        if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).field_71075_bZ.field_75098_d || !((EntityPlayer)entity).field_71075_bZ.field_75100_b) {
            entity.field_70159_w *= 0.7;
            entity.field_70179_y *= 0.7;
        }
    }
}
