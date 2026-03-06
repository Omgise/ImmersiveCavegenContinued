package net.tclproject.immersivecavegen.world;

import net.minecraft.world.gen.feature.*;
import net.minecraft.block.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.block.material.*;
import net.minecraft.world.biome.*;

public class WorldGenLakesUnderground extends WorldGenLakes
{
    public WorldGenLakesUnderground(final Block block) {
        super(block);
    }

    public boolean func_76484_a(final World p_76484_1_, final Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
        for (p_76484_3_ -= 8, p_76484_5_ -= 8; p_76484_4_ > 5 && p_76484_1_.func_147437_c(p_76484_3_, p_76484_4_, p_76484_5_); --p_76484_4_) {}
        if (p_76484_4_ <= 4) {
            return false;
        }
        p_76484_4_ -= 4;
        if (p_76484_1_.func_147437_c(p_76484_3_, p_76484_4_, p_76484_5_)) {
            return false;
        }
        final boolean[] aboolean = new boolean[2048];
        for (int l = p_76484_2_.nextInt(4) + 4, i1 = 0; i1 < l; ++i1) {
            final double d0 = p_76484_2_.nextDouble() * 6.0 + 3.0;
            final double d2 = p_76484_2_.nextDouble() * 4.0 + 2.0;
            final double d3 = p_76484_2_.nextDouble() * 6.0 + 3.0;
            final double d4 = p_76484_2_.nextDouble() * (16.0 - d0 - 2.0) + 1.0 + d0 / 2.0;
            final double d5 = p_76484_2_.nextDouble() * (8.0 - d2 - 4.0) + 2.0 + d2 / 2.0;
            final double d6 = p_76484_2_.nextDouble() * (16.0 - d3 - 2.0) + 1.0 + d3 / 2.0;
            for (int k1 = 1; k1 < 15; ++k1) {
                for (int l2 = 1; l2 < 15; ++l2) {
                    for (int i2 = 1; i2 < 7; ++i2) {
                        final double d7 = (k1 - d4) / (d0 / 2.0);
                        final double d8 = (i2 - d5) / (d2 / 2.0);
                        final double d9 = (l2 - d6) / (d3 / 2.0);
                        final double d10 = d7 * d7 + d8 * d8 + d9 * d9;
                        if (d10 < 1.0) {
                            aboolean[(k1 * 16 + l2) * 8 + i2] = true;
                        }
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j2 = 0; j2 < 16; ++j2) {
                for (int j3 = 0; j3 < 8; ++j3) {
                    final boolean flag = !aboolean[(i1 * 16 + j2) * 8 + j3] && ((i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j3]) || (i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j3]) || (j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j3]) || (j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j3]) || (j3 < 7 && aboolean[(i1 * 16 + j2) * 8 + j3 + 1]) || (j3 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j3 - 1)]));
                    if (flag) {
                        final Material material = p_76484_1_.func_147439_a(p_76484_3_ + i1, p_76484_4_ + j3, p_76484_5_ + j2).func_149688_o();
                        if (j3 >= 4 && material.func_76224_d()) {
                            return false;
                        }
                        if (j3 < 4 && !material.func_76220_a() && p_76484_1_.func_147439_a(p_76484_3_ + i1, p_76484_4_ + j3, p_76484_5_ + j2) != this.field_150556_a) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j2 = 0; j2 < 16; ++j2) {
                for (int j3 = 0; j3 < 8; ++j3) {
                    if (aboolean[(i1 * 16 + j2) * 8 + j3]) {
                        p_76484_1_.func_147465_d(p_76484_3_ + i1, p_76484_4_ + j3, p_76484_5_ + j2, (j3 >= 4) ? Blocks.field_150350_a : Blocks.field_150355_j, 0, 2);
                    }
                }
            }
        }
        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j2 = 0; j2 < 16; ++j2) {
                for (int j3 = 4; j3 < 8; ++j3) {
                    if (aboolean[(i1 * 16 + j2) * 8 + j3] && p_76484_1_.func_147439_a(p_76484_3_ + i1, p_76484_4_ + j3 - 1, p_76484_5_ + j2) == Blocks.field_150346_d && p_76484_1_.func_72972_b(EnumSkyBlock.Sky, p_76484_3_ + i1, p_76484_4_ + j3, p_76484_5_ + j2) > 0) {
                        final BiomeGenBase biomegenbase = p_76484_1_.func_72807_a(p_76484_3_ + i1, p_76484_5_ + j2);
                        if (biomegenbase.field_76752_A == Blocks.field_150391_bh) {
                            p_76484_1_.func_147465_d(p_76484_3_ + i1, p_76484_4_ + j3 - 1, p_76484_5_ + j2, (Block)Blocks.field_150391_bh, 0, 2);
                        }
                        else {
                            p_76484_1_.func_147465_d(p_76484_3_ + i1, p_76484_4_ + j3 - 1, p_76484_5_ + j2, (Block)Blocks.field_150349_c, 0, 2);
                        }
                    }
                }
            }
        }
        if (this.field_150556_a.func_149688_o() == Material.field_151587_i) {
            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j2 = 0; j2 < 16; ++j2) {
                    for (int j3 = 0; j3 < 8; ++j3) {
                        final boolean flag = !aboolean[(i1 * 16 + j2) * 8 + j3] && ((i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j3]) || (i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j3]) || (j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j3]) || (j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j3]) || (j3 < 7 && aboolean[(i1 * 16 + j2) * 8 + j3 + 1]) || (j3 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j3 - 1)]));
                        if (flag && (j3 < 4 || p_76484_2_.nextInt(2) != 0) && p_76484_1_.func_147439_a(p_76484_3_ + i1, p_76484_4_ + j3, p_76484_5_ + j2).func_149688_o().func_76220_a()) {
                            p_76484_1_.func_147465_d(p_76484_3_ + i1, p_76484_4_ + j3, p_76484_5_ + j2, Blocks.field_150348_b, 0, 2);
                        }
                    }
                }
            }
        }
        if (this.field_150556_a.func_149688_o() == Material.field_151586_h) {
            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j2 = 0; j2 < 16; ++j2) {
                    final byte b0 = 4;
                    if (p_76484_1_.func_72884_u(p_76484_3_ + i1, p_76484_4_ + b0, p_76484_5_ + j2)) {
                        p_76484_1_.func_147465_d(p_76484_3_ + i1, p_76484_4_ + b0, p_76484_5_ + j2, Blocks.field_150432_aD, 0, 2);
                    }
                }
            }
        }
        return true;
    }
}
