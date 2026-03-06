package net.tclproject.mysteriumlib.asm.fixes;

import java.util.*;
import net.minecraft.world.gen.structure.*;
import net.minecraft.world.chunk.*;
import net.tclproject.mysteriumlib.asm.annotations.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.block.material.*;
import net.minecraft.world.gen.*;
import net.minecraft.block.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraft.world.biome.*;
import net.minecraftforge.event.terraingen.*;
import net.minecraft.world.*;
import net.tclproject.immersivecavegen.*;
import net.minecraft.world.gen.feature.*;

public class MysteriumPatchesFixesOld
{
    protected static Random noiseGen;
    protected static Random b;
    protected static float[] ravineData;
    protected static World world;
    protected static long seedX;
    protected static long seedZ;
    protected static long worldSeed;
    protected static long chunkSeed;
    protected static int colossalCaveChance;
    protected static boolean genCaves;
    protected static boolean genFillerCaves;
    private static Random newRand;
    public static MapGenStronghold strongholdGenerator;
    public static MapGenVillage villageGenerator;
    public static MapGenMineshaft mineshaftGenerator;
    public static MapGenScatteredFeature scatteredFeatureGenerator;

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
    public static boolean func_151539_a(final MapGenBase instance, final IChunkProvider p_151539_1_, final World par2World, final int chunkX, final int chunkZ, final Block[] par5ArrayOfBlock) {
        for (final String str : WGConfig.dimblacklist) {
            if (MysteriumPatchesFixesOld.world != null && String.valueOf(MysteriumPatchesFixesOld.world.field_73011_w.field_76574_g).equalsIgnoreCase(str)) {
                return false;
            }
        }
        if (instance instanceof MapGenCaves) {
            MysteriumPatchesFixesOld.world = par2World;
            MysteriumPatchesFixesOld.worldSeed = MysteriumPatchesFixesOld.world.func_72905_C();
            MysteriumPatchesFixesOld.b.setSeed(MysteriumPatchesFixesOld.worldSeed);
            MysteriumPatchesFixesOld.noiseGen.setSeed(MysteriumPatchesFixesOld.worldSeed);
            MysteriumPatchesFixesOld.seedX = MysteriumPatchesFixesOld.b.nextLong();
            MysteriumPatchesFixesOld.seedZ = MysteriumPatchesFixesOld.b.nextLong();
            for (int varChunkX = chunkX - 12; varChunkX <= chunkX + 12; ++varChunkX) {
                for (int varChunkZ = chunkZ - 12; varChunkZ <= chunkZ + 12; ++varChunkZ) {
                    MysteriumPatchesFixesOld.chunkSeed = (varChunkX * MysteriumPatchesFixesOld.seedX ^ varChunkZ * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
                    final boolean genRavines = validCaveLocation(varChunkX, varChunkZ);
                    generateCaves(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
                    for (int hj = 1; hj < WGConfig.cavesSpawnMultiplier; ++hj) {
                        generateCaves(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
                    }
                    if (genRavines) {
                        generateRavines(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
                        for (int hj = 1; hj < WGConfig.cavernsSpawnMultiplier; ++hj) {
                            generateRavines(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
                        }
                    }
                    generateColossalCaves(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
                    for (int hj = 1; hj < WGConfig.oldBigCavesSpawnMultiplier; ++hj) {
                        generateColossalCaves(varChunkX, varChunkZ, chunkX, chunkZ, par5ArrayOfBlock);
                    }
                }
            }
            return true;
        }
        return false;
    }

    protected static void generateLargeCaveNode(final long par1, final int par3, final int par4, final Block[] par5ArrayOfBlock, final double par6, final double par8, final double par10) {
        generateCaveNode(par1, par3, par4, par5ArrayOfBlock, par6, par8, par10, 1.0f + MysteriumPatchesFixesOld.b.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5, 0);
    }

    protected static void generateCaveNode(final long par1, final int par3, final int par4, final Block[] par5ArrayOfBlock, double par6, double par8, double par10, float par12, float par13, float par14, int par15, int par16, double par17, int bigCave) {
        if (bigCave == 0) {
            final Random r = new Random();
            if (r.nextInt(100) < WGConfig.giantCaveChance) {
                bigCave = 3;
            }
        }
        final double var19 = par3 * 16 + 8;
        final double var20 = par4 * 16 + 8;
        float var21 = 0.0f;
        float var22 = 0.0f;
        final Random var23 = new Random(par1);
        float curviness1 = 0.1f;
        float curviness2 = 0.1f;
        if (par16 <= 0) {
            par16 = 112 - var23.nextInt(28);
        }
        int var24 = var23.nextInt(par16 / 2) + par16 / 4;
        double minWidth = 1.5;
        if (bigCave != 1 && bigCave != 2) {
            if (bigCave == 3) {
                par17 = 1.0f - par12 / 100.0f;
                minWidth = 2.0;
            }
        }
        else {
            if (bigCave == 1) {
                par16 = 112 + var23.nextInt(var23.nextInt(337) + 1);
                if (par16 > 336) {
                    par16 = 336;
                }
                if (var23.nextBoolean()) {
                    final float multiplier = var23.nextFloat() * par16 / 96.0f + (672 - par16) / 672.0f;
                    if (multiplier > 1.0f) {
                        par12 *= multiplier;
                    }
                }
                else {
                    par12 *= var23.nextFloat() + 1.0f;
                }
            }
            else {
                par16 = 224 + var23.nextInt(113);
            }
            var24 = var23.nextInt(par16 / 4) + par16 / 2;
            curviness1 = par16 / 3360.0f + 0.05f;
            if (curviness1 < 0.1f) {
                curviness2 = curviness1;
            }
            par17 = 1.0f - par12 / 100.0f;
            minWidth = 2.5;
        }
        boolean var25 = false;
        if (par15 == -1) {
            par15 = par16 / 2;
            var25 = true;
        }
        int skipCount = 999;
        final boolean var26 = var23.nextInt(6) == 0;
        while (par15 < par16) {
            final double var27 = minWidth + MathHelper.func_76126_a(par15 * 3.141593f / par16) * par12;
            final double var28 = var27 * par17;
            final float var29 = MathHelper.func_76134_b(par14);
            final float var30 = MathHelper.func_76126_a(par14);
            par6 += MathHelper.func_76134_b(par13) * var29;
            par8 += var30;
            par10 += MathHelper.func_76126_a(par13) * var29;
            if (var26) {
                par14 *= 0.92f;
            }
            else {
                par14 *= 0.7f;
            }
            par14 += var22 * curviness2;
            par13 += var21 * curviness1;
            var22 *= 0.9f;
            var21 *= 0.75f;
            var22 += (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 2.0f;
            var21 += (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 4.0f;
            if (!var25 && bigCave < 3 && par15 == var24 && par12 > 1.0f && par16 > 0) {
                if (bigCave == 0) {
                    generateCaveNode(var23.nextLong(), par3, par4, par5ArrayOfBlock, par6, par8, par10, var23.nextFloat() * 0.5f + 0.5f, par13 - 1.5707964f, par14 / 3.0f, par15, par16, 1.0, 0);
                    generateCaveNode(var23.nextLong(), par3, par4, par5ArrayOfBlock, par6, par8, par10, var23.nextFloat() * 0.5f + 0.5f, par13 + 1.5707964f, par14 / 3.0f, par15, par16, 1.0, 0);
                    return;
                }
                generateCaveNode(var23.nextLong(), par3, par4, par5ArrayOfBlock, par6, par8, par10, var23.nextFloat() * par12 / 3.0f + par12 / 3.0f, par13 - 1.5707964f, par14 / 3.0f, par15, par16, 1.0, 3);
                generateCaveNode(var23.nextLong(), par3, par4, par5ArrayOfBlock, par6, par8, par10, var23.nextFloat() * par12 / 3.0f + par12 / 3.0f, par13 + 1.5707964f, par14 / 3.0f, par15, par16, 1.0, 3);
            }
            else {
                if (!var25 && var23.nextInt(4) != 0 && skipCount <= (int)var27 / 2) {
                    ++skipCount;
                }
                else {
                    final double var31 = par6 - var19;
                    final double var32 = par10 - var20;
                    final double var33 = par16 - par15 + (double)(par12 + 18.0f);
                    if (var31 * var31 + var32 * var32 > var33 * var33) {
                        return;
                    }
                    skipCount = 0;
                    final double var29_2 = var27 * 2.0;
                    if (par6 >= var19 - 16.0 - var29_2 && par10 >= var20 - 16.0 - var29_2 && par6 <= var19 + 16.0 + var29_2 && par10 <= var20 + 16.0 + var29_2) {
                        int var34 = MathHelper.func_76128_c(par6 - var27) - par3 * 16 - 1;
                        int var35 = MathHelper.func_76128_c(par6 + var27) - par3 * 16 + 1;
                        int var36 = MathHelper.func_76128_c(par8 - var28) - 1;
                        int var37 = MathHelper.func_76128_c(par8 + var28) + 1;
                        int var38 = MathHelper.func_76128_c(par10 - var27) - par4 * 16 - 1;
                        int var39 = MathHelper.func_76128_c(par10 + var27) - par4 * 16 + 1;
                        if (var34 < 0) {
                            var34 = 0;
                        }
                        if (var35 > 16) {
                            var35 = 16;
                        }
                        if (var36 < 0) {
                            var36 = 0;
                        }
                        if (var37 > 126) {
                            var37 = 126;
                        }
                        if (var38 < 0) {
                            var38 = 0;
                        }
                        if (var39 > 16) {
                            var39 = 16;
                        }
                        double noiseMultiplier = 0.55 / (var29_2 - 2.0);
                        if (noiseMultiplier > 0.3) {
                            noiseMultiplier = 0.3;
                        }
                        for (int var40 = var34; var40 < var35; ++var40) {
                            double var41 = (var40 + par3 * 16 + 0.5 - par6) / var27;
                            var41 *= var41;
                            for (int var42 = var38; var42 < var39; ++var42) {
                                double var43 = (var42 + par4 * 16 + 0.5 - par10) / var27;
                                var43 = var43 * var43 + var41;
                                int var44 = var40 << 12 | var42 << 8 | var37;
                                int yIndex = var37;
                                int grassMycelium = 0;
                                if (var43 < 1.0) {
                                    for (int var45 = var37 - 1; var45 >= var36; --var45) {
                                        final double var46 = (var45 + 0.5 - par8) / var28;
                                        if (var46 > -0.7 && var46 * var46 + var43 + (MysteriumPatchesFixesOld.noiseGen.nextInt(3) - 1) * noiseMultiplier < 1.0 && waterCheck(var40, yIndex, var42, par5ArrayOfBlock)) {
                                            final Block var47 = par5ArrayOfBlock[var44];
                                            if (var47 != null && var47 != Blocks.field_150357_h) {
                                                final int biome = MysteriumPatchesFixesOld.world.getBiomeGenForCoordsBody(var40 + par3 * 16, var42 + par4 * 16).field_76756_M;
                                                if (var45 < 60 || biome != 16) {
                                                    if (var47 == Blocks.field_150349_c) {
                                                        grassMycelium = 1;
                                                    }
                                                    if (var47 == Blocks.field_150391_bh) {
                                                        grassMycelium = 2;
                                                    }
                                                    if (var45 < 10) {
                                                        par5ArrayOfBlock[var44] = Blocks.field_150353_l;
                                                    }
                                                    else {
                                                        par5ArrayOfBlock[var44] = null;
                                                        if (grassMycelium > 0 && par5ArrayOfBlock[var44 - 1] == Blocks.field_150346_d) {
                                                            if (grassMycelium == 1) {
                                                                par5ArrayOfBlock[var44 - 1] = (Block)Blocks.field_150349_c;
                                                            }
                                                            if (grassMycelium == 2) {
                                                                par5ArrayOfBlock[var44 - 1] = (Block)Blocks.field_150391_bh;
                                                            }
                                                        }
                                                        final Block fallingBlock = par5ArrayOfBlock[var44 + 1];
                                                        if (fallingBlock != Blocks.field_150354_m) {
                                                            if (fallingBlock == Blocks.field_150351_n) {
                                                                par5ArrayOfBlock[var44 + 1] = Blocks.field_150348_b;
                                                            }
                                                        }
                                                        else if ((biome < 36 || biome > 39) && (biome < 164 || biome > 167)) {
                                                            par5ArrayOfBlock[var44 + 1] = Blocks.field_150322_A;
                                                        }
                                                        else {
                                                            par5ArrayOfBlock[var44 + 1] = Blocks.field_150406_ce;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        --var44;
                                        --yIndex;
                                    }
                                }
                            }
                        }
                        if (var25) {
                            break;
                        }
                    }
                }
                ++par15;
            }
        }
    }

    protected static boolean validGiantCaveLocation(final int varChunkX, final int varChunkZ) {
        final int chunkModX = varChunkX & 0xF;
        final int chunkModZ = varChunkZ & 0xF;
        if ((chunkModX != 0 || chunkModZ != 0) && (chunkModX != 8 || chunkModZ != 8)) {
            return false;
        }
        if (varChunkX * varChunkX + varChunkZ * varChunkZ <= 128) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed(varChunkX * MysteriumPatchesFixesOld.seedX ^ varChunkZ * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) <= MysteriumPatchesFixesOld.colossalCaveChance / 2) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 16) * MysteriumPatchesFixesOld.seedX ^ varChunkZ * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 16) * MysteriumPatchesFixesOld.seedX ^ varChunkZ * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed(varChunkX * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 16) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed(varChunkX * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 16) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 8) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 8) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 8) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 8) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 8) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 8) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 8) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 8) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 24) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 8) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 16) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 16) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 8) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 24) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 24) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 8) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 16) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 16) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 8) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ - 24) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 24) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 8) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 16) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 16) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX - 8) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 24) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 24) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 8) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 16) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 16) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed((varChunkX + 8) * MysteriumPatchesFixesOld.seedX ^ (varChunkZ + 24) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        if (MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        MysteriumPatchesFixesOld.b.setSeed(varChunkX * MysteriumPatchesFixesOld.seedX ^ varChunkZ * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
        final int tmp = MysteriumPatchesFixesOld.b.nextInt(2) + MysteriumPatchesFixesOld.b.nextInt(2) + MysteriumPatchesFixesOld.b.nextInt(2);
        return true;
    }

    protected static boolean validCaveLocation(final int chunkX, final int chunkZ) {
        int caveCount = 0;
        MysteriumPatchesFixesOld.genCaves = true;
        MysteriumPatchesFixesOld.genFillerCaves = true;
        for (int cx = -6; cx <= 6; ++cx) {
            for (int cz = -6; cz <= 6; ++cz) {
                if (cx * cx + cz * cz <= 36 && MysteriumPatchesFixesOld.genCaves) {
                    if (validGiantCaveLocation(chunkX + cx, chunkZ + cz)) {
                        MysteriumPatchesFixesOld.genCaves = false;
                        return MysteriumPatchesFixesOld.genFillerCaves = false;
                    }
                    if (MysteriumPatchesFixesOld.genFillerCaves) {
                        MysteriumPatchesFixesOld.b.setSeed((chunkX + cx) * MysteriumPatchesFixesOld.seedX ^ (chunkZ + cz) * MysteriumPatchesFixesOld.seedZ ^ MysteriumPatchesFixesOld.worldSeed);
                        final int size = MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(40) + 1) + 1);
                        if (MysteriumPatchesFixesOld.b.nextInt(15) == 0) {
                            caveCount += size;
                        }
                        if (caveCount > 1) {
                            MysteriumPatchesFixesOld.genFillerCaves = false;
                        }
                    }
                }
            }
        }
        return true;
    }

    protected static void generateRavine(final long par1, final int par3, final int par4, final Block[] par5ArrayOfBlock, double par6, double par8, double par10, float par12, float par13, float par14, double height, final int bigRavine) {
        final Random var19 = new Random(par1);
        MysteriumPatchesFixesOld.noiseGen.setSeed(par1);
        final double var20 = par3 * 16 + 8;
        final double var21 = par4 * 16 + 8;
        float var22 = 0.0f;
        float var23 = 0.0f;
        float curviness = 0.05f;
        int par15 = 112 - var19.nextInt(28);
        if (bigRavine > 0) {
            par15 = 112 + MysteriumPatchesFixesOld.noiseGen.nextInt(MysteriumPatchesFixesOld.noiseGen.nextInt(225) + 1);
            par12 *= MysteriumPatchesFixesOld.noiseGen.nextFloat() * MysteriumPatchesFixesOld.noiseGen.nextFloat() * 1.5f + 1.0f;
            if (bigRavine == 2) {
                par15 += 100 + MysteriumPatchesFixesOld.noiseGen.nextInt(61);
                if (par15 > 336) {
                    par15 = 336;
                }
                par12 += 3.0f + MysteriumPatchesFixesOld.noiseGen.nextFloat() * 2.0f;
                if (par12 > 15.0f) {
                    par12 = 15.0f;
                }
            }
            curviness = par15 / 2240.0f;
        }
        float var24 = 1.0f;
        for (int skipCount = 0; skipCount < 128; ++skipCount) {
            if (skipCount == 0 || var19.nextInt(3) == 0) {
                var24 += var19.nextFloat() * var19.nextFloat();
            }
        }
        int skipCount = 5;
        float ravineDataMultiplier = 1.1f - (par12 - 2.0f) * 0.07f;
        if (ravineDataMultiplier < 0.6f) {
            ravineDataMultiplier = 0.6f;
        }
        for (int par16 = 0; par16 < 128; ++par16) {
            if (++skipCount > 1 && (skipCount > 3 || MysteriumPatchesFixesOld.noiseGen.nextInt(3) == 0)) {
                skipCount = 0;
                var24 = (1.0f + MysteriumPatchesFixesOld.noiseGen.nextFloat() * MysteriumPatchesFixesOld.noiseGen.nextFloat() * ravineDataMultiplier) * (0.95f + MysteriumPatchesFixesOld.noiseGen.nextInt(2) * 0.1f);
            }
            MysteriumPatchesFixesOld.ravineData[par16] = var24 * var24;
        }
        skipCount = 999;
        for (int par16 = 0; par16 < par15; ++par16) {
            double var25 = 1.5 + MathHelper.func_76126_a(par16 * 3.141593f / par15) * par12;
            if (bigRavine > 0) {
                height = 3.416667 - var25 / 18.0;
                if (height > 3.0) {
                    height = 3.0;
                }
            }
            double var26 = var25 * height;
            var25 *= var19.nextFloat() * 0.25 + 0.75;
            var26 *= var19.nextFloat() * 0.25 + 0.75;
            final float var27 = MathHelper.func_76134_b(par14);
            final float var28 = MathHelper.func_76126_a(par14);
            par6 += MathHelper.func_76134_b(par13) * var27;
            par8 += var28;
            par10 += MathHelper.func_76126_a(par13) * var27;
            par14 *= 0.7f;
            par14 += var23 * curviness;
            par13 += var22 * curviness;
            var23 *= 0.8f;
            var22 *= 0.5f;
            var23 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0f;
            var22 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0f;
            if (var19.nextInt(4) != 0 && skipCount < (int)var25 / 2) {
                ++skipCount;
            }
            else {
                final double var29 = par6 - var20;
                final double var30 = par10 - var21;
                final double var31 = par15 - par16 + (double)(par12 + 18.0f);
                if (var29 * var29 + var30 * var30 > var31 * var31) {
                    return;
                }
                skipCount = 0;
                final double noiseMultiplier = 0.3333333 / (var25 - 0.5);
                final double var53_2 = var25 * 2.0;
                if (par6 >= var20 - 16.0 - var53_2 && par10 >= var21 - 16.0 - var53_2 && par6 <= var20 + 16.0 + var53_2 && par10 <= var21 + 16.0 + var53_2) {
                    int var32 = MathHelper.func_76128_c(par6 - var25) - par3 * 16 - 1;
                    int var33 = MathHelper.func_76128_c(par6 + var25) - par3 * 16 + 1;
                    int var34 = MathHelper.func_76128_c(par8 - var26) - 1;
                    int var35 = MathHelper.func_76128_c(par8 + var26) + 1;
                    int var36 = MathHelper.func_76128_c(par10 - var25) - par4 * 16 - 1;
                    int var37 = MathHelper.func_76128_c(par10 + var25) - par4 * 16 + 1;
                    if (var32 < 0) {
                        var32 = 0;
                    }
                    if (var33 > 16) {
                        var33 = 16;
                    }
                    if (var34 < 0) {
                        var34 = 0;
                    }
                    if (var35 > 120) {
                        var35 = 120;
                    }
                    if (var36 < 0) {
                        var36 = 0;
                    }
                    if (var37 > 16) {
                        var37 = 16;
                    }
                    for (int var38 = var32; var38 < var33; ++var38) {
                        double var39 = (var38 + par3 * 16 + 0.5 - par6) / var25;
                        var39 *= var39;
                        for (int var40 = var36; var40 < var37; ++var40) {
                            double var41 = (var40 + par4 * 16 + 0.5 - par10) / var25;
                            var41 = var41 * var41 + var39;
                            int var42 = var38 << 12 | var40 << 8 | var35;
                            int yIndex = var35;
                            int grassMycelium = 0;
                            if (var41 < 1.0) {
                                for (int var43 = var35 - 1; var43 >= var34; --var43) {
                                    final double var44 = (var43 + 0.5 - par8) / var26;
                                    if (var41 * MysteriumPatchesFixesOld.ravineData[var43] + var44 * var44 / 6.0 + (MysteriumPatchesFixesOld.noiseGen.nextInt(3) - 1) * noiseMultiplier < 1.0 && waterCheck(var38, yIndex, var40, par5ArrayOfBlock)) {
                                        final Block var45 = par5ArrayOfBlock[var42];
                                        if (var45 != null && var45 != Blocks.field_150357_h) {
                                            final int biome = MysteriumPatchesFixesOld.world.getBiomeGenForCoordsBody(var38 + par3 * 16, var40 + par4 * 16).field_76756_M;
                                            if (var44 < 60.0 || biome != 16) {
                                                if (var45 == Blocks.field_150349_c) {
                                                    grassMycelium = 1;
                                                }
                                                if (var45 == Blocks.field_150391_bh) {
                                                    grassMycelium = 2;
                                                }
                                                if (var43 < 10) {
                                                    par5ArrayOfBlock[var42] = Blocks.field_150353_l;
                                                }
                                                else {
                                                    par5ArrayOfBlock[var42] = null;
                                                    if (grassMycelium > 0 && par5ArrayOfBlock[var42 - 1] == Blocks.field_150346_d) {
                                                        if (grassMycelium == 1) {
                                                            par5ArrayOfBlock[var42 - 1] = (Block)Blocks.field_150349_c;
                                                        }
                                                        if (grassMycelium == 2) {
                                                            par5ArrayOfBlock[var42 - 1] = (Block)Blocks.field_150391_bh;
                                                        }
                                                    }
                                                    final Block fallingBlock = par5ArrayOfBlock[var42 + 1];
                                                    if (fallingBlock != Blocks.field_150354_m) {
                                                        if (fallingBlock == Blocks.field_150351_n) {
                                                            par5ArrayOfBlock[var42 + 1] = Blocks.field_150346_d;
                                                        }
                                                    }
                                                    else if ((biome < 36 || biome > 39) && (biome < 164 || biome > 167)) {
                                                        par5ArrayOfBlock[var42 + 1] = Blocks.field_150322_A;
                                                    }
                                                    else {
                                                        par5ArrayOfBlock[var42 + 1] = Blocks.field_150406_ce;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    --var42;
                                    --yIndex;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE, booleanAlwaysReturned = false)
    public static boolean generate(final WorldGenLiquids l, final World world, final Random random, final int x, final int y, final int z) {
        final boolean type = l.field_150521_a.func_149688_o() == Material.field_151586_h;
        return (WGConfig.disableSourceWater && type && (!WGConfig.disableSourceUnderground || y < 64)) || (WGConfig.disableSourceLava && !type && (!WGConfig.disableSourceUnderground || y < 64));
    }

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static void populate(final ChunkProviderGenerate g, final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        boolean flag = false;
        final World world = (World)DimensionManager.getWorld(0);
        if (world != null) {
            BlockFalling.field_149832_M = true;
            final BiomeGenBase biomegenbase = world.getBiomeGenForCoordsBody(k + 16, l + 16);
            MysteriumPatchesFixesOld.newRand.setSeed(world.func_72905_C());
            final long i1 = MysteriumPatchesFixesOld.newRand.nextLong() / 2L * 2L + 1L;
            final long j1 = MysteriumPatchesFixesOld.newRand.nextLong() / 2L * 2L + 1L;
            MysteriumPatchesFixesOld.newRand.setSeed(p_73153_2_ * i1 + p_73153_3_ * j1 ^ world.func_72905_C());
            MinecraftForge.EVENT_BUS.post((Event)new PopulateChunkEvent.Pre(p_73153_1_, world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_, flag));
            if (world.func_72912_H().func_76089_r()) {
                if (WGConfig.enableMineshaftSpawn) {
                    MysteriumPatchesFixesOld.mineshaftGenerator.func_75051_a(world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_);
                }
                flag = (WGConfig.enableVillageSpawn && MysteriumPatchesFixesOld.villageGenerator.func_75051_a(world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_));
                if (WGConfig.enableStrongholdSpawn) {
                    MysteriumPatchesFixesOld.strongholdGenerator.func_75051_a(world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_);
                }
                if (!WGConfig.enableDesolateSpawn) {
                    MysteriumPatchesFixesOld.scatteredFeatureGenerator.func_75051_a(world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_);
                }
            }
            if (biomegenbase != BiomeGenBase.field_76769_d && biomegenbase != BiomeGenBase.field_76786_s && !flag && MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.waterLakesChance) == 0 && TerrainGen.populate(p_73153_1_, world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAKE)) {
                int k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                int l2 = MysteriumPatchesFixesOld.newRand.nextInt(256);
                int i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                if (MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.waterLakesChance) == 0) {
                    k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                    l2 = MysteriumPatchesFixesOld.newRand.nextInt(256);
                    i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                    new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
                if (MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.waterLakesChance) == 0) {
                    k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                    l2 = MysteriumPatchesFixesOld.newRand.nextInt(256);
                    i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                    new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
            }
            if (TerrainGen.populate(p_73153_1_, world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAVA) && !flag) {
                int k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                int l2 = MysteriumPatchesFixesOld.newRand.nextInt(MysteriumPatchesFixesOld.newRand.nextInt(248) + 8);
                int i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                if (l2 < 63 || MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.lavaLakesChance) == 0) {
                    new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
                else if (MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
                    new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
                k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                l2 = MysteriumPatchesFixesOld.newRand.nextInt(MysteriumPatchesFixesOld.newRand.nextInt(248) + 8);
                i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                if (l2 < 63 || MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.lavaLakesChance) == 0) {
                    new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
                else if (MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
                    new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
                k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                l2 = MysteriumPatchesFixesOld.newRand.nextInt(MysteriumPatchesFixesOld.newRand.nextInt(248) + 8);
                i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                if (l2 < 63 || MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.lavaLakesChance) == 0) {
                    new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
                else if (MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
                    new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
            }
            if (TerrainGen.populate(p_73153_1_, world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAVA) && MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
                int k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                int l2 = MysteriumPatchesFixesOld.newRand.nextInt(MysteriumPatchesFixesOld.newRand.nextInt(248) + 8);
                int i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                if (l2 < 63) {
                    new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                }
                if (MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
                    k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                    l2 = MysteriumPatchesFixesOld.newRand.nextInt(MysteriumPatchesFixesOld.newRand.nextInt(248) + 8);
                    i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                    if (l2 < 63) {
                        new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                    }
                }
                if (MysteriumPatchesFixesOld.newRand.nextInt(WGConfig.undergWaterLakesChance) == 0) {
                    k2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                    l2 = MysteriumPatchesFixesOld.newRand.nextInt(MysteriumPatchesFixesOld.newRand.nextInt(248) + 8);
                    i2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                    if (l2 < 63) {
                        new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, MysteriumPatchesFixesOld.newRand, k2, l2, i2);
                    }
                }
            }
            int k2;
            int l2;
            int i2;
            boolean doGen;
            int j2;
            for (doGen = TerrainGen.populate(p_73153_1_, world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.DUNGEON), k2 = 0; doGen && k2 < 8; ++k2) {
                l2 = k + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                i2 = MysteriumPatchesFixesOld.newRand.nextInt(256);
                j2 = l + MysteriumPatchesFixesOld.newRand.nextInt(16) + 8;
                if (WGConfig.enableDungeonSpawn) {
                    new WorldGenDungeons().func_76484_a(world, MysteriumPatchesFixesOld.newRand, l2, i2, j2);
                }
            }
            biomegenbase.func_76728_a(world, MysteriumPatchesFixesOld.newRand, k, l);
            if (TerrainGen.populate(p_73153_1_, world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.ANIMALS)) {
                SpawnerAnimals.func_77191_a(world, biomegenbase, k + 8, l + 8, 16, 16, MysteriumPatchesFixesOld.newRand);
            }
            k += 8;
            l += 8;
            for (doGen = TerrainGen.populate(p_73153_1_, world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.ICE), k2 = 0; doGen && k2 < 16; ++k2) {
                for (int l2 = 0; l2 < 16; ++l2) {
                    i2 = world.func_72874_g(k + k2, l + l2);
                    if (world.func_72884_u(k2 + k, i2 - 1, l2 + l)) {
                        world.func_147465_d(k2 + k, i2 - 1, l2 + l, Blocks.field_150432_aD, 0, 2);
                    }
                    if (world.func_147478_e(k2 + k, i2, l2 + l, true)) {
                        world.func_147465_d(k2 + k, i2, l2 + l, Blocks.field_150431_aC, 0, 2);
                    }
                }
            }
            MinecraftForge.EVENT_BUS.post((Event)new PopulateChunkEvent.Post(p_73153_1_, world, MysteriumPatchesFixesOld.newRand, p_73153_2_, p_73153_3_, flag));
            BlockFalling.field_149832_M = false;
        }
    }

    protected static void generateColossalCaves(final int varChunkX, final int varChunkZ, final int chunkX, final int chunkZ, final Block[] par6ArrayOfBlock) {
        if (validGiantCaveLocation(varChunkX, varChunkZ)) {
            final int centerX = varChunkX * 16 + 8;
            final int centerZ = varChunkZ * 16 + 8;
            int subCenterX = 0;
            int subCenterZ = 0;
            final int caveType = MysteriumPatchesFixesOld.b.nextInt(4);
            for (int caveSystemCount = 0; caveSystemCount < 8; ++caveSystemCount) {
                switch (caveType) {
                    case 0: {
                        subCenterX = centerX + MysteriumPatchesFixesOld.b.nextInt(33) - 40;
                        subCenterZ = centerZ + caveSystemCount * 15 + MysteriumPatchesFixesOld.b.nextInt(17) - 84;
                        break;
                    }
                    case 1: {
                        subCenterX = centerX + caveSystemCount * 15 + MysteriumPatchesFixesOld.b.nextInt(17) - 84;
                        subCenterZ = centerZ + MysteriumPatchesFixesOld.b.nextInt(33) - 40;
                        break;
                    }
                    case 2: {
                        subCenterX = centerX - caveSystemCount * 15 + MysteriumPatchesFixesOld.b.nextInt(17) + 20;
                        subCenterZ = centerZ + caveSystemCount * 15 + MysteriumPatchesFixesOld.b.nextInt(17) - 84;
                        break;
                    }
                    case 3: {
                        subCenterX = centerX + caveSystemCount * 15 + MysteriumPatchesFixesOld.b.nextInt(17) - 84;
                        subCenterZ = centerZ + caveSystemCount * 15 + MysteriumPatchesFixesOld.b.nextInt(17) - 84;
                        break;
                    }
                }
                generateCaveSystem(20, subCenterX, subCenterZ, chunkX, chunkZ, par6ArrayOfBlock, 48);
                for (int i = 0; i < 5; ++i) {
                    generateCaveNode(MysteriumPatchesFixesOld.b.nextLong(), chunkX, chunkZ, par6ArrayOfBlock, subCenterX + MysteriumPatchesFixesOld.b.nextInt(48), MysteriumPatchesFixesOld.b.nextInt(6) + 10, subCenterZ + MysteriumPatchesFixesOld.b.nextInt(48), MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * 6.0f, MysteriumPatchesFixesOld.b.nextFloat() * 6.283185f, (MysteriumPatchesFixesOld.b.nextFloat() - 0.5f) / 4.0f, 0, 0, 1.0, 0);
                }
            }
        }
    }

    protected static void generateCaves(final int par2, final int par3, final int par4, final int par5, final Block[] par6ArrayOfBlock) {
        MysteriumPatchesFixesOld.b.setSeed(MysteriumPatchesFixesOld.chunkSeed);
        int caveSize = MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(40) + 1) + 1);
        final int genCave = MysteriumPatchesFixesOld.b.nextInt(15);
        if (genCave == 0 && MysteriumPatchesFixesOld.genCaves) {
            generateCaveSystem(caveSize, par2 * 16, par3 * 16, par4, par5, par6ArrayOfBlock, 16);
            if (caveSize > 4 && caveSize < 20) {
                for (int caveCount = MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(caveSize / 5 * 2 + 2) + 1) + 1), cx = 0; cx < caveCount; ++cx) {
                    int cz = 1;
                    if (MysteriumPatchesFixesOld.b.nextInt(4) == 0) {
                        cz += MysteriumPatchesFixesOld.b.nextInt(4);
                    }
                    for (int LL = 0; LL < cz; ++LL) {
                        generateCaveNode(MysteriumPatchesFixesOld.b.nextLong(), par4, par5, par6ArrayOfBlock, par2 * 16 + MysteriumPatchesFixesOld.b.nextInt(16), MysteriumPatchesFixesOld.b.nextInt(6) + 10, par3 * 16 + MysteriumPatchesFixesOld.b.nextInt(16), MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * 6.0f, MysteriumPatchesFixesOld.b.nextFloat() * 6.283185f, (MysteriumPatchesFixesOld.b.nextFloat() - 0.5f) / 4.0f, 0, 0, 1.0, 0);
                    }
                }
            }
            if (((par2 % 18 == 0 && par3 % 18 == 0) || (par2 % 18 == 9 && par3 % 18 == 9)) && par2 * par2 + par3 * par3 >= 1024) {
                generateCaveNode(MysteriumPatchesFixesOld.b.nextLong(), par4, par5, par6ArrayOfBlock, par2 * 16 + 8, MysteriumPatchesFixesOld.b.nextInt(11) + 20, par3 * 16 + 8, MysteriumPatchesFixesOld.b.nextFloat() * 12.0f + MysteriumPatchesFixesOld.b.nextFloat() * 6.0f + 3.0f, MysteriumPatchesFixesOld.b.nextFloat() * 6.283185f, (MysteriumPatchesFixesOld.b.nextFloat() - 0.5f) / 4.0f, 0, 0, 1.0, 2);
            }
            else if (caveSize > 4 && caveSize < 15 && MysteriumPatchesFixesOld.b.nextBoolean()) {
                generateCaveNode(MysteriumPatchesFixesOld.b.nextLong(), par4, par5, par6ArrayOfBlock, par2 * 16 + 8, MysteriumPatchesFixesOld.b.nextInt(11) + 20, par3 * 16 + 8, MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * 8.0f + 2.0f, MysteriumPatchesFixesOld.b.nextFloat() * 6.283185f, (MysteriumPatchesFixesOld.b.nextFloat() - 0.5f) / 4.0f, 0, 0, 1.0, 1);
            }
        }
        else if (genCave == 5 || genCave == 10) {
            final int caveCount = MysteriumPatchesFixesOld.world.getBiomeGenForCoordsBody(par2 * 16 + 8, par3 * 16 + 8).field_76756_M;
            caveSize = 0;
            if (caveCount != 3 && caveCount != 34 && caveCount != 131 && caveCount != 162) {
                if ((caveCount < 36 || caveCount > 39) && (caveCount < 164 || caveCount > 167)) {
                    if (caveCount == 17) {
                        caveSize = 5;
                    }
                }
                else {
                    caveSize = 10;
                }
            }
            else {
                caveSize = 15;
            }
            if (!MysteriumPatchesFixesOld.genCaves) {
                caveSize /= 2;
            }
            if (caveSize > 0) {
                for (int cx = MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(caveSize) + 1), cz = 0; cz < cx; ++cz) {
                    generateCaveNode(MysteriumPatchesFixesOld.b.nextLong(), par4, par5, par6ArrayOfBlock, par2 * 16 + MysteriumPatchesFixesOld.b.nextInt(16), MysteriumPatchesFixesOld.b.nextInt(50) + 40, par3 * 16 + MysteriumPatchesFixesOld.b.nextInt(16), MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * 6.0f, MysteriumPatchesFixesOld.b.nextFloat() * 6.283185f, (MysteriumPatchesFixesOld.b.nextFloat() - 0.5f) / 4.0f, 0, 0, 1.0, 0);
                }
            }
        }
        if (MysteriumPatchesFixesOld.genFillerCaves) {
            int caveCount = 0;
            for (int cx = -1; cx <= 1; ++cx) {
                for (int cz = -1; cz <= 1; ++cz) {
                    if (cx != 0 && cz != 0) {
                        validCaveLocation(par2 + cx, par3 + cz);
                        if (MysteriumPatchesFixesOld.genFillerCaves) {
                            ++caveCount;
                        }
                    }
                }
            }
            if (caveCount == 0) {
                caveCount = 1;
            }
            else if (caveCount > 2) {
                caveCount = 8;
            }
            MysteriumPatchesFixesOld.b.setSeed(MysteriumPatchesFixesOld.chunkSeed);
            if (MysteriumPatchesFixesOld.b.nextInt(caveCount) == 0) {
                generateCaveSystem(MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(9) + 1) + 3, par2 * 16, par3 * 16, par4, par5, par6ArrayOfBlock, 16);
                if (MysteriumPatchesFixesOld.b.nextBoolean()) {
                    generateCaveNode(MysteriumPatchesFixesOld.b.nextLong(), par4, par5, par6ArrayOfBlock, par2 * 16 + MysteriumPatchesFixesOld.b.nextInt(16), MysteriumPatchesFixesOld.b.nextInt(6) + 10, par3 * 16 + MysteriumPatchesFixesOld.b.nextInt(16), MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * 6.0f, MysteriumPatchesFixesOld.b.nextFloat() * 6.283185f, (MysteriumPatchesFixesOld.b.nextFloat() - 0.5f) / 4.0f, 0, 0, 1.0, 0);
                }
                if (MysteriumPatchesFixesOld.b.nextInt(4) == 0) {
                    generateCaveNode(MysteriumPatchesFixesOld.b.nextLong(), par4, par5, par6ArrayOfBlock, par2 * 16 + 8, MysteriumPatchesFixesOld.b.nextInt(11) + 20, par3 * 16 + 8, MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * 8.0f + 2.0f, MysteriumPatchesFixesOld.b.nextFloat() * 6.283185f, (MysteriumPatchesFixesOld.b.nextFloat() - 0.5f) / 4.0f, 0, 0, 1.0, 1);
                }
            }
        }
    }

    protected static void generateCaveSystem(final int numberOfCaves, final int par2, final int par3, final int par4, final int par5, final Block[] par6ArrayOfBlock, final int spread) {
        for (int var8 = 0; var8 < numberOfCaves; ++var8) {
            final double var9 = par2 + MysteriumPatchesFixesOld.b.nextInt(spread);
            final double var10 = MysteriumPatchesFixesOld.b.nextInt(MysteriumPatchesFixesOld.b.nextInt(120) + 8);
            final double var11 = par3 + MysteriumPatchesFixesOld.b.nextInt(spread);
            int var12 = 1;
            if (MysteriumPatchesFixesOld.b.nextInt(4) == 0) {
                generateLargeCaveNode(MysteriumPatchesFixesOld.b.nextLong(), par4, par5, par6ArrayOfBlock, var9, var10, var11);
                var12 += MysteriumPatchesFixesOld.b.nextInt(4);
            }
            for (int var13 = 0; var13 < var12; ++var13) {
                final float var14 = MysteriumPatchesFixesOld.b.nextFloat() * 3.1415927f * 2.0f;
                final float var15 = (MysteriumPatchesFixesOld.b.nextFloat() - 0.5f) / 4.0f;
                float var16 = MysteriumPatchesFixesOld.b.nextFloat() * 2.0f + MysteriumPatchesFixesOld.b.nextFloat();
                if (MysteriumPatchesFixesOld.b.nextInt(10) == 0) {
                    var16 *= MysteriumPatchesFixesOld.b.nextFloat() * MysteriumPatchesFixesOld.b.nextFloat() * 4.0f + 1.0f;
                }
                generateCaveNode(MysteriumPatchesFixesOld.b.nextLong(), par4, par5, par6ArrayOfBlock, var9, var10, var11, var16, var14, var15, 0, 0, 1.0, 0);
            }
        }
    }

    protected static void generateRavines(final int par2, final int par3, final int par4, final int par5, final Block[] par6ArrayOfBlock) {
        ImmersiveCavegen.rand.setSeed(MysteriumPatchesFixesOld.chunkSeed);
        if (ImmersiveCavegen.rand.nextInt(50) == 0) {
            final double var7 = par2 * 16 + ImmersiveCavegen.rand.nextInt(16);
            double var8 = ImmersiveCavegen.rand.nextInt(ImmersiveCavegen.rand.nextInt(40) + 8) + 20;
            final double var9 = par3 * 16 + ImmersiveCavegen.rand.nextInt(16);
            final float var10 = ImmersiveCavegen.rand.nextFloat() * 3.1415927f * 2.0f;
            final float var11 = (ImmersiveCavegen.rand.nextFloat() - 0.5f) / 4.0f;
            final float var12 = (ImmersiveCavegen.rand.nextFloat() * 2.0f + ImmersiveCavegen.rand.nextFloat()) * 2.0f;
            final long ravineSeed = ImmersiveCavegen.rand.nextLong();
            int bigRavine = 0;
            double height = 3.0;
            if (ravineSeed % 50L == 0L && par2 * par2 + par3 * par3 >= 1024) {
                bigRavine = 2;
            }
            else if (ravineSeed % 8L == 0L) {
                bigRavine = 1;
            }
            else {
                final int biome = MysteriumPatchesFixesOld.world.getBiomeGenForCoordsBody(par2 * 16 + 8, par3 * 16 + 8).field_76756_M;
                if ((biome >= 36 && biome <= 39) || (biome >= 164 && biome <= 167)) {
                    if (var8 < 40.0) {
                        var8 += ImmersiveCavegen.rand.nextInt(16);
                    }
                    height += ImmersiveCavegen.rand.nextInt(2) + 1;
                }
            }
            generateRavine(ravineSeed, par4, par5, par6ArrayOfBlock, var7, var8, var9, var12, var10, var11, height, bigRavine);
        }
    }

    public static boolean waterCheck(final int blockX, final int blockY, final int blockZ, final Block[] blockData) {
        if (blockY >= 25 && blockY <= 62) {
            for (int x = blockX - 1; x <= blockX + 1; ++x) {
                if (x >= 0 && x <= 15) {
                    for (int z = blockZ - 1; z <= blockZ + 1; ++z) {
                        if (z >= 0 && z <= 15) {
                            final int xyz = x << 12 | z << 8;
                            for (int y = blockY - 1; y <= blockY + 1; ++y) {
                                if (blockData[xyz + y] == Blocks.field_150355_j) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            for (int x = blockZ - 1; x <= blockZ + 1; ++x) {
                if (x >= 0 && x <= 15) {
                    int xyz = blockX - 2;
                    if (xyz >= 0 && blockData[xyz << 12 | x << 8 | blockY] == Blocks.field_150355_j) {
                        return false;
                    }
                    xyz = blockX + 2;
                    if (xyz <= 15 && blockData[xyz << 12 | x << 8 | blockY] == Blocks.field_150355_j) {
                        return false;
                    }
                }
            }
            for (int x = blockX - 1; x <= blockX + 1; ++x) {
                if (x >= 0 && x <= 15) {
                    int xyz = blockZ - 2;
                    if (xyz >= 0 && blockData[x << 12 | xyz << 8 | blockY] == Blocks.field_150355_j) {
                        return false;
                    }
                    xyz = blockZ + 2;
                    if (xyz <= 15 && blockData[x << 12 | xyz << 8 | blockY] == Blocks.field_150355_j) {
                        return false;
                    }
                }
            }
            if (blockData[blockX << 12 | blockZ << 8 | blockY - 2] == Blocks.field_150355_j) {
                return false;
            }
            if (blockData[blockX << 12 | blockZ << 8 | blockY + 2] == Blocks.field_150355_j) {
                return false;
            }
        }
        return true;
    }

    public static boolean validGiantCaveLocation(final int varChunkX, final int varChunkZ, final long seedX, final long seedZ, final long worldSeed) {
        final int chunkModX = varChunkX & 0xF;
        final int chunkModZ = varChunkZ & 0xF;
        if ((chunkModX != 0 || chunkModZ != 0) && (chunkModX != 8 || chunkModZ != 8)) {
            return false;
        }
        if (varChunkX * varChunkX + varChunkZ * varChunkZ <= 512) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed(varChunkX * seedX ^ varChunkZ * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) != 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 16) * seedX ^ varChunkZ * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 16) * seedX ^ varChunkZ * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed(varChunkX * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed(varChunkX * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 8) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 8) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 8) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 8) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 24) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 16) * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 8) * seedX ^ (varChunkZ - 24) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 24) * seedX ^ (varChunkZ - 8) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 16) * seedX ^ (varChunkZ - 16) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 8) * seedX ^ (varChunkZ - 24) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 24) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 16) * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX - 8) * seedX ^ (varChunkZ + 24) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 24) * seedX ^ (varChunkZ + 8) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 16) * seedX ^ (varChunkZ + 16) * seedZ ^ worldSeed);
        if (ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) == 0) {
            return false;
        }
        ImmersiveCavegen.rand.setSeed((varChunkX + 8) * seedX ^ (varChunkZ + 24) * seedZ ^ worldSeed);
        return ImmersiveCavegen.rand.nextInt(MysteriumPatchesFixesOld.colossalCaveChance) != 0;
    }

    public static boolean didSpawn(final MapGenMineshaft instance, final int chunkX, final int chunkZ) {
        if (((chunkX + 2000000) % 14 != 0 || (chunkZ + 2000000) % 14 != 0) && ((chunkX + 2000000) % 14 != 7 || (chunkZ + 2000000) % 14 != 7)) {
            return false;
        }
        final long worldSeed = instance.field_75039_c.func_72905_C();
        ImmersiveCavegen.rand.setSeed(worldSeed);
        final long seedX = ImmersiveCavegen.rand.nextLong();
        final long seedZ = ImmersiveCavegen.rand.nextLong();
        int caveCount = 0;
        for (int cx = -6; cx <= 6; ++cx) {
            for (int cz = -6; cz <= 6; ++cz) {
                if (cx * cx + cz * cz <= 36) {
                    ImmersiveCavegen.rand.setSeed((chunkX + cx) * seedX ^ (chunkZ + cz) * seedZ ^ worldSeed);
                    final int size = ImmersiveCavegen.rand.nextInt(ImmersiveCavegen.rand.nextInt(ImmersiveCavegen.rand.nextInt(40) + 1) + 1);
                    if (ImmersiveCavegen.rand.nextInt(15) == 0) {
                        caveCount += size;
                    }
                    if (caveCount > 33 || validGiantCaveLocation(chunkX + cx, chunkZ + cz, seedX, seedZ, worldSeed)) {
                        return false;
                    }
                }
            }
        }
        return caveCount >= 10 || (caveCount >= 5 && instance.field_75038_b.nextBoolean()) || instance.field_75038_b.nextInt(4) == 0;
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE, anotherMethodReturned = "didSpawn")
    public static boolean canSpawnStructureAtCoords(final MapGenMineshaft instance, final int chunkX, final int chunkZ) {
        return WGConfig.enableBetterMineshafts;
    }

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static boolean generate(final WorldGenSand instance, final World p_76484_1_, final Random p_76484_2_, final int p_76484_3_, final int p_76484_4_, final int p_76484_5_) {
        if (WGConfig.enableBetterSand) {
            if (p_76484_1_.func_147439_a(p_76484_3_, p_76484_4_, p_76484_5_).func_149688_o() != Material.field_151586_h) {
                return false;
            }
            final int var6 = p_76484_2_.nextInt(instance.field_76539_b - 2) + 2;
            final byte var7 = 2;
            for (int var8 = p_76484_3_ - var6; var8 <= p_76484_3_ + var6; ++var8) {
                for (int var9 = p_76484_5_ - var6; var9 <= p_76484_5_ + var6; ++var9) {
                    final int var10 = var8 - p_76484_3_;
                    final int var11 = var9 - p_76484_5_;
                    if (var10 * var10 + var11 * var11 <= var6 * var6) {
                        for (int var12 = p_76484_4_ - var7; var12 <= p_76484_4_ + var7; ++var12) {
                            final Block var13 = p_76484_1_.func_147439_a(var8, var12, var9);
                            if (var13 == Blocks.field_150346_d || var13 == Blocks.field_150349_c) {
                                if (p_76484_1_.func_147439_a(var8, var12 - 1, var9) != Blocks.field_150350_a) {
                                    p_76484_1_.func_147465_d(var8, var12, var9, instance.field_150517_a, 0, 2);
                                }
                                else if (instance.field_150517_a == Blocks.field_150354_m) {
                                    p_76484_1_.func_147465_d(var8, var12, var9, Blocks.field_150322_A, 0, 2);
                                }
                                else if (instance.field_150517_a == Blocks.field_150351_n) {
                                    p_76484_1_.func_147465_d(var8, var12, var9, Blocks.field_150348_b, 0, 2);
                                }
                                else {
                                    p_76484_1_.func_147465_d(var8, var12, var9, instance.field_150517_a, 0, 2);
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        else {
            if (p_76484_1_.func_147439_a(p_76484_3_, p_76484_4_, p_76484_5_).func_149688_o() != Material.field_151586_h) {
                return false;
            }
            final int l = p_76484_2_.nextInt(instance.field_76539_b - 2) + 2;
            final byte b0 = 2;
            for (int i1 = p_76484_3_ - l; i1 <= p_76484_3_ + l; ++i1) {
                for (int j1 = p_76484_5_ - l; j1 <= p_76484_5_ + l; ++j1) {
                    final int k1 = i1 - p_76484_3_;
                    final int l2 = j1 - p_76484_5_;
                    if (k1 * k1 + l2 * l2 <= l * l) {
                        for (int i2 = p_76484_4_ - b0; i2 <= p_76484_4_ + b0; ++i2) {
                            final Block block = p_76484_1_.func_147439_a(i1, i2, j1);
                            if (block == Blocks.field_150346_d || block == Blocks.field_150349_c) {
                                p_76484_1_.func_147465_d(i1, i2, j1, instance.field_150517_a, 0, 2);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    static {
        MysteriumPatchesFixesOld.noiseGen = new Random();
        MysteriumPatchesFixesOld.b = new Random();
        MysteriumPatchesFixesOld.ravineData = new float[128];
        MysteriumPatchesFixesOld.colossalCaveChance = 26 - WGConfig.giantCaveChance / 4;
        MysteriumPatchesFixesOld.genCaves = false;
        MysteriumPatchesFixesOld.genFillerCaves = false;
        MysteriumPatchesFixesOld.newRand = new Random();
        MysteriumPatchesFixesOld.strongholdGenerator = new MapGenStronghold();
        MysteriumPatchesFixesOld.villageGenerator = new MapGenVillage();
        MysteriumPatchesFixesOld.mineshaftGenerator = new MapGenMineshaft();
        MysteriumPatchesFixesOld.scatteredFeatureGenerator = new MapGenScatteredFeature();
    }
}
