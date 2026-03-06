package net.tclproject.immersivecavegen.world;

import net.tclproject.immersivecavegen.world.biomes.caves.*;
import net.minecraftforge.event.terraingen.*;
import net.tclproject.immersivecavegen.*;
import cpw.mods.fml.common.eventhandler.*;
import net.tclproject.mysteriumlib.asm.fixes.*;
import net.minecraftforge.common.*;
import net.minecraft.world.biome.*;
import ganymedes01.etfuturum.configuration.configs.*;
import cpw.mods.fml.common.*;
import net.minecraft.world.chunk.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.tclproject.immersivecavegen.blocks.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.material.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import java.util.*;

public class CavesDecorator
{
    public static List freezable;
    public static int maxGenHeight;
    public static int maxLength;
    private static int timesPerChunck;
    private static final GenerateJungleCaves jungleGen;
    private static final GenerateWaterCaves waterGen;
    private static final GenerateSandCaves sandGen;
    private static final GeneratePlainCaves plainGen;
    private static final GenerateIceCaves iceGen;
    private static final GenerateFireCaves fireGen;
    private static final GenerateLivingCaves livingGen;

    @SubscribeEvent
    public void decorate(final DecorateBiomeEvent.Post decorationEvent) {
        for (final String str : WGConfig.dimblacklist) {
            if (decorationEvent.world != null && String.valueOf(decorationEvent.world.field_73011_w.field_76574_g).equalsIgnoreCase(str)) {
                return;
            }
        }
        this.generate(decorationEvent.rand, decorationEvent.chunkX + 8, decorationEvent.chunkZ + 8, decorationEvent.world);
    }

    public void generate(final Random random, final int blockX, final int blockZ, final World world) {
        if (MysteriumPatchesFixesCave.oceanAvg == -1) {
            int addedTimes = 1;
            int result = 0;
            int interResult = 0;
            for (int i = 0; i < 32; ++i) {
                for (int j = 0; j < 32; ++j) {
                    final int Xcoord = blockX + i;
                    final int Zcoord = blockZ + j;
                    if (world.field_73011_w.field_76575_d && !WGConfig.netherCaves) {
                        return;
                    }
                    int Ycoord;
                    if (!world.field_73011_w.field_76575_d) {
                        for (Ycoord = Math.min(world.func_72976_f(Xcoord, Zcoord) - 1, random.nextInt(CavesDecorator.maxGenHeight)); Ycoord > 10 && (!GenerateStoneStalactite.blockWhiteList.contains(world.func_147439_a(Xcoord, Ycoord + 1, Zcoord)) || !world.func_147437_c(Xcoord, Ycoord, Zcoord)); --Ycoord) {}
                    }
                    else {
                        Ycoord = Math.min(world.func_72976_f(Xcoord, Zcoord) - 1, random.nextInt(CavesDecorator.maxGenHeight));
                    }
                    if (Ycoord > 10) {
                        interResult = getNumEmptyBlocks(world, Xcoord, Ycoord, Zcoord) / 2;
                        result += interResult;
                        if (interResult >= 3) {
                            ++addedTimes;
                        }
                    }
                }
            }
            int finalResult = result / addedTimes;
            finalResult = (MysteriumPatchesFixesCave.oceanAvg = ((finalResult >= 25) ? (25 - random.nextInt(10)) : ((finalResult <= 10) ? (10 + random.nextInt(7)) : finalResult)));
        }
        for (int k = 0; k < CavesDecorator.timesPerChunck; ++k) {
            final int Xcoord2 = blockX + random.nextInt(16);
            final int Zcoord2 = blockZ + random.nextInt(16);
            if (world.field_73011_w.field_76575_d && !WGConfig.netherCaves) {
                return;
            }
            int Ycoord2;
            if (!world.field_73011_w.field_76575_d) {
                for (Ycoord2 = Math.min(world.func_72976_f(Xcoord2, Zcoord2) - 1, random.nextInt(CavesDecorator.maxGenHeight)); Ycoord2 > 10 && (!GenerateStoneStalactite.blockWhiteList.contains(world.func_147439_a(Xcoord2, Ycoord2 + 1, Zcoord2)) || !world.func_147437_c(Xcoord2, Ycoord2, Zcoord2)); --Ycoord2) {}
            }
            else {
                Ycoord2 = Math.min(world.func_72976_f(Xcoord2, Zcoord2) - 1, random.nextInt(CavesDecorator.maxGenHeight));
            }
            if (Ycoord2 > 10 && random.nextFloat() < WGConfig.generationDensity) {
                final BiomeGenBase biome = world.func_72807_a(Xcoord2, Zcoord2);
                if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FROZEN) && WGConfig.icaves) {
                    CavesDecorator.iceGen.func_76484_a(world, random, Xcoord2, Ycoord2, Zcoord2);
                }
                else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SWAMP) && WGConfig.lcaves) {
                    CavesDecorator.livingGen.func_76484_a(world, random, Xcoord2, Ycoord2, Zcoord2);
                }
                else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.NETHER) || (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SPARSE) && WGConfig.fcaves)) {
                    CavesDecorator.fireGen.func_76484_a(world, random, Xcoord2, Ycoord2, Zcoord2);
                }
                else if (biome.field_76750_F > 1.5f && biome.field_76751_G < 0.1f && WGConfig.scaves) {
                    CavesDecorator.sandGen.func_76484_a(world, random, Xcoord2, Ycoord2, Zcoord2);
                }
                else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MUSHROOM) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE) || (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.DENSE) && WGConfig.jcaves)) {
                    CavesDecorator.jungleGen.func_76484_a(world, random, Xcoord2, Ycoord2, Zcoord2);
                }
                else if ((!biome.func_76736_e() && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.WATER)) || !WGConfig.wcaves) {
                    CavesDecorator.plainGen.func_76484_a(world, random, Xcoord2, Ycoord2, Zcoord2);
                }
                else {
                    CavesDecorator.waterGen.func_76484_a(world, random, Xcoord2, Ycoord2, Zcoord2);
                }
            }
        }
    }

    @Optional.Method(modid = "etfuturum")
    public static boolean isBlockDeepslate(final World world, final int x, final int y, final int z) {
        return y < ConfigWorld.deepslateMaxY && ConfigWorld.deepslateGenerationMode == 0;
    }

    public static boolean shouldGenerateStone(final World world, final int origX, final int origZ) {
        for (int p_72807_1_ = origX - 1; p_72807_1_ <= origX + 1; ++p_72807_1_) {
            if (p_72807_1_ != origX) {
                final int p_72807_2_ = origZ;
                if (world.func_72899_e(p_72807_1_, 0, p_72807_2_)) {
                    final Chunk chunk = world.func_72938_d(p_72807_1_, p_72807_2_);
                    try {
                        final BiomeGenBase b = chunk.func_76591_a(p_72807_1_ & 0xF, p_72807_2_ & 0xF, world.field_73011_w.field_76578_c);
                        int count = 0;
                        for (final String str : WGConfig.secondYLevelList) {
                            if (!b.field_76791_y.equalsIgnoreCase(str)) {
                                ++count;
                            }
                        }
                        if (count == WGConfig.secondYLevelList.length) {
                            return true;
                        }
                    }
                    catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                else {
                    final BiomeGenBase b2 = world.field_73011_w.field_76578_c.func_76935_a(p_72807_1_, p_72807_2_);
                    int count2 = 0;
                    for (final String str2 : WGConfig.secondYLevelList) {
                        if (!b2.field_76791_y.equalsIgnoreCase(str2)) {
                            ++count2;
                        }
                    }
                    if (count2 == WGConfig.secondYLevelList.length) {
                        return true;
                    }
                }
            }
        }
        for (int p_72807_2_2 = origZ - 1; p_72807_2_2 <= origZ + 1; ++p_72807_2_2) {
            if (p_72807_2_2 != origZ) {
                final int p_72807_1_2 = origX;
                if (world.func_72899_e(p_72807_1_2, 0, p_72807_2_2)) {
                    final Chunk chunk = world.func_72938_d(p_72807_1_2, p_72807_2_2);
                    try {
                        final BiomeGenBase b = chunk.func_76591_a(p_72807_1_2 & 0xF, p_72807_2_2 & 0xF, world.field_73011_w.field_76578_c);
                        int count = 0;
                        for (final String str : WGConfig.secondYLevelList) {
                            if (!b.field_76791_y.equalsIgnoreCase(str)) {
                                ++count;
                            }
                        }
                        if (count == WGConfig.secondYLevelList.length) {
                            return true;
                        }
                    }
                    catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                else {
                    final BiomeGenBase b2 = world.field_73011_w.field_76578_c.func_76935_a(p_72807_1_2, p_72807_2_2);
                    int count2 = 0;
                    for (final String str2 : WGConfig.secondYLevelList) {
                        if (!b2.field_76791_y.equalsIgnoreCase(str2)) {
                            ++count2;
                        }
                    }
                    if (count2 == WGConfig.secondYLevelList.length) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void generateFloodedCaves(final World world, final Random random, final int x, int y, final int z) {
        final int vary = getNumEmptyBlocks(world, x, y, z);
        if (vary != 0) {
            y = y - vary + 1;
        }
        if (world.func_147439_a(x, y - 1, z).func_149721_r() && WGConfig.waterCaves) {
            world.func_147465_d(x, y, z, (Block)Blocks.field_150358_i, 0, 3);
        }
    }

    public static void convertToSandType(final World world, final Random random, final int x, final int y, final int z) {
        final int height = random.nextInt(5) + 3;
        final int length = random.nextInt(5) + 3;
        final int width = random.nextInt(5) + 3;
        final int newX = x - length / 2;
        final int newY = y + height / 2;
        final int newZ = z - width / 2;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < length; ++j) {
                for (int k = 0; k < width; ++k) {
                    if (weightedChoice(0.7f, 0.3f, 0.0f, 0.0f, 0.0f, 0.0f) == 1) {
                        final IdentityHashMap sandEquivalent = new IdentityHashMap(8);
                        sandEquivalent.put(Blocks.field_150348_b, Blocks.field_150322_A);
                        sandEquivalent.put(Blocks.field_150346_d, Blocks.field_150354_m);
                        sandEquivalent.put(Blocks.field_150351_n, Blocks.field_150354_m);
                        final Block aux = sandEquivalent.get(world.func_147439_a(newX + j, newY - i, newZ + k));
                        if (aux != null) {
                            world.func_147465_d(newX + j, newY - i, newZ + k, aux, 0, 2);
                        }
                    }
                }
            }
        }
    }

    public static boolean generateGlowcaps(final World world, final Random random, final int x, int y, final int z) {
        final int vary = getNumEmptyBlocks(world, x, y, z);
        if (vary != 0) {
            y = y - vary + 1;
        }
        if (world.func_147439_a(x, y - 1, z).func_149662_c()) {
            final int glowcapNum = randomChoice(0, 1, 2, 3);
            world.func_147465_d(x, y, z, BlockInit.cavePlantBlock, glowcapNum, 3);
            return true;
        }
        return false;
    }

    public static boolean generateSmallMushrooms(final World world, final Random random, final int x, int y, final int z) {
        final int vary = getNumEmptyBlocks(world, x, y, z);
        if (vary != 0) {
            y = y - vary + 1;
        }
        if (world.func_147439_a(x, y - 1, z).func_149662_c()) {
            final int glowcapNum = randomChoice(0, 1);
            world.func_147465_d(x, y, z, (Block)((glowcapNum == 0) ? Blocks.field_150338_P : Blocks.field_150337_Q), 0, 2);
            return true;
        }
        return false;
    }

    public static boolean generateVegetation(final World world, final Random random, final int x, int y, final int z, final int vary) {
        if (vary != 0) {
            y = y - vary + 1;
        }
        if (world.func_147439_a(x, y - 1, z).func_149662_c()) {
            final int selection = randomChoice(0, 1, 2, 3);
            Block flowerblock = null;
            switch (selection) {
                case 0: {
                    flowerblock = (Block)Blocks.field_150328_O;
                    break;
                }
                case 1: {
                    flowerblock = (Block)Blocks.field_150327_N;
                    break;
                }
                case 2: {
                    flowerblock = (Block)Blocks.field_150330_I;
                    break;
                }
                case 3: {
                    flowerblock = (Block)Blocks.field_150329_H;
                    break;
                }
                default: {
                    flowerblock = (Block)Blocks.field_150329_H;
                    break;
                }
            }
            world.func_147465_d(x, y, z, flowerblock, 0, 2);
            if (random.nextFloat() > 0.5f || flowerblock != Blocks.field_150330_I) {
                world.func_147465_d(x, y - 1, z, Blocks.field_150346_d, 0, 2);
            }
            if (WGConfig.livingCavesLush) {
                convertToLushType(world, random, x, y, z);
            }
            return true;
        }
        return false;
    }

    public static void convertToLushType(final World world, final Random random, final int x, final int y, final int z) {
        final int height = random.nextInt(5) + 3;
        final int length = random.nextInt(10) + 3;
        final int width = random.nextInt(10) + 3;
        final int newX = x - length / 2;
        final int newY = y + height / 2;
        final int newZ = z - width / 2;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < length; ++j) {
                for (int k = 0; k < width; ++k) {
                    if (weightedChoice(0.8f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f) == 1 && ((j != length - 1 && j != 0) || (k != width - 1 && k != 0))) {
                        final Block aux = world.func_147439_a(newX + j, newY - i, newZ + k);
                        if (CavesDecorator.freezable.contains(aux)) {
                            if (world.func_147437_c(newX + j, newY - i + 1, newZ + k)) {
                                world.func_147465_d(newX + j, newY - i, newZ + k, (Block)Blocks.field_150349_c, 0, 2);
                                if (random.nextFloat() > 0.66f) {
                                    final int selection = randomChoice(0, 1, 2, 3, 4, 5);
                                    Block flowerblock = null;
                                    switch (selection) {
                                        case 0: {
                                            flowerblock = (Block)Blocks.field_150328_O;
                                            break;
                                        }
                                        case 1: {
                                            flowerblock = (Block)Blocks.field_150327_N;
                                            break;
                                        }
                                        case 2: {
                                            flowerblock = (Block)Blocks.field_150329_H;
                                            break;
                                        }
                                        default: {
                                            flowerblock = (Block)Blocks.field_150329_H;
                                            break;
                                        }
                                    }
                                    world.func_147465_d(newX + j, newY - i + 1, newZ + k, flowerblock, 1, 2);
                                }
                            }
                            else {
                                world.func_147465_d(newX + j, newY - i, newZ + k, Blocks.field_150346_d, 0, 2);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean generateTree(final World world, final Random random, final int x, int y, final int z, final int vary) {
        if (vary != 0) {
            y = y - vary + 1;
        }
        if (WGConfig.livingCavesLush) {
            convertToLushType(world, random, x, y, z);
        }
        if (world.func_147439_a(x, y - 1, z).func_149662_c()) {
            if (random.nextFloat() > WGConfig.treeChance) {
                world.func_147465_d(x, y, z, Blocks.field_150364_r, 0, 2);
                world.func_147465_d(x, y + 1, z, Blocks.field_150364_r, 0, 2);
                if (random.nextFloat() > 0.5) {
                    world.func_147465_d(x, y + 2, z, Blocks.field_150364_r, 0, 2);
                    if (random.nextFloat() > 0.8) {
                        world.func_147465_d(x, y + 3, z, Blocks.field_150364_r, 0, 2);
                        if (random.nextFloat() > 0.95) {
                            world.func_147465_d(x, y + 4, z, Blocks.field_150364_r, 0, 2);
                        }
                    }
                }
            }
            else {
                safeSetblock(world, x, y, z, Blocks.field_150364_r, 0, 2);
                safeSetblock(world, x, y + 1, z, Blocks.field_150364_r, 0, 2);
                surroundByLeaves(world, x, y + 1, z);
                if (random.nextFloat() > 0.5) {
                    safeSetblock(world, x, y + 2, z, Blocks.field_150364_r, 0, 2);
                    surroundByLeaves(world, x, y + 2, z);
                    if (random.nextFloat() > 0.7) {
                        safeSetblock(world, x, y + 3, z, Blocks.field_150364_r, 0, 2);
                        surroundByLeaves(world, x, y + 3, z);
                        if (random.nextFloat() > 0.85) {
                            safeSetblock(world, x, y + 4, z, Blocks.field_150364_r, 0, 2);
                            surroundByLeaves(world, x, y + 4, z);
                            safeSetblock(world, x, y + 5, z, (Block)Blocks.field_150362_t, 0, 2);
                        }
                        else {
                            safeSetblock(world, x, y + 4, z, (Block)Blocks.field_150362_t, 0, 2);
                        }
                    }
                    else {
                        safeSetblock(world, x, y + 3, z, (Block)Blocks.field_150362_t, 0, 2);
                    }
                }
                else {
                    safeSetblock(world, x, y + 2, z, (Block)Blocks.field_150362_t, 0, 2);
                }
            }
            return true;
        }
        return false;
    }

    public static void surroundByLeaves(final World world, final int x, final int y, final int z) {
        safeSetblock(world, x + 1, y, z, (Block)Blocks.field_150362_t, 0, 2);
        safeSetblock(world, x - 1, y, z, (Block)Blocks.field_150362_t, 0, 2);
        safeSetblock(world, x, y, z - 1, (Block)Blocks.field_150362_t, 0, 2);
        safeSetblock(world, x, y, z + 1, (Block)Blocks.field_150362_t, 0, 2);
    }

    public static void safeSetblock(final World world, final int x, final int y, final int z, final Block b, final int meta, final int flags) {
        if (world.func_147439_a(x, y, z).canBeReplacedByLeaves((IBlockAccess)world, x, y, z)) {
            world.func_147465_d(x, y, z, b, meta, flags);
        }
    }

    public static void generateIceshrooms(final World world, final Random random, final int x, int y, final int z) {
        final int vary = getNumEmptyBlocks(world, x, y, z);
        if (vary != 0) {
            y = y - vary + 1;
        }
        if (!world.func_147437_c(x, y - 1, z)) {
            if (!world.func_147439_a(x, y, z).func_149688_o().func_76224_d()) {
                world.func_147465_d(x, y - 1, z, Blocks.field_150432_aD, 0, 2);
                world.func_147465_d(x, y, z, BlockInit.cavePlantBlock, randomChoice(0, 1, 4, 5), 3);
            }
            convertToFrozenType(world, random, x, y, z);
        }
    }

    public static void generateLavashrooms(final World world, final Random random, final int x, int y, final int z) {
        final int vary = getNumEmptyBlocks(world, x, y, z);
        if (vary != 0) {
            y = y - vary + 1;
        }
        if (!world.func_147437_c(x, y - 1, z)) {
            if (!world.func_147439_a(x, y, z).func_149688_o().func_76224_d()) {
                world.func_147465_d(x, y - 1, z, BlockInit.scorchedStone, 0, 2);
                world.func_147465_d(x, y, z, BlockInit.cavePlantBlock, randomChoice(0, 1, 6, 7), 3);
            }
            convertToLavaType(world, random, x, y, z);
        }
    }

    public static void generateIcicles(final World world, final Random random, final int x, final int y, final int z, final int distance) {
        world.func_147465_d(x, y + 1, z, Blocks.field_150432_aD, 0, 2);
        world.func_147465_d(x, y, z, BlockInit.iceStalactiteBlock, randomChoice(0, 1, 2), 3);
        convertToFrozenType(world, random, x, y, z);
        final int botY = y - distance + 1;
        if (distance != 0 && !world.func_147439_a(x, botY, z).func_149688_o().func_76224_d()) {
            convertToFrozenType(world, random, x, botY, z);
        }
    }

    public static void generateMushrooms(final World p_76484_1_, final Random p_76484_2_, final int p_76484_3_, int p_76484_4_, final int p_76484_5_, final int distance) {
        if (p_76484_4_ > 56) {
            return;
        }
        final int l = p_76484_2_.nextInt(2);
        final boolean stone = p_76484_2_.nextInt(5) > 2;
        if (distance != 0) {
            p_76484_4_ = p_76484_4_ - distance + 1;
        }
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
                        if (block instanceof BlockHugeGlowingMushroom || block instanceof BlockHugeGlowingMushroom2) {
                            flag = false;
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
        final Block block2 = p_76484_1_.func_147439_a(p_76484_3_, p_76484_4_ - 1, p_76484_5_);
        if (block2 != Blocks.field_150346_d && block2 != Blocks.field_150348_b && block2 != Blocks.field_150391_bh) {
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
                        p_76484_1_.func_147465_d(l3, k1, i2, stone ? Blocks.field_150348_b : ((l == 0) ? BlockInit.mushroomBlockBlue : BlockInit.mushroomBlockGreen), j2, 2);
                    }
                }
            }
        }
        for (int k1 = 0; k1 < i1; ++k1) {
            final Block block3 = p_76484_1_.func_147439_a(p_76484_3_, p_76484_4_ + k1, p_76484_5_);
            if (block3.canBeReplacedByLeaves((IBlockAccess)p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_)) {
                p_76484_1_.func_147465_d(p_76484_3_, p_76484_4_ + k1, p_76484_5_, stone ? Blocks.field_150348_b : ((l == 0) ? BlockInit.mushroomBlockBlue : BlockInit.mushroomBlockGreen), 10, 2);
            }
        }
    }

    public static void generateSkulls(final World world, final Random random, final int x, final int y, final int z, final int numEmptyBlocks) {
        if (numEmptyBlocks > 0 && noLiquidUnderneath(world, x, y - numEmptyBlocks + 1, z)) {
            final int auxY = y - numEmptyBlocks + 1;
            if (auxY > 0 && numEmptyBlocks > 0) {
                world.func_147465_d(x, auxY, z, Blocks.field_150465_bP, 1, 3);
                final TileEntity skullTE = world.func_147438_o(x, auxY, z);
                if (skullTE instanceof TileEntitySkull) {
                    ((TileEntitySkull)skullTE).func_145903_a(random.nextInt(360));
                }
            }
        }
    }

    public static void generateGlowLily(final World world, final Random random, final int x, final int y, final int z, final int numEmptyBlocks) {
        if (isGoodForLily(world, x, y - numEmptyBlocks, z)) {
            world.func_147465_d(x, y - numEmptyBlocks + 1, z, (random.nextFloat() > 0.5f) ? BlockInit.glowLily : BlockInit.glowLilyBlue, 0, 2);
        }
    }

    public static void generateLily(final World world, final Random random, final int x, final int y, final int z, final int numEmptyBlocks) {
        if (world.func_147439_a(x, y - numEmptyBlocks, z) == Blocks.field_150355_j) {
            world.func_147449_b(x, y - numEmptyBlocks + 1, z, Blocks.field_150392_bi);
        }
    }

    public static boolean isGoodForLily(final World world, final int x, final int y, final int z) {
        return world.func_147439_a(x, y, z).func_149688_o() == Material.field_151586_h && world.func_72805_g(x, y, z) == 0 && ((world.func_147439_a(x + 1, y, z).func_149688_o() == Material.field_151586_h && world.func_72805_g(x + 1, y, z) == 0) || (world.func_147439_a(x - 1, y, z).func_149688_o() == Material.field_151586_h && world.func_72805_g(x - 1, y, z) == 0));
    }

    public static boolean noLiquidUnderneath(final World world, final int x, final int y, final int z) {
        return !(world.func_147439_a(x, y - 1, z) instanceof BlockLiquid);
    }

    public static void generateVines(final World world, final Random random, final int x, final int y, final int z) {
        final int distance = getNumEmptyBlocks(world, x, y, z);
        int aux;
        if (distance > 5) {
            aux = random.nextInt(distance - 5) + 5;
        }
        else {
            aux = distance;
        }
        final int side = random.nextInt(4) + 2;
        for (int i = 0; i < aux && !world.func_147439_a(x, y - i, z).func_149688_o().func_76224_d(); ++i) {
            world.func_147465_d(x, y - i, z, Blocks.field_150395_bd, 1 << Direction.field_71579_d[side], 0);
        }
    }

    public static int getNumEmptyBlocks(final World world, final int x, int y, final int z) {
        int dist;
        for (dist = 0; y > 5 && !world.func_147445_c(x, y, z, true) && world.func_147437_c(x, y, z); --y, ++dist) {}
        return dist;
    }

    public static void convertToFrozenType(final World world, final Random random, final int x, final int y, final int z) {
        final int height = random.nextInt(5) + 3;
        final int length = random.nextInt(5) + 3;
        final int width = random.nextInt(5) + 3;
        final int newX = x - length / 2;
        final int newY = y + height / 2;
        final int newZ = z - width / 2;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < length; ++j) {
                for (int k = 0; k < width; ++k) {
                    if (weightedChoice(0.8f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f) == 1) {
                        final Block aux = world.func_147439_a(newX + j, newY - i, newZ + k);
                        if (CavesDecorator.freezable.contains(aux)) {
                            world.func_147465_d(newX + j, newY - i, newZ + k, Blocks.field_150432_aD, 0, 2);
                        }
                    }
                }
            }
        }
    }

    public static void convertToLavaType(final World world, final Random random, final int x, final int y, final int z) {
        final int height = random.nextInt(5) + 3;
        final int length = random.nextInt(5) + 3;
        final int width = random.nextInt(5) + 3;
        final int newX = x - length / 2;
        final int newY = y + height / 2;
        final int newZ = z - width / 2;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < length; ++j) {
                for (int k = 0; k < width; ++k) {
                    if (weightedChoice(0.8f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f) == 1) {
                        final Block aux = world.func_147439_a(newX + j, newY - i, newZ + k);
                        if (CavesDecorator.freezable.contains(aux) && !world.func_147437_c(newX + j, newY - i + 1, newZ + k)) {
                            world.func_147465_d(newX + j, newY - i, newZ + k, BlockInit.scorchedStone, 0, 2);
                        }
                    }
                }
            }
        }
    }

    public static int randomChoice(final int... val) {
        final Random random = new Random();
        return val[random.nextInt(val.length)];
    }

    public static int weightedChoice(final String[] values) {
        if (values.length != 6) {
            throw new RuntimeException("Incorrect amount of arguments into weightedChoice! Must be 6!");
        }
        final float[] fResult = new float[values.length];
        for (int i = 0; i < values.length; ++i) {
            fResult[i] = Float.parseFloat(values[i]);
        }
        return weightedChoice(fResult[0], fResult[1], fResult[2], fResult[3], fResult[4], fResult[5]);
    }

    public static int weightedChoice(float par1, float par2, float par3, float par4, float par5, final float par6) {
        final float total = par1 + par2 + par3 + par4 + par5 + par6;
        final float val = new Random().nextFloat();
        par1 /= total;
        par2 /= total;
        par3 /= total;
        par4 /= total;
        par5 /= total;
        if (val < par1) {
            return 1;
        }
        if (val < par2 + par1) {
            return 2;
        }
        float previous = par1 + par2;
        if (val < par3 + previous) {
            return 3;
        }
        previous += par3;
        if (val < par4 + previous) {
            return 4;
        }
        previous += par4;
        return (val < par5 + previous) ? 5 : 6;
    }

    public static void generateScorchedLavaStone(final World world, final Random random, final int x, final int y, final int z, final int distance) {
        if (world.func_147439_a(x, y - 1, z).func_149721_r()) {
            world.func_147449_b(x, y - 1, z, BlockInit.scorchedLavaStone);
        }
        else if (world.func_147439_a(x, y - distance, z).func_149721_r()) {
            world.func_147449_b(x, y - distance, z, BlockInit.scorchedLavaStone);
        }
    }

    static {
        CavesDecorator.freezable = Arrays.asList(Blocks.field_150348_b, Blocks.field_150346_d, Blocks.field_150351_n, (Block)Blocks.field_150349_c);
        CavesDecorator.maxGenHeight = 80;
        CavesDecorator.maxLength = 8;
        CavesDecorator.timesPerChunck = 50;
        jungleGen = new GenerateJungleCaves();
        waterGen = new GenerateWaterCaves();
        sandGen = new GenerateSandCaves();
        plainGen = new GeneratePlainCaves();
        iceGen = new GenerateIceCaves();
        fireGen = new GenerateFireCaves();
        livingGen = new GenerateLivingCaves();
    }
}
