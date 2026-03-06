package net.tclproject.immersivecavegen.blocks;

import net.minecraft.util.*;
import net.tclproject.immersivecavegen.misc.*;
import net.minecraft.block.material.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import java.util.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.common.*;

public class BlockCavePlant extends BlockBush implements IGrowable
{
    private IIcon[] icons;

    public BlockCavePlant() {
        super(Material.field_151585_k);
        this.icons = new IIcon[8];
        this.func_149647_a(GamemodeTab.tabCaves);
        this.func_149713_g(0);
        this.func_149672_a(BlockCavePlant.field_149779_h);
        this.func_149752_b(0.6f);
        this.func_149663_c("caveplant");
    }

    public boolean func_149718_j(final World world, final int x, final int y, final int z) {
        if (world.func_147445_c(x, y - 1, z, false)) {
            return true;
        }
        final Block bellowId = world.func_147439_a(x, y - 1, z);
        return bellowId.func_149688_o().func_151565_r() == MapColor.field_151657_g || (bellowId == this && world.func_72805_g(x, y - 1, z) == 4);
    }

    public boolean canBeReplacedByLeaves(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    public int func_149692_a(int meta) {
        if (meta > 7) {
            meta = 0;
        }
        return meta;
    }

    public int func_149643_k(final World world, final int x, final int y, final int z) {
        return world.func_72805_g(x, y, z);
    }

    public boolean func_149700_E() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(final int side, int metadata) {
        if (metadata > 7) {
            metadata = 0;
        }
        return this.icons[metadata];
    }

    @SideOnly(Side.CLIENT)
    public void func_149666_a(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int i = 0; i < this.icons.length; ++i) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    public void func_149855_e(final World world, final int x, final int y, final int z) {
        if (!this.func_149718_j(world, x, y, z)) {
            world.func_147468_f(x, y, z);
        }
    }

    public void func_149695_a(final World world, final int x, final int y, final int z, final Block blockID) {
        this.func_149855_e(world, x, y, z);
    }

    public int func_149745_a(final Random rand) {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(final IIconRegister iconRegister) {
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = iconRegister.func_94245_a("immersivecavegen:caveplant" + i);
        }
    }

    public void func_149719_a(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
        final int metadata = par1IBlockAccess.func_72805_g(par2, par3, par4);
        switch (metadata) {
            case 1: {
                this.func_149676_a(0.25f, 0.0f, 0.25f, 0.6f, 0.75f, 0.75f);
                break;
            }
            case 2:
            case 4: {
                this.func_149676_a(0.25f, 0.0f, 0.25f, 0.75f, 0.4f, 0.75f);
                break;
            }
            default: {
                this.func_149676_a(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);
                break;
            }
        }
    }

    public static void growMushroom(final World p_76484_1_, final Random p_76484_2_, final int p_76484_3_, final int p_76484_4_, final int p_76484_5_, final int l) {
        if (p_76484_1_.func_147437_c(p_76484_3_, p_76484_4_ - 1, p_76484_5_)) {
            return;
        }
        final int i1 = p_76484_2_.nextInt(3) + 4;
        boolean flag = true;
        if (p_76484_4_ < 1 || p_76484_4_ + i1 + 1 >= 256) {
            return;
        }
        for (int j1 = p_76484_4_; j1 <= p_76484_4_ + 1 + i1; ++j1) {
            byte b0 = 3;
            if (j1 <= p_76484_4_ + 3) {
                b0 = 0;
            }
            for (int k1 = p_76484_3_ - b0; k1 <= p_76484_3_ + b0 && flag; ++k1) {
                for (int l2 = p_76484_5_ - b0; l2 <= p_76484_5_ + b0 && flag; ++l2) {
                    if (j1 >= 0 && j1 < 256) {
                        final Block block = p_76484_1_.func_147439_a(k1, j1, l2);
                        if (!block.isAir((IBlockAccess)p_76484_1_, k1, j1, l2) && !block.isLeaves((IBlockAccess)p_76484_1_, k1, j1, l2)) {
                            flag = true;
                        }
                    }
                    else {
                        flag = true;
                    }
                }
            }
        }
        if (!flag) {
            return;
        }
        int k2 = p_76484_4_ + i1;
        if (l == 1) {
            k2 = p_76484_4_ + i1 - 3;
        }
        for (int k1 = k2; k1 <= p_76484_4_ + i1; ++k1) {
            int l2 = 1;
            if (k1 < p_76484_4_ + i1) {
                ++l2;
            }
            if (l == 0) {
                l2 = 3;
            }
            for (int l3 = p_76484_3_ - l2; l3 <= p_76484_3_ + l2; ++l3) {
                for (int i2 = p_76484_5_ - l2; i2 <= p_76484_5_ + l2; ++i2) {
                    int j2 = 5;
                    if (l3 == p_76484_3_ - l2) {
                        --j2;
                    }
                    if (l3 == p_76484_3_ + l2) {
                        ++j2;
                    }
                    if (i2 == p_76484_5_ - l2) {
                        j2 -= 3;
                    }
                    if (i2 == p_76484_5_ + l2) {
                        j2 += 3;
                    }
                    if (l == 0 || k1 < p_76484_4_ + i1) {
                        if (l3 == p_76484_3_ - l2 || l3 == p_76484_3_ + l2) {
                            if (i2 == p_76484_5_ - l2) {
                                continue;
                            }
                            if (i2 == p_76484_5_ + l2) {
                                continue;
                            }
                        }
                        if (l3 == p_76484_3_ - (l2 - 1) && i2 == p_76484_5_ - l2) {
                            j2 = 1;
                        }
                        if (l3 == p_76484_3_ - l2 && i2 == p_76484_5_ - (l2 - 1)) {
                            j2 = 1;
                        }
                        if (l3 == p_76484_3_ + (l2 - 1) && i2 == p_76484_5_ - l2) {
                            j2 = 3;
                        }
                        if (l3 == p_76484_3_ + l2 && i2 == p_76484_5_ - (l2 - 1)) {
                            j2 = 3;
                        }
                        if (l3 == p_76484_3_ - (l2 - 1) && i2 == p_76484_5_ + l2) {
                            j2 = 7;
                        }
                        if (l3 == p_76484_3_ - l2 && i2 == p_76484_5_ + (l2 - 1)) {
                            j2 = 7;
                        }
                        if (l3 == p_76484_3_ + (l2 - 1) && i2 == p_76484_5_ + l2) {
                            j2 = 9;
                        }
                        if (l3 == p_76484_3_ + l2 && i2 == p_76484_5_ + (l2 - 1)) {
                            j2 = 9;
                        }
                    }
                    if (j2 == 5 && k1 < p_76484_4_ + i1) {
                        j2 = 0;
                    }
                    if ((j2 != 0 || p_76484_4_ >= p_76484_4_ + i1 - 1) && p_76484_1_.func_147439_a(l3, k1, i2).canBeReplacedByLeaves((IBlockAccess)p_76484_1_, l3, k1, i2)) {
                        p_76484_1_.func_147465_d(l3, k1, i2, (l == 0) ? BlockInit.mushroomBlockBlue : BlockInit.mushroomBlockGreen, j2, 2);
                    }
                }
            }
        }
        for (int k1 = 0; k1 < i1; ++k1) {
            final Block block2 = p_76484_1_.func_147439_a(p_76484_3_, p_76484_4_ + k1, p_76484_5_);
            if (block2.canBeReplacedByLeaves((IBlockAccess)p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_)) {
                p_76484_1_.func_147465_d(p_76484_3_, p_76484_4_ + k1, p_76484_5_, (l == 0) ? BlockInit.mushroomBlockBlue : BlockInit.mushroomBlockGreen, 10, 2);
            }
        }
    }

    protected boolean func_149854_a(final Block par1) {
        return true;
    }

    public boolean canSustainPlant(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection direction, final IPlantable plantable) {
        return direction != ForgeDirection.UP;
    }

    public Block func_149715_a(final float val) {
        this.field_149784_t = (int)val;
        return (Block)this;
    }

    public boolean func_149851_a(final World world, final int x, final int y, final int z, final boolean whatisthis) {
        final int meta = world.func_72805_g(x, y, z);
        return meta == 2 || meta == 3 || meta == 4 || meta == 5;
    }

    public boolean func_149852_a(final World p_149852_1_, final Random p_149852_2_, final int p_149852_3_, final int p_149852_4_, final int p_149852_5_) {
        return p_149852_1_.field_73012_v.nextFloat() < 0.45;
    }

    public void func_149853_b(final World world, final Random rand, final int x, final int y, final int z) {
        final int meta = world.func_72805_g(x, y, z);
        if (meta == 2 || meta == 3) {
            growMushroom(world, rand, x, y, z, 1);
        }
        else if (meta == 4 || meta == 5) {
            growMushroom(world, rand, x, y, z, 0);
        }
    }
}
