package net.tclproject.immersivecavegen.blocks;

import net.minecraft.block.*;
import net.tclproject.immersivecavegen.misc.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.block.material.*;
import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.client.renderer.texture.*;

public class BlockIceStalactite extends Block
{
    private IIcon[] icons;

    public BlockIceStalactite() {
        super(Material.field_151588_w);
        this.icons = new IIcon[3];
        this.func_149647_a(GamemodeTab.tabCaves);
        this.func_149752_b(0.6f);
        this.func_149663_c("icestalactite");
        this.func_149672_a(BlockIceStalactite.field_149778_k);
    }

    public int func_149701_w() {
        return 1;
    }

    public void func_149670_a(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity.func_70089_S()) {
            entity.func_70097_a(DamageSource.field_76367_g, 1.0f);
        }
    }

    public boolean func_149718_j(final World world, final int x, final int y, final int z) {
        return world.func_147439_a(x, y + 1, z).isNormalCube((IBlockAccess)world, x, y, z) || world.func_147439_a(x, y + 1, z) instanceof BlockIceStalactite || world.func_147439_a(x, y + 1, z).func_149688_o().func_151565_r() == MapColor.field_151657_g;
    }

    public boolean func_149742_c(final World world, final int x, final int y, final int z) {
        return this.func_149718_j(world, x, y, z) && super.func_149742_c(world, x, y, z);
    }

    protected boolean func_149700_E() {
        return true;
    }

    public AxisAlignedBB func_149668_a(final World par1World, final int par2, final int par3, final int par4) {
        return null;
    }

    public int func_149643_k(final World world, final int x, final int y, final int z) {
        return world.func_72805_g(x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_149691_a(final int side, int metadata) {
        if (metadata > 2) {
            metadata = 0;
        }
        return this.icons[metadata];
    }

    public int func_149645_b() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void func_149666_a(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int i = 0; i < this.icons.length; ++i) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    public Item func_149650_a(final int metadata, final Random random, final int par3) {
        return Item.func_150898_a(Blocks.field_150432_aD);
    }

    public boolean func_149686_d() {
        return false;
    }

    public void func_149695_a(final World world, final int x, final int y, final int z, final Block block) {
        if (!this.func_149718_j(world, x, y, z)) {
            this.func_149696_a(world, x, y, z, world.func_72805_g(x, y, z), 0);
            world.func_147468_f(x, y, z);
        }
    }

    public int func_149745_a(final Random rand) {
        return rand.nextInt(3) - 1;
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(final IIconRegister iconRegister) {
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = iconRegister.func_94245_a("immersivecavegen:icestalactite" + i);
        }
    }

    public boolean func_149662_c() {
        return false;
    }

    public void func_149719_a(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
        final int metadata = par1IBlockAccess.func_72805_g(par2, par3, par4);
        switch (metadata) {
            case 1: {
                this.func_149676_a(0.25f, 0.2f, 0.25f, 0.75f, 1.0f, 0.75f);
                break;
            }
            case 2: {
                this.func_149676_a(0.25f, 0.5f, 0.25f, 0.75f, 1.0f, 0.75f);
                break;
            }
            case 9: {
                this.func_149676_a(0.25f, 0.0f, 0.25f, 0.75f, 0.8f, 0.75f);
                break;
            }
            case 10: {
                this.func_149676_a(0.25f, 0.0f, 0.25f, 0.75f, 0.4f, 0.75f);
                break;
            }
            default: {
                this.func_149676_a(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);
                break;
            }
        }
    }
}
