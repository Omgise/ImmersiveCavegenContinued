package net.tclproject.mysteriumlib.asm.fixes;

import net.minecraft.world.gen.*;
import net.minecraft.block.*;
import net.tclproject.immersivecavegen.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.tclproject.mysteriumlib.asm.annotations.*;
import net.minecraft.world.*;

public class MysteriumPatchesFixesRavine
{
    @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
    public static boolean func_151540_a(final MapGenRavine instance, final long p_151540_1_, final int p_151540_3_, final int p_151540_4_, final Block[] p_151540_5_, double p_151540_6_, double p_151540_8_, double p_151540_10_, final float p_151540_12_, float p_151540_13_, float p_151540_14_, int p_151540_15_, int p_151540_16_, final double p_151540_17_) {
        for (final String str : WGConfig.dimblacklist) {
            if (instance.field_75039_c != null && String.valueOf(instance.field_75039_c.field_73011_w.field_76574_g).equalsIgnoreCase(str)) {
                return false;
            }
        }
        final Random var19 = new Random(p_151540_1_);
        final double var20 = p_151540_3_ * 16 + 8;
        final double var21 = p_151540_4_ * 16 + 8;
        float var22 = 0.0f;
        float var23 = 0.0f;
        if (p_151540_16_ <= 0) {
            final int var24 = instance.field_75040_a * 16 - 16;
            p_151540_16_ = var24 - var19.nextInt(var24 / 4);
        }
        boolean var25 = false;
        if (p_151540_15_ == -1) {
            p_151540_15_ = p_151540_16_ / 2;
            var25 = true;
        }
        float var26 = 1.0f;
        for (int var27 = 0; var27 < 256; ++var27) {
            if (var27 == 0 || var19.nextInt(3) == 0) {
                var26 = 1.0f + var19.nextFloat() * var19.nextFloat() * 1.0f;
            }
            instance.field_75046_d[var27] = var26 * var26;
        }
        while (p_151540_15_ < p_151540_16_) {
            double var28 = 1.5 + MathHelper.func_76126_a(p_151540_15_ * 3.1415927f / p_151540_16_) * p_151540_12_ * 1.0f;
            double var29 = var28 * p_151540_17_;
            var28 *= var19.nextFloat() * 0.25 + 0.75;
            var29 *= var19.nextFloat() * 0.25 + 0.75;
            final float var30 = MathHelper.func_76134_b(p_151540_14_);
            final float var31 = MathHelper.func_76126_a(p_151540_14_);
            p_151540_6_ += MathHelper.func_76134_b(p_151540_13_) * var30;
            p_151540_8_ += var31;
            p_151540_10_ += MathHelper.func_76126_a(p_151540_13_) * var30;
            p_151540_14_ *= 0.7f;
            p_151540_14_ += var23 * 0.05f;
            p_151540_13_ += var22 * 0.05f;
            var23 *= 0.8f;
            var22 *= 0.5f;
            var23 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0f;
            var22 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0f;
            if (var25 || var19.nextInt(4) != 0) {
                final double var32 = p_151540_6_ - var20;
                final double var33 = p_151540_10_ - var21;
                final double var34 = p_151540_16_ - p_151540_15_;
                final double var35 = p_151540_12_ + 2.0f + 16.0f;
                if (var32 * var32 + var33 * var33 - var34 * var34 > var35 * var35) {
                    return true;
                }
                if (p_151540_6_ >= var20 - 16.0 - var28 * 2.0 && p_151540_10_ >= var21 - 16.0 - var28 * 2.0 && p_151540_6_ <= var20 + 16.0 + var28 * 2.0 && p_151540_10_ <= var21 + 16.0 + var28 * 2.0) {
                    int var36 = MathHelper.func_76128_c(p_151540_6_ - var28) - p_151540_3_ * 16 - 1;
                    int var37 = MathHelper.func_76128_c(p_151540_6_ + var28) - p_151540_3_ * 16 + 1;
                    int var38 = MathHelper.func_76128_c(p_151540_8_ - var29) - 1;
                    int var39 = MathHelper.func_76128_c(p_151540_8_ + var29) + 1;
                    int var40 = MathHelper.func_76128_c(p_151540_10_ - var28) - p_151540_4_ * 16 - 1;
                    int var41 = MathHelper.func_76128_c(p_151540_10_ + var28) - p_151540_4_ * 16 + 1;
                    if (var36 < 0) {
                        var36 = 0;
                    }
                    if (var37 > 16) {
                        var37 = 16;
                    }
                    if (var38 < 1) {
                        var38 = 1;
                    }
                    if (var39 > 248) {
                        var39 = 248;
                    }
                    if (var40 < 0) {
                        var40 = 0;
                    }
                    if (var41 > 16) {
                        var41 = 16;
                    }
                    boolean var42 = false;
                    for (int var43 = var36; !var42 && var43 < var37; ++var43) {
                        for (int var44 = var40; !var42 && var44 < var41; ++var44) {
                            for (int var45 = var39 + 1; !var42 && var45 >= var38 - 1; --var45) {
                                final int var46 = (var43 * 16 + var44) * 256 + var45;
                                if (var45 >= 0 && var45 < 256) {
                                    final Block var47 = p_151540_5_[var46];
                                    if (var47 == Blocks.field_150358_i || var47 == Blocks.field_150355_j) {
                                        var42 = true;
                                    }
                                    if (var45 != var38 - 1 && var43 != var36 && var43 != var37 - 1 && var44 != var40 && var44 != var41 - 1) {
                                        var45 = var38;
                                    }
                                }
                            }
                        }
                    }
                    if (!var42) {
                        for (int var43 = var36; var43 < var37; ++var43) {
                            final double var48 = (var43 + p_151540_3_ * 16 + 0.5 - p_151540_6_) / var28;
                            for (int var46 = var40; var46 < var41; ++var46) {
                                final double var49 = (var46 + p_151540_4_ * 16 + 0.5 - p_151540_10_) / var28;
                                int var50 = (var43 * 16 + var46) * 256 + var39;
                                boolean var51 = false;
                                if (var48 * var48 + var49 * var49 < 1.0) {
                                    for (int var52 = var39 - 1; var52 >= var38; --var52) {
                                        final double var53 = (var52 + 0.5 - p_151540_8_) / var29;
                                        if ((var48 * var48 + var49 * var49) * instance.field_75046_d[var52] + var53 * var53 / 6.0 < 1.0) {
                                            final Block var54 = p_151540_5_[var50];
                                            if (var54 == Blocks.field_150349_c) {
                                                var51 = true;
                                            }
                                            if (var54 == Blocks.field_150348_b || var54 == Blocks.field_150346_d || var54 == Blocks.field_150349_c) {
                                                if (var52 < 10) {
                                                    p_151540_5_[var50] = (Block)Blocks.field_150356_k;
                                                }
                                                else {
                                                    p_151540_5_[var50] = null;
                                                    if (var51 && p_151540_5_[var50 - 1] == Blocks.field_150346_d) {
                                                        p_151540_5_[var50 - 1] = instance.field_75039_c.getBiomeGenForCoordsBody(var43 + p_151540_3_ * 16, var46 + p_151540_4_ * 16).field_76752_A;
                                                    }
                                                }
                                            }
                                        }
                                        --var50;
                                    }
                                }
                            }
                        }
                        if (var25) {
                            break;
                        }
                    }
                }
            }
            ++p_151540_15_;
        }
        return true;
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
    public static boolean func_151538_a(final MapGenRavine instance, final World p_151538_1_, final int p_151538_2_, final int p_151538_3_, final int p_151538_4_, final int p_151538_5_, final Block[] p_151538_6_) {
        for (final String str : WGConfig.dimblacklist) {
            if (p_151538_1_ != null && String.valueOf(p_151538_1_.field_73011_w.field_76574_g).equalsIgnoreCase(str)) {
                return false;
            }
        }
        if ((WGConfig.oneBigCave || instance.field_75038_b.nextInt(50) == 100) && !WGConfig.turnOffVanillaCaverns) {
            final double d0 = p_151538_2_ * 16 + instance.field_75038_b.nextInt(16);
            final double d2 = instance.field_75038_b.nextInt(instance.field_75038_b.nextInt(40) + 8) + 20;
            final double d3 = p_151538_3_ * 16 + instance.field_75038_b.nextInt(16);
            final byte b0 = 1;
            for (int i1 = 0; i1 < b0; ++i1) {
                final float f = instance.field_75038_b.nextFloat() * 3.1415927f * 2.0f;
                final float f2 = (instance.field_75038_b.nextFloat() - 0.5f) * 2.0f / 8.0f;
                final float f3 = (instance.field_75038_b.nextFloat() * 2.0f + instance.field_75038_b.nextFloat()) * 2.0f;
                instance.func_151540_a(instance.field_75038_b.nextLong(), p_151538_4_, p_151538_5_, p_151538_6_, d0, d2, d3, f3, f, f2, 0, 0, 3.0);
            }
        }
        return true;
    }
}
