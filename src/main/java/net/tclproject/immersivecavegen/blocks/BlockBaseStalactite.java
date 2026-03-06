package net.tclproject.immersivecavegen.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.tclproject.immersivecavegen.misc.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.creativetab.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.tclproject.immersivecavegen.entities.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.*;

public class BlockBaseStalactite extends Block
{
    private final Item droppedItem;
    private IIcon[] icons;

    public BlockBaseStalactite(final Item drop) {
        super(Material.field_151576_e);
        this.icons = new IIcon[26];
        this.droppedItem = drop;
        this.func_149711_c(0.8f);
        this.func_149672_a(BlockBaseStalactite.field_149769_e);
        this.func_149647_a(GamemodeTab.tabCaves);
    }

    public Item func_149650_a(final int metadata, final Random random, final int par3) {
        return this.droppedItem;
    }

    public int func_149745_a(final Random rand) {
        return rand.nextInt(3) - 1;
    }

    public boolean func_149718_j(final World world, final int x, final int y, final int z) {
        boolean result = true;
        int metaAbove = world.func_72805_g(x, y + 1, z);
        int metaUnder = world.func_72805_g(x, y - 1, z);
        final int metadata = world.func_72805_g(x, y, z);
        final Block above = world.func_147439_a(x, y + 1, z);
        final Block below = world.func_147439_a(x, y - 1, z);
        if (!(above instanceof BlockBaseStalactite)) {
            metaAbove = 0;
        }
        if (!(below instanceof BlockBaseStalactite)) {
            metaUnder = 0;
        }
        if (metadata >= 0 && metadata <= 3 && !above.func_149721_r()) {
            result = false;
        }
        if ((metadata == 0 || metadata == 8 || metadata == 9 || metadata == 10) && !below.func_149721_r()) {
            result = false;
        }
        if ((metadata == 4 || metadata == 5) && !above.func_149721_r() && metaAbove != 4 && metaAbove != 5 && metaAbove != 6 && metaAbove != 12 && metaAbove != 3 && !below.func_149721_r() && metaUnder != 4 && metaUnder != 5 && metaUnder != 11 && metaUnder != 8 && metaUnder != 7) {
            result = false;
        }
        if ((metadata == 6 || metadata == 12) && !below.func_149721_r() && metaUnder != 4 && metaUnder != 5 && metaUnder != 8) {
            result = false;
        }
        if ((metadata == 7 || metadata == 11) && !above.func_149721_r() && metaAbove != 4 && metaAbove != 5 && metaAbove != 3) {
            result = false;
        }
        return result;
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
        if (metadata > 25) {
            metadata = 0;
        }
        return this.icons[metadata];
    }

    public int func_149645_b() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void func_149666_a(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int i = 0; i < 13; ++i) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    public boolean func_149686_d() {
        return false;
    }

    public void func_149689_a(final World world, final int x, final int y, final int z, final EntityLivingBase par5EntityLivingBase, final ItemStack par6ItemStack) {
        this.func_149674_a(world, x, y, z, null);
    }

    public void func_149674_a(final World world, final int x, final int y, final int z, final Random random) {
        if (!this.func_149718_j(world, x, y, z)) {
            this.func_149697_b(world, x, y, z, world.func_72805_g(x, y, z), 0);
            world.func_147468_f(x, y, z);
        }
    }

    public void func_149670_a(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity.func_70089_S() && !(entity instanceof ICaveEntity)) {
            entity.func_70097_a(DamageSource.field_76367_g, 1.0f);
        }
        if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).field_71075_bZ.field_75098_d || !((EntityPlayer)entity).field_71075_bZ.field_75100_b) {
            entity.field_70159_w *= 0.7;
            entity.field_70179_y *= 0.7;
        }
    }

    public void func_149746_a(final World world, final int par2, final int par3, final int par4, final Entity entity, final float par6) {
        if (entity.func_70089_S()) {
            entity.func_70097_a(DamageSource.field_76377_j, 5.0f);
        }
    }

    public void func_149695_a(final World world, final int x, final int y, final int z, final Block blockID) {
        if (!world.field_72995_K && !this.func_149718_j(world, x, y, z)) {
            world.func_147480_a(x, y, z, true);
        }
    }

    @SideOnly(Side.CLIENT)
    public void func_149651_a(final IIconRegister iconRegister) {
        for (int i = 0; i < 13; ++i) {
            this.icons[i] = iconRegister.func_94245_a("immersivecavegen" + this.func_149641_N() + i);
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
            default: {
                this.func_149676_a(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);
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
        }
    }
}
