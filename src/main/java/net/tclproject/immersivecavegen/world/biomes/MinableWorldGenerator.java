package net.tclproject.immersivecavegen.world.biomes;

import net.minecraft.world.gen.feature.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.util.*;

public class MinableWorldGenerator extends WorldGenerator
{
    private Block replacedWith;
    private int replaceWithBlockMetadata;
    private int numberOfBlocks;
    private Block toBeReplaced;

    public MinableWorldGenerator(final Block block1, final int par2) {
        this.replaceWithBlockMetadata = 0;
        this.replacedWith = block1;
        this.numberOfBlocks = par2;
        this.toBeReplaced = Blocks.field_150348_b;
    }

    public MinableWorldGenerator(final Block block1, final int par2, final Block block2) {
        this.replaceWithBlockMetadata = 0;
        this.replacedWith = block1;
        this.numberOfBlocks = par2;
        this.toBeReplaced = block2;
    }

    public boolean func_76484_a(final World par1World, final Random par2Random, final int x, final int y, final int z) {
        final float var6 = par2Random.nextFloat() * 3.1415927f;
        final double var7 = x + 8 + MathHelper.func_76126_a(var6) * this.numberOfBlocks / 8.0f;
        final double var8 = x + 8 - MathHelper.func_76126_a(var6) * this.numberOfBlocks / 8.0f;
        final double var9 = z + 8 + MathHelper.func_76134_b(var6) * this.numberOfBlocks / 8.0f;
        final double var10 = z + 8 - MathHelper.func_76134_b(var6) * this.numberOfBlocks / 8.0f;
        final double var11 = y + par2Random.nextInt(3) - 2;
        final double var12 = y + par2Random.nextInt(3) - 2;
        for (int var13 = 0; var13 <= this.numberOfBlocks; ++var13) {
            final double var14 = var7 + (var8 - var7) * var13 / this.numberOfBlocks;
            final double var15 = var11 + (var12 - var11) * var13 / this.numberOfBlocks;
            final double var16 = var9 + (var10 - var9) * var13 / this.numberOfBlocks;
            final double var17 = par2Random.nextDouble() * this.numberOfBlocks / 16.0;
            final double var18 = (MathHelper.func_76126_a(var13 * 3.1415927f / this.numberOfBlocks) + 1.0f) * var17 + 1.0;
            final double var19 = (MathHelper.func_76126_a(var13 * 3.1415927f / this.numberOfBlocks) + 1.0f) * var17 + 1.0;
            final int var20 = MathHelper.func_76128_c(var14 - var18 / 2.0);
            final int var21 = MathHelper.func_76128_c(var15 - var19 / 2.0);
            final int var22 = MathHelper.func_76128_c(var16 - var18 / 2.0);
            final int var23 = MathHelper.func_76128_c(var14 + var18 / 2.0);
            final int var24 = MathHelper.func_76128_c(var15 + var19 / 2.0);
            final int var25 = MathHelper.func_76128_c(var16 + var18 / 2.0);
            for (int var26 = var20; var26 <= var23; ++var26) {
                final double var27 = (var26 + 0.5 - var14) / (var18 / 2.0);
                if (var27 * var27 < 1.0) {
                    for (int var28 = var21; var28 <= var24; ++var28) {
                        final double var29 = (var28 + 0.5 - var15) / (var19 / 2.0);
                        if (var27 * var27 + var29 * var29 < 1.0) {
                            for (int var30 = var22; var30 <= var25; ++var30) {
                                final double var31 = (var30 + 0.5 - var16) / (var18 / 2.0);
                                final Block block = par1World.func_147439_a(var26, var28, var30);
                                if (var27 * var27 + var29 * var29 + var31 * var31 < 1.0 && block != null && (block == this.toBeReplaced || (this.toBeReplaced == Blocks.field_150346_d && block == Blocks.field_150349_c))) {
                                    if (par1World.func_147437_c(var26, var28 + 1, var30) && this.replacedWith == Blocks.field_150346_d) {
                                        par1World.func_147465_d(var26, var28, var30, (Block)Blocks.field_150349_c, this.replaceWithBlockMetadata, 3);
                                    }
                                    else {
                                        par1World.func_147465_d(var26, var28, var30, this.replacedWith, this.replaceWithBlockMetadata, 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
