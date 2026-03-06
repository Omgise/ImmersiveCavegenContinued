package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.relauncher.*;
import java.util.*;
import net.minecraft.block.material.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.texture.*;

public class BlockHugeGlowingMushroom extends BlockHugeMushroom
{
    private static final String[] field_149793_a;
    private final int field_149792_b;
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149794_M;
    @SideOnly(Side.CLIENT)
    private IIcon field_149795_N;
    @SideOnly(Side.CLIENT)
    private IIcon field_149796_O;
    Random rand;

    public BlockHugeGlowingMushroom(final Material p_i45412_1_, final int p_i45412_2_) {
        super(p_i45412_1_, p_i45412_2_);
        this.rand = new Random();
        this.field_149792_b = p_i45412_2_;
        this.func_149711_c(0.2f).func_149672_a(BlockHugeGlowingMushroom.field_149766_f).func_149713_g(3).func_149663_c("mushroom").func_149658_d("immersivecavegen:mushroom_block").func_149715_a(0.4f);
    }

    public boolean func_149662_c() {
        return false;
    }

    public int func_149643_k(final World world, final int x, final int y, final int z) {
        return world.func_72805_g(x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public Item func_149694_d(final World p_149694_1_, final int p_149694_2_, final int p_149694_3_, final int p_149694_4_) {
        return Item.func_150899_d(Block.func_149682_b(BlockInit.mushroomBlockBlue) + this.field_149792_b);
    }

    public boolean func_149686_d() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static int func_149997_b(final int p_149997_0_) {
        return ~p_149997_0_ & 0xF;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149646_a(final IBlockAccess p_149646_1_, final int p_149646_2_, final int p_149646_3_, final int p_149646_4_, final int p_149646_5_) {
        final Block block = p_149646_1_.func_147439_a(p_149646_2_, p_149646_3_, p_149646_4_);
        return block != p_149646_1_.func_147439_a(p_149646_2_ - Facing.field_71586_b[p_149646_5_], p_149646_3_ - Facing.field_71587_c[p_149646_5_], p_149646_4_ - Facing.field_71585_d[p_149646_5_]) || (block != this && block != this && super.func_149646_a(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_));
    }

    public Item func_149650_a(final int p_149650_1_, final Random p_149650_2_, final int p_149650_3_) {
        return Item.func_150898_a(BlockInit.cavePlantBlock);
    }

    public int func_149692_a(final int metadata) {
        return 4 + this.rand.nextInt(2);
    }

    @SideOnly(Side.CLIENT)
    public int func_149701_w() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(final int p_149691_1_, final int p_149691_2_) {
        return (p_149691_2_ == 10 && p_149691_1_ > 1) ? this.field_149795_N : ((p_149691_2_ >= 1 && p_149691_2_ <= 9 && p_149691_1_ == 1) ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ >= 1 && p_149691_2_ <= 3 && p_149691_1_ == 2) ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ >= 7 && p_149691_2_ <= 9 && p_149691_1_ == 3) ? this.field_149794_M[this.field_149792_b] : (((p_149691_2_ == 1 || p_149691_2_ == 4 || p_149691_2_ == 7) && p_149691_1_ == 4) ? this.field_149794_M[this.field_149792_b] : (((p_149691_2_ == 3 || p_149691_2_ == 6 || p_149691_2_ == 9) && p_149691_1_ == 5) ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ == 14) ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ == 15) ? this.field_149795_N : this.field_149796_O)))))));
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(final IIconRegister p_149651_1_) {
        this.field_149794_M = new IIcon[BlockHugeGlowingMushroom.field_149793_a.length];
        for (int i = 0; i < this.field_149794_M.length; ++i) {
            this.field_149794_M[i] = p_149651_1_.func_94245_a(this.func_149641_N() + "_" + BlockHugeGlowingMushroom.field_149793_a[i]);
        }
        this.field_149796_O = p_149651_1_.func_94245_a(this.func_149641_N() + "_inside");
        this.field_149795_N = p_149651_1_.func_94245_a(this.func_149641_N() + "_skin_stem");
    }

    static {
        field_149793_a = new String[] { "skin_blue2", "skin_green2" };
    }
}
