package net.tclproject.mysteriumlib.asm.fixes;

import net.minecraft.block.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import java.util.*;
import net.tclproject.immersivecavegen.*;
import net.minecraft.block.material.*;
import net.tclproject.mysteriumlib.asm.annotations.*;

public class MysteriumPatchesFixesSand
{
    private static Block sandBlock;
    private static Block solidBlock;
    private static int radius;

    @Fix(targetMethod = "<init>")
    public static void WorldGenSand(final WorldGenSand s, final Block p_i45462_1_, final int p_i45462_2_) {
        MysteriumPatchesFixesSand.sandBlock = p_i45462_1_;
        if (MysteriumPatchesFixesSand.sandBlock == Blocks.field_150354_m) {
            MysteriumPatchesFixesSand.solidBlock = Blocks.field_150322_A;
        }
        else if (MysteriumPatchesFixesSand.sandBlock == Blocks.field_150351_n) {
            MysteriumPatchesFixesSand.solidBlock = Blocks.field_150348_b;
        }
        else {
            MysteriumPatchesFixesSand.solidBlock = MysteriumPatchesFixesSand.sandBlock;
        }
        MysteriumPatchesFixesSand.radius = p_i45462_2_;
    }

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static boolean generate(final WorldGenSand s, final World p_76484_1_, final Random p_76484_2_, final int p_76484_3_, final int p_76484_4_, final int p_76484_5_) {
        for (final String str : WGConfig.dimblacklist) {
            if (p_76484_1_ != null && String.valueOf(p_76484_1_.field_73011_w.field_76574_g).equalsIgnoreCase(str)) {
                return false;
            }
        }
        if (p_76484_1_.func_147439_a(p_76484_3_, p_76484_4_, p_76484_5_).func_149688_o() != Material.field_151586_h) {
            return false;
        }
        final int var6 = p_76484_2_.nextInt(MysteriumPatchesFixesSand.radius - 2) + 2;
        final byte var7 = 2;
        for (int var8 = p_76484_3_ - var6; var8 <= p_76484_3_ + var6; ++var8) {
            for (int var9 = p_76484_5_ - var6; var9 <= p_76484_5_ + var6; ++var9) {
                final int var10 = var8 - p_76484_3_;
                final int var11 = var9 - p_76484_5_;
                if (var10 * var10 + var11 * var11 <= var6 * var6) {
                    for (int var12 = p_76484_4_ - var7; var12 <= p_76484_4_ + var7; ++var12) {
                        final Block var13 = p_76484_1_.func_147439_a(var8, var12, var9);
                        if (var13 == Blocks.field_150346_d || var13 == Blocks.field_150349_c) {
                            if (p_76484_1_.func_147439_a(var8, var12 - 1, var9) == Blocks.field_150350_a) {
                                if (var12 >= 62 && p_76484_1_.func_147439_a(var8, var12 - 2, var9) != Blocks.field_150350_a) {
                                    p_76484_1_.func_147465_d(var8, var12, var9, MysteriumPatchesFixesSand.sandBlock, 0, 2);
                                }
                                else {
                                    p_76484_1_.func_147465_d(var8, var12, var9, MysteriumPatchesFixesSand.solidBlock, 0, 2);
                                }
                            }
                            else {
                                p_76484_1_.func_147465_d(var8, var12, var9, MysteriumPatchesFixesSand.sandBlock, 0, 2);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
