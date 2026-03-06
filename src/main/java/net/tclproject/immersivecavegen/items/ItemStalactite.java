package net.tclproject.immersivecavegen.items;

import net.minecraft.block.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.tclproject.immersivecavegen.blocks.*;
import net.minecraft.entity.*;

public class ItemStalactite extends MultiItemBlock
{
    public ItemStalactite(final Block block) {
        super(block);
    }

    public ItemStalactite(final Block block, final ArrayList names) {
        super(block, names);
    }

    public boolean func_77648_a(final ItemStack itemStack, final EntityPlayer par2EntityPlayer, final World world, int x, int y, int z, int side, final float par8, final float par9, final float par10) {
        final Block blockId = world.func_147439_a(x, y, z);
        if (blockId == Blocks.field_150433_aE && (world.func_72805_g(x, y, z) & 0x7) < 1) {
            side = 1;
        }
        else if (blockId != Blocks.field_150395_bd && blockId != Blocks.field_150329_H && blockId != Blocks.field_150330_I && (blockId == Blocks.field_150350_a || !blockId.isReplaceable((IBlockAccess)world, x, y, z))) {
            if (side == 0) {
                --y;
            }
            if (side == 1) {
                ++y;
            }
            if (side == 2) {
                --z;
            }
            if (side == 3) {
                ++z;
            }
            if (side == 4) {
                --x;
            }
            if (side == 5) {
                ++x;
            }
        }
        if (itemStack.field_77994_a == 0) {
            return false;
        }
        if (!par2EntityPlayer.func_82247_a(x, y, z, side, itemStack)) {
            return false;
        }
        if (y == 255) {
            return false;
        }
        if (!world.func_147472_a(BlockInit.stoneStalactiteBlock, x, y, z, false, side, (Entity)par2EntityPlayer, itemStack) && !world.func_147472_a(BlockInit.sandStalactiteBlock, x, y, z, false, side, (Entity)par2EntityPlayer, itemStack)) {
            return false;
        }
        if (this.canPlace(itemStack, world, x, y, z)) {
            final Block block = BlockInit.stoneStalactiteBlock;
            final int j1 = this.func_77647_b(itemStack.func_77960_j());
            final int k1 = block.func_149660_a(world, x, y, z, side, par8, par9, par10, j1);
            if (this.placeBlockAt(itemStack, par2EntityPlayer, world, x, y, z, side, par8, par9, par10, k1)) {
                world.func_72908_a((double)(x + 0.5f), (double)(y + 0.5f), (double)(z + 0.5f), block.field_149762_H.func_150496_b(), (block.field_149762_H.func_150497_c() + 1.0f) / 2.0f, block.field_149762_H.func_150494_d() * 0.8f);
                --itemStack.field_77994_a;
            }
            return true;
        }
        return false;
    }

    private boolean canPlace(final ItemStack itemStack, final World world, final int x, final int y, final int z) {
        boolean result = false;
        final int metadata = this.func_77647_b(itemStack.func_77960_j());
        final boolean upNormal = world.func_147445_c(x, y + 1, z, false);
        final boolean downNormal = world.func_147445_c(x, y - 1, z, false);
        final boolean upStalactite = world.func_147439_a(x, y + 1, z) == BlockInit.stoneStalactiteBlock || world.func_147439_a(x, y + 1, z) == BlockInit.sandStalactiteBlock;
        final boolean downStalactite = world.func_147439_a(x, y - 1, z) == BlockInit.stoneStalactiteBlock || world.func_147439_a(x, y - 1, z) == BlockInit.sandStalactiteBlock;
        if ((metadata != 0 && metadata != 4 && metadata != 5) || (!upNormal && !downNormal && !upStalactite && !downStalactite)) {
            if ((metadata >= 4 && metadata != 7 && metadata != 11) || (!upNormal && !upStalactite)) {
                if ((metadata == 6 || (metadata > 7 && metadata < 11) || metadata == 12) && (downNormal || downStalactite)) {
                    result = true;
                }
            }
            else {
                result = true;
            }
        }
        else {
            result = true;
        }
        return result;
    }
}
