package net.tclproject.mysteriumlib.asm.fixes;

import java.util.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.world.gen.structure.*;
import net.tclproject.mysteriumlib.asm.annotations.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.chunk.*;
import net.tclproject.immersivecavegen.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.tclproject.immersivecavegen.world.*;
import net.minecraft.world.biome.*;

public class MysteriumPatchesFixesCave
{
    private static final Random rand;
    private static final Random noiseGen;
    private static final Random caveRNG;
    public static final Random newRand;
    private static final Random largeCaveRNG;
    private static long seedMultiplier;
    private static long regionalCaveSeedMultiplier;
    private static long colossalCaveSeedMultiplier;
    private static int caveOffsetX;
    private static World worldObj;
    private static int caveOffsetZ;
    private static int mineshaftOffsetX;
    private static int mineshaftOffsetZ;
    private static int chunkX_16;
    private static int chunkZ_16;
    private static double chunkCenterX;
    private static double chunkCenterZ;
    private static Block[] chunkData;
    private static final byte[] caveDataArray;
    private static final float[] ravineData;
    private static final float[] ravineHeightLookup;
    private static final byte[] biomeList;
    private static final float[] SINE_TABLE;
    private static boolean isInitialized;
    public static MapGenStronghold strongholdGenerator;
    public static MapGenVillage villageGenerator;
    public static MapGenMineshaft mineshaftGenerator;
    public static MapGenScatteredFeature scatteredFeatureGenerator;
    public static int oceanAvg;

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static void func_151542_a(final MapGenCaves c, final long p_151542_1_, final int p_151542_3_, final int p_151542_4_, final Block[] p_151542_5_, final double p_151542_6_, final double p_151542_8_, final double p_151542_10_) {
    }

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static void func_151541_a(final MapGenCaves c, final long p_151541_1_, final int p_151541_3_, final int p_151541_4_, final Block[] p_151541_5_, final double p_151541_6_, final double p_151541_8_, final double p_151541_10_, final float p_151541_12_, final float p_151541_13_, final float p_151541_14_, final int p_151541_15_, final int p_151541_16_, final double p_151541_17_) {
    }

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static void func_151538_a(final MapGenCaves c, final World p_151538_1_, final int p_151538_2_, final int p_151538_3_, final int p_151538_4_, final int p_151538_5_, final Block[] p_151538_6_) {
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
    public static boolean func_151539_a(final MapGenBase instance, final IChunkProvider p_151539_1_, final World world, final int chunkX, final int chunkZ, final Block[] data) {
        for (final String str : WGConfig.dimblacklist) {
            if (world != null && String.valueOf(world.field_73011_w.field_76574_g).equalsIgnoreCase(str)) {
                return false;
            }
        }
        if (instance instanceof MapGenCaves) {
            if (!MysteriumPatchesFixesCave.isInitialized) {
                initialize(world);
            }
            generate(chunkX, chunkZ, data);
            return true;
        }
        return false;
    }

    private static void initialize(final World world) {
        MysteriumPatchesFixesCave.isInitialized = true;
        MysteriumPatchesFixesCave.worldObj = world;
        MysteriumPatchesFixesCave.rand.setSeed(MysteriumPatchesFixesCave.worldObj.func_72905_C());
        MysteriumPatchesFixesCave.newRand.setSeed(MysteriumPatchesFixesCave.worldObj.func_72905_C());
        MysteriumPatchesFixesCave.seedMultiplier = MysteriumPatchesFixesCave.rand.nextLong() / 2L * 2L + 1L;
        MysteriumPatchesFixesCave.caveOffsetX = MysteriumPatchesFixesCave.rand.nextInt(128) + 2000000;
        MysteriumPatchesFixesCave.caveOffsetZ = MysteriumPatchesFixesCave.rand.nextInt(128) + 2000000;
        MysteriumPatchesFixesCave.mineshaftOffsetX = MysteriumPatchesFixesCave.rand.nextInt(7) + 2000000;
        MysteriumPatchesFixesCave.mineshaftOffsetZ = MysteriumPatchesFixesCave.rand.nextInt(7) + 2000000;
        final byte range = 66;
    Label_0186:
        for (int i = 0; i < 100; i += 2) {
            MysteriumPatchesFixesCave.colossalCaveSeedMultiplier = MysteriumPatchesFixesCave.seedMultiplier + i;
            for (int z = -range; z <= range; ++z) {
                for (int x = -range; x <= range; ++x) {
                    if (validColossalCaveLocation(x, z, x * x + z * z)) {
                        break Label_0186;
                    }
                }
            }
        }
        for (int i = 0; i < 100; i += 2) {
            MysteriumPatchesFixesCave.regionalCaveSeedMultiplier = MysteriumPatchesFixesCave.seedMultiplier + i;
            for (int z = -range; z <= range; z += 12) {
                for (int x = -range; x <= range; x += 12) {
                    if (validRegionalCaveLocation(x, z, x * x + z * z) && isGiantCaveRegion(x, z)) {
                        return;
                    }
                }
            }
        }
    }

    public static void generate(final int chunkX, final int chunkZ, final Block[] data) {
        MysteriumPatchesFixesCave.chunkData = data;
        MysteriumPatchesFixesCave.chunkX_16 = chunkX * 16;
        MysteriumPatchesFixesCave.chunkZ_16 = chunkZ * 16;
        MysteriumPatchesFixesCave.chunkCenterX = MysteriumPatchesFixesCave.chunkX_16 + 8;
        MysteriumPatchesFixesCave.chunkCenterZ = MysteriumPatchesFixesCave.chunkZ_16 + 8;
        MysteriumPatchesFixesCave.noiseGen.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
        initializeCaveData(chunkX, chunkZ);
        for (int index = -12; index <= 12; ++index) {
            for (int z = -12; z <= 12; ++z) {
                final int index2 = index * index + z * z;
                if (index2 <= 145) {
                    final int y = chunkX + index;
                    final int cz = chunkZ + z;
                    if (y != 0 || cz != 0) {
                        final long chunkSeed = (y * 341873128712L + cz * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier;
                        final int genCaves = validCaveLocation(index, z);
                        if (genCaves != 2 && genCaves < 6) {
                            if (genCaves > 0) {
                                if (genCaves == 3) {
                                    MysteriumPatchesFixesCave.rand.setSeed(chunkSeed);
                                    for (int i = 0; i < WGConfig.cavesSpawnMultiplier; ++i) {
                                        generateRegionalCaves(y, cz);
                                    }
                                }
                                MysteriumPatchesFixesCave.rand.setSeed(chunkSeed);
                                for (int i = 0; i < WGConfig.cavesSpawnMultiplier; ++i) {
                                    generateCaves(y, cz, index2, genCaves);
                                }
                                MysteriumPatchesFixesCave.rand.setSeed(chunkSeed);
                                for (int i = 0; i < WGConfig.cavernsSpawnMultiplier; ++i) {
                                    generateRavines(y, cz, index2 <= 20, genCaves);
                                }
                            }
                            else if (genCaves < 0 && Math.abs(index) <= 9 && Math.abs(z) <= 9) {
                                MysteriumPatchesFixesCave.rand.setSeed(chunkSeed);
                                for (int i = 0; i < WGConfig.cavesSpawnMultiplier; ++i) {
                                    generateColossalCaveSystem(y, cz);
                                }
                            }
                        }
                        else if (index2 <= 65) {
                            MysteriumPatchesFixesCave.rand.setSeed(chunkSeed);
                            for (int i = 0; i < WGConfig.cavesSpawnMultiplier; ++i) {
                                generateSpecialCaveSystems(y, cz, genCaves);
                            }
                        }
                    }
                }
            }
        }
        for (int index = 0; index < 16; ++index) {
            for (int z = 0; z < 16; ++z) {
                final int index2 = index << 12 | z << 8;
                for (int y = 1; y <= 4; ++y) {
                    if (MysteriumPatchesFixesCave.chunkData[index2 | y] == Blocks.field_150357_h) {
                        MysteriumPatchesFixesCave.chunkData[index2 | y] = Blocks.field_150348_b;
                    }
                }
            }
        }
        if ((chunkX & 0x1) == (chunkZ & 0x1) && (MysteriumPatchesFixesCave.biomeList[(MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(MysteriumPatchesFixesCave.chunkX_16 + 8, MysteriumPatchesFixesCave.chunkZ_16 + 8).field_76756_M > 255) ? 20 : MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(MysteriumPatchesFixesCave.chunkX_16 + 8, MysteriumPatchesFixesCave.chunkZ_16 + 8).field_76756_M] & 0x10) != 0x0) {
            final int index = MysteriumPatchesFixesCave.rand.nextInt(16) << 12 | MysteriumPatchesFixesCave.rand.nextInt(16) << 8 | MysteriumPatchesFixesCave.rand.nextInt(3) + 1;
            if (MysteriumPatchesFixesCave.chunkData[index] == Blocks.field_150348_b) {
                MysteriumPatchesFixesCave.chunkData[index] = Blocks.field_150412_bA;
            }
        }
    }

    private static void generateCaves(final int chunkX, final int chunkZ, final int flag, int genCaves) {
        if (MysteriumPatchesFixesCave.rand.nextInt(100) + 1 <= WGConfig.caveNormalReductionPercentage) {
            return;
        }
        int chance = MysteriumPatchesFixesCave.rand.nextInt(15);
        int caveSize = 0;
        if (genCaves == 1 || genCaves == 0) {
            final Random r = new Random();
            r.setSeed(MysteriumPatchesFixesCave.worldObj.func_72905_C());
            if (r.nextInt(100) < WGConfig.giantCaveChance) {
                genCaves = 3;
                chance = 0;
            }
        }
        if (chance == 0) {
            caveSize = MysteriumPatchesFixesCave.rand.nextInt(MysteriumPatchesFixesCave.rand.nextInt(MysteriumPatchesFixesCave.rand.nextInt(40) + 1) + 1);
            final boolean blockX = true;
            if (caveSize > 0) {
                final int blockZ = chunkX * 16;
                final int type = chunkZ * 16;
                final int range = chunkX + MysteriumPatchesFixesCave.caveOffsetX + 4;
                final int i = chunkZ + MysteriumPatchesFixesCave.caveOffsetZ + 4;
                int y = -1;
                final boolean genNormalCaves = genCaves != 3 && genCaves != 4;
                boolean applyCaveVariation = false;
                long regionSeed = 0L;
                if (blockX && genCaves < 5) {
                    MysteriumPatchesFixesCave.caveRNG.setSeed((range / 16 * 341873128712L + i / 16 * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
                    MysteriumPatchesFixesCave.largeCaveRNG.setSeed(MysteriumPatchesFixesCave.rand.nextLong());
                    if (!genNormalCaves || MysteriumPatchesFixesCave.caveRNG.nextInt(4) != 0) {
                        y = (1 << MysteriumPatchesFixesCave.caveRNG.nextInt(3)) - 1;
                        applyCaveVariation = true;
                        regionSeed = MysteriumPatchesFixesCave.caveRNG.nextLong();
                    }
                    if (genCaves != 3 || !isGiantCaveRegion(chunkX, chunkZ)) {
                        if ((range & 0x7) == 0x0 && (i & 0x7) == 0x0 && (range & 0x8) == (i & 0x8)) {
                            generateLargeCave(chunkX, chunkZ, 0);
                        }
                        else if (caveSize <= 3 && MysteriumPatchesFixesCave.largeCaveRNG.nextInt(4) <= y) {
                            final int direction = validLargeCaveLocation(chunkX, chunkZ, caveSize);
                            if (direction > 0) {
                                int x = 1;
                                int largerLargeCaves = 30;
                                int y2 = 15;
                                if (MysteriumPatchesFixesCave.largeCaveRNG.nextInt(10) == 0) {
                                    x += 1 + MysteriumPatchesFixesCave.largeCaveRNG.nextInt(3);
                                }
                                for (int curviness = 0; curviness < x; ++curviness) {
                                    final int z = generateLargeCave(chunkX, chunkZ, x);
                                    largerLargeCaves = Math.min(largerLargeCaves, z);
                                    y2 = Math.max(y2, z);
                                }
                                if (x > 1) {
                                    if (largerLargeCaves < y2) {
                                        generateVerticalCave(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), blockZ + 8, largerLargeCaves, y2, type + 8, -1.0f, blockZ + 8, type + 8, 1);
                                    }
                                }
                                else {
                                    generateHorizontalCave(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), chunkX * 16 + 8, y2 & 0xFF, chunkZ * 16 + 8, MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * 0.5f + 0.25f, y2 / 256 / 1024.0f + 3.1415927f, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) / 4.0f, 0, MysteriumPatchesFixesCave.largeCaveRNG.nextInt(direction * 4 + 4) + direction * 20 + 20, 0);
                                }
                            }
                        }
                    }
                }
                if (flag <= 40) {
                    boolean var29 = false;
                    int x = 4;
                    boolean var30 = false;
                    y = 10;
                    float var31 = 1.0f;
                    float var32 = 0.1f;
                    boolean var33 = false;
                    boolean seaLevelCaves = false;
                    if (applyCaveVariation) {
                        MysteriumPatchesFixesCave.caveRNG.setSeed(regionSeed);
                        if (caveSize < 20 && genCaves < 5) {
                            var29 = MysteriumPatchesFixesCave.caveRNG.nextBoolean();
                            x = 2 << MysteriumPatchesFixesCave.caveRNG.nextInt(2) + MysteriumPatchesFixesCave.caveRNG.nextInt(2);
                            var30 = MysteriumPatchesFixesCave.caveRNG.nextBoolean();
                            y = 5 << MysteriumPatchesFixesCave.caveRNG.nextInt(2) + MysteriumPatchesFixesCave.caveRNG.nextInt(2);
                        }
                        if (MysteriumPatchesFixesCave.caveRNG.nextBoolean()) {
                            var31 += MysteriumPatchesFixesCave.caveRNG.nextFloat();
                            if (MysteriumPatchesFixesCave.caveRNG.nextBoolean()) {
                                var31 /= 2.0f;
                            }
                        }
                        if (MysteriumPatchesFixesCave.caveRNG.nextBoolean()) {
                            if (MysteriumPatchesFixesCave.caveRNG.nextBoolean()) {
                                var32 /= 2.0f;
                            }
                            if (MysteriumPatchesFixesCave.rand.nextBoolean()) {
                                var32 += MysteriumPatchesFixesCave.rand.nextFloat() * var32;
                            }
                            else {
                                var32 += MysteriumPatchesFixesCave.caveRNG.nextFloat() * var32;
                            }
                        }
                        if (caveSize >= 20) {
                            final float branchPoint = (float)(caveSize / 10);
                            var31 = (var31 + branchPoint - 1.0f) / branchPoint;
                            var32 = (var32 + (branchPoint - 1.0f) / 10.0f) / branchPoint;
                        }
                        var33 = MysteriumPatchesFixesCave.caveRNG.nextBoolean();
                        seaLevelCaves = MysteriumPatchesFixesCave.caveRNG.nextBoolean();
                    }
                    if (genNormalCaves) {
                        int var34;
                        if ((var34 = caveSize) >= 10) {
                            if (var31 > 1.5f) {
                                var34 = caveSize / 2 + 1;
                            }
                            else if (var31 > 1.25f) {
                                var34 = caveSize * 3 / 4 + 1;
                            }
                        }
                        if (genCaves == 5 && var34 > 2) {
                            var34 = var34 / 4 + 2;
                        }
                        final Random r2 = new Random();
                        r2.setSeed(MysteriumPatchesFixesCave.worldObj.func_72905_C());
                        if (r2.nextInt(100) < WGConfig.giantCaveChance) {
                            var30 = true;
                            y = 1;
                        }
                        generateCaveSystem(var34, blockZ, type, var31, var32, var30, y, var29, x);
                    }
                    caveSize /= 5;
                    if (caveSize > 0) {
                        if (caveSize < 4) {
                            if (var33) {
                                int var34 = MysteriumPatchesFixesCave.rand.nextInt(MysteriumPatchesFixesCave.rand.nextInt(caveSize + 3) + 1);
                                if (var34 > 2) {
                                    var34 = 2;
                                }
                                for (int length = 0; length < var34; ++length) {
                                    int width = Math.max(1, 1 + WGConfig.widthAdditionNormal);
                                    if (WGConfig.hardLimitsEnabled) {
                                        width = Math.min(width, WGConfig.widthMaxNormal);
                                        width = Math.max(width, WGConfig.widthMinNormal);
                                    }
                                    if (MysteriumPatchesFixesCave.rand.nextInt(4) == 0) {
                                        width += MysteriumPatchesFixesCave.rand.nextInt(4);
                                    }
                                    for (int LL = 0; LL < width; ++LL) {
                                        generateSingleCave(blockZ, 3, type, var32);
                                    }
                                }
                            }
                            if (seaLevelCaves) {
                                int var34 = MysteriumPatchesFixesCave.rand.nextInt(caveSize * 2 + 1);
                                if (var34 > caveSize) {
                                    var34 = caveSize;
                                }
                                final int length = 23 - var34 * 5;
                                if (WGConfig.hardLimitsEnabled) {
                                    var34 = Math.min(var34, WGConfig.widthMaxNormal);
                                    var34 = Math.max(var34, WGConfig.widthMinNormal);
                                }
                                for (int width = 0; width < var34; ++width) {
                                    generateSingleCave(blockZ, MysteriumPatchesFixesCave.rand.nextInt(length) + width * 5 + 40, type, var32);
                                }
                            }
                        }
                        else {
                            caveSize /= 2;
                        }
                    }
                    else {
                        caveSize = MysteriumPatchesFixesCave.rand.nextInt(2);
                    }
                }
                if (!blockX && flag <= 65) {
                    final float var35 = getDirection(chunkX, chunkZ);
                    if (var35 > -5.0f) {
                        final double var36 = chunkX * 16 + 8;
                        final double var37 = MysteriumPatchesFixesCave.largeCaveRNG.nextInt(20) + 15;
                        final double var38 = chunkZ * 16 + 8;
                        final int var34 = 96 + MysteriumPatchesFixesCave.largeCaveRNG.nextInt(32);
                        final int length = var34 + 24 + MysteriumPatchesFixesCave.largeCaveRNG.nextInt(16);
                        float var39 = Math.max(1.0f, MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() + 1.0f + WGConfig.widthAdditionNormal);
                        if (WGConfig.hardLimitsEnabled) {
                            var39 = Math.min(var39, (float)WGConfig.widthMaxNormal);
                            var39 = Math.max(var39, (float)WGConfig.widthMinNormal);
                        }
                        generateHorizontalCave(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), var36, var37, var38, var39, var35, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) / 4.0f, var34, length, 1);
                        generateHorizontalCave(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), var36, var37, var38, Math.min(var39 * 0.75f, MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * var39 * 0.75f + var39 * 0.25f), var35 - 1.5707964f, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) / 4.0f, 0, length - var34, -1);
                        generateHorizontalCave(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), var36, var37, var38, Math.min(var39 * 0.75f, MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * var39 * 0.75f + var39 * 0.25f), var35 + 1.5707964f, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) / 4.0f, 0, length - var34, -1);
                    }
                }
            }
            else if (flag <= 65 && blockX && (genCaves == 1 || (genCaves == 3 && !isGiantCaveRegion(chunkX, chunkZ)))) {
                final int blockZ = validCaveClusterLocation(chunkX, chunkZ, true, true);
                if (blockZ > 0) {
                    generateCaveCluster(chunkX, chunkZ, (genCaves == 3) ? 1 : blockZ);
                }
            }
        }
        if (chance <= 1 && flag <= 40) {
            if (chance == 1) {
                caveSize = MysteriumPatchesFixesCave.rand.nextInt(MysteriumPatchesFixesCave.rand.nextInt(MysteriumPatchesFixesCave.rand.nextInt(40) + 1) + 1) / 5;
                if (caveSize > 3) {
                    caveSize /= 2;
                }
            }
            if (caveSize > 0) {
                final int var40 = chunkX * 16;
                final int blockZ = chunkZ * 16;
                int type;
                try {
                    type = (MysteriumPatchesFixesCave.biomeList[MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(var40 + 8, blockZ + 8).field_76756_M] & 0x3);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    type = 1;
                }
                switch (type) {
                    case 1: {
                        caveSize += MysteriumPatchesFixesCave.rand.nextInt(caveSize * (MysteriumPatchesFixesCave.rand.nextInt(2) + 1) + 2);
                        break;
                    }
                    case 2: {
                        caveSize += MysteriumPatchesFixesCave.rand.nextInt(caveSize + MysteriumPatchesFixesCave.rand.nextInt(2) + 1);
                        break;
                    }
                }
                if (caveSize > 0) {
                    if (caveSize > 9) {
                        caveSize = 9;
                    }
                    final int range = 50 - caveSize * 5;
                    chance = 50 + chance * 10;
                    for (int i = 0; i < caveSize; ++i) {
                        final int y = MysteriumPatchesFixesCave.rand.nextInt(range) + chance + i * 5;
                        if (y < 80 || type < 4) {
                            generateSingleCave(var40, y, blockZ, 0.1f);
                        }
                    }
                }
            }
        }
    }

    public static int validLargeCaveLocation(final int chunkX, final int chunkZ, int caves) {
        int flag = 3;
        int caves2 = caves;
        for (int x = -3; x <= 3; ++x) {
            for (int z = -3; z <= 3; ++z) {
                final int x2z2 = x * x + z * z;
                if (x2z2 > 0 && x2z2 <= 10) {
                    MysteriumPatchesFixesCave.caveRNG.setSeed(((chunkX + x) * 341873128712L + (chunkZ + z) * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
                    if (MysteriumPatchesFixesCave.caveRNG.nextInt(15) == 0) {
                        final int c = MysteriumPatchesFixesCave.caveRNG.nextInt(MysteriumPatchesFixesCave.caveRNG.nextInt(MysteriumPatchesFixesCave.caveRNG.nextInt(40) + 1) + 1);
                        if (x2z2 <= 5) {
                            caves += c;
                            if (caves > 12) {
                                return 0;
                            }
                        }
                        caves2 += c;
                        if (caves2 > 6) {
                            flag = ((caves2 > 12) ? 1 : 2);
                        }
                    }
                }
            }
        }
        return flag;
    }

    private static float getDirection(final int chunkX, final int chunkZ) {
        int caveCountEast = 0;
        int caveCountWest = 0;
        int caveCountSouth = 0;
        int caveCountNorth = 0;
        int caveCountCenter = 0;
        for (int direction = -4; direction <= 4; ++direction) {
            for (int x = -4; x <= 4; ++x) {
                final int x2z2 = x * x + direction * direction;
                if (x2z2 <= 17) {
                    MysteriumPatchesFixesCave.largeCaveRNG.setSeed(((chunkX + x) * 341873128712L + (chunkZ + direction) * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
                    if (MysteriumPatchesFixesCave.largeCaveRNG.nextInt(15) == 0) {
                        final int caves = MysteriumPatchesFixesCave.largeCaveRNG.nextInt(MysteriumPatchesFixesCave.largeCaveRNG.nextInt(MysteriumPatchesFixesCave.largeCaveRNG.nextInt(40) + 1) + 1);
                        if (x > 0) {
                            caveCountEast += caves;
                        }
                        else if (x < 0) {
                            caveCountWest += caves;
                        }
                        if (direction > 0) {
                            caveCountSouth += caves;
                        }
                        else if (direction < 0) {
                            caveCountNorth += caves;
                        }
                        if (x2z2 <= 4) {
                            caveCountCenter += caves;
                            if (caveCountCenter > 8) {
                                return -10.0f;
                            }
                        }
                    }
                }
            }
        }
        if (caveCountCenter < 3) {
            return -10.0f;
        }
        int direction = 0;
        if (caveCountEast > 0) {
            ++direction;
        }
        if (caveCountSouth > 0) {
            direction += 2;
        }
        if (caveCountWest > 0) {
            direction += 4;
        }
        if (caveCountNorth > 0) {
            direction += 8;
        }
        switch (direction) {
            case 0: {
                return MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * 6.2831855f;
            }
            case 1: {
                return 3.1415927f + (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) * 1.5707964f;
            }
            case 2: {
                return 4.712389f + (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) * 1.5707964f;
            }
            case 3: {
                return 3.926991f + (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) * 0.7853982f;
            }
            case 4: {
                return (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) * 1.5707964f;
            }
            case 5: {
                return 1.5707964f + MysteriumPatchesFixesCave.largeCaveRNG.nextInt(2) * 3.1415927f;
            }
            case 6: {
                return 5.497787f + (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) * 0.7853982f;
            }
            case 7: {
                return 4.712389f;
            }
            case 8: {
                return 1.5707964f + (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) * 1.5707964f;
            }
            case 9: {
                return 2.356194f + (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) * 0.7853982f;
            }
            case 10: {
                return MysteriumPatchesFixesCave.largeCaveRNG.nextInt(2) * 3.1415927f;
            }
            case 11: {
                return 3.1415927f;
            }
            case 12: {
                return 0.7853982f + (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) * 0.7853982f;
            }
            case 13: {
                return 1.5707964f;
            }
            case 14: {
                return 0.0f;
            }
            default: {
                return -10.0f;
            }
        }
    }

    private static void generateCaveCluster(int centerX, int centerZ, final int clusterType) {
        centerX = centerX * 16 + 8;
        centerZ = centerZ * 16 + 8;
        final boolean comboCave = clusterType == 3;
        boolean linkCave = clusterType == 1 || clusterType == 2;
        int quadrant = MysteriumPatchesFixesCave.rand.nextInt(4);
        double y = 3.0;
        final boolean size = true;
        double yInc = 53.0;
        int mazeIndex1 = -1;
        int mazeIndex2 = -1;
        int mazeType = 0;
        byte centerOffset = 4;
        int caveCount = Math.max(1, 1 + WGConfig.additionalCavesInClusterAmount);
        if (WGConfig.hardLimitsEnabled) {
            caveCount = Math.min(caveCount, WGConfig.maxCavesInClusterAmount);
            caveCount = Math.max(caveCount, WGConfig.minCavesInClusterAmount);
        }
        int var34;
        int caveType;
        if (comboCave) {
            var34 = MysteriumPatchesFixesCave.rand.nextInt(6) + 24;
            caveType = MysteriumPatchesFixesCave.rand.nextInt(3);
            mazeIndex1 = MysteriumPatchesFixesCave.rand.nextInt(var34);
            mazeIndex2 = (mazeIndex1 + var34 / 4 + MysteriumPatchesFixesCave.rand.nextInt(var34 / 2)) % var34;
            mazeType = MysteriumPatchesFixesCave.rand.nextInt(2);
            yInc /= var34 - 1;
            centerOffset = 8;
        }
        else {
            var34 = 1;
            caveType = MysteriumPatchesFixesCave.rand.nextInt(4);
        }
        for (int caveIndex = 0; caveIndex < var34; ++caveIndex) {
            switch (caveType) {
                case 0: {
                    int x = 0;
                    if (!comboCave) {
                        x = MysteriumPatchesFixesCave.rand.nextInt(35);
                        y = x;
                        caveCount = MysteriumPatchesFixesCave.rand.nextInt(4) + 3 + WGConfig.additionalCavesInClusterAmount;
                        if (WGConfig.hardLimitsEnabled) {
                            caveCount = Math.min(caveCount, WGConfig.maxCavesInClusterAmount);
                            caveCount = Math.max(caveCount, WGConfig.minCavesInClusterAmount);
                        }
                    }
                    for (int y2 = caveCount; y2 > 0; --y2) {
                        final double var35 = centerX + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantX(y2 + quadrant);
                        final double var36 = comboCave ? (y + (MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f - 1.0f)) : y;
                        final double var37 = centerZ + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantZ(y2 + quadrant);
                        generateCircularRoom(var35, var36, var37, MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * (comboCave ? 9.0f : 6.0f) + 3.0f);
                        generateDirectionalCave((int)(var35 + 0.5), (int)(var36 + 0.5), (int)(var37 + 0.5), centerX, centerZ, 0);
                        if (!comboCave) {
                            y += MysteriumPatchesFixesCave.rand.nextInt(4) + 2;
                            if (y2 == 1) {
                                generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), centerX + MysteriumPatchesFixesCave.rand.nextInt(5) - 2, x, (int)(var36 + 0.5), centerZ + MysteriumPatchesFixesCave.rand.nextInt(5) - 2, 0.0f, centerX, centerZ, 8);
                            }
                        }
                        if (linkCave && y2 == caveCount / 2) {
                            generateHorizontalLinkCave((int)var35, (int)var36, (int)var37, centerX, centerZ, 2 - clusterType);
                        }
                    }
                    break;
                }
                case 1: {
                    if (!comboCave) {
                        y = MysteriumPatchesFixesCave.rand.nextInt(35);
                        caveCount = MysteriumPatchesFixesCave.rand.nextInt(4) + 3 + WGConfig.additionalCavesInClusterAmount;
                        if (WGConfig.hardLimitsEnabled) {
                            caveCount = Math.min(caveCount, WGConfig.maxCavesInClusterAmount);
                            caveCount = Math.max(caveCount, WGConfig.minCavesInClusterAmount);
                        }
                    }
                    for (int y2 = caveCount; y2 > 0; --y2) {
                        final double var35 = centerX + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantX(y2 + quadrant);
                        final double var36 = comboCave ? (y + (MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f - 1.0f)) : y;
                        final double var37 = centerZ + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantZ(y2 + quadrant);
                        generateRavineCave(var35, var36, var37, comboCave ? 2.0f : 1.0f);
                        if (!comboCave) {
                            y += MysteriumPatchesFixesCave.rand.nextInt(3) + 3;
                        }
                        if (linkCave && y2 == caveCount / 2) {
                            generateHorizontalLinkCave((int)var35, (int)var36, (int)var37, centerX, centerZ, 4 - clusterType);
                        }
                    }
                    break;
                }
                case 2: {
                    if (!comboCave) {
                        y = MysteriumPatchesFixesCave.rand.nextInt(8);
                        caveCount = MysteriumPatchesFixesCave.rand.nextInt(4) + 3 + WGConfig.additionalCavesInClusterAmount;
                        if (WGConfig.hardLimitsEnabled) {
                            caveCount = Math.min(caveCount, WGConfig.maxCavesInClusterAmount);
                            caveCount = Math.max(caveCount, WGConfig.minCavesInClusterAmount);
                        }
                    }
                    for (int y2 = caveCount; y2 > 0; --y2) {
                        final double var35 = centerX + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantX(y2 + quadrant);
                        final int z1 = Math.round(comboCave ? (((float)y + MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f - 1.0f) / 1.5f) : ((float)y));
                        final double var38 = centerZ + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantZ(y2 + quadrant);
                        int length = Math.max(0, z1 - MysteriumPatchesFixesCave.rand.nextInt(5) + WGConfig.additionalCavesInClusterLength);
                        if (WGConfig.hardLimitsEnabled) {
                            length = Math.min(length, WGConfig.maxCavesInClusterLength);
                            length = Math.max(length, WGConfig.minCavesInClusterLength);
                        }
                        final int dirSwitch = z1 + Math.min(40 + MysteriumPatchesFixesCave.rand.nextInt(16), 24 + MysteriumPatchesFixesCave.rand.nextInt(36 - z1 / 2));
                        float var39 = MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f;
                        if (MysteriumPatchesFixesCave.rand.nextInt(10) == 0) {
                            var39 = Math.min(8.0f, var39 * (MysteriumPatchesFixesCave.rand.nextFloat() * 3.0f + 1.0f) + 2.0f);
                        }
                        if ((y2 + caveIndex & 0x1) == 0x0) {
                            generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), var35, length, dirSwitch, var38, var39, centerX, centerZ, comboCave ? 24 : 16);
                        }
                        else {
                            generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), var35, dirSwitch - length, 0, var38, var39, centerX, centerZ, comboCave ? 24 : 16);
                        }
                        if (!comboCave) {
                            y += MysteriumPatchesFixesCave.rand.nextInt(4) + 3;
                        }
                        if (linkCave && y2 == caveCount / 2) {
                            generateHorizontalLinkCave(centerX, MysteriumPatchesFixesCave.rand.nextInt(10) + 10, centerZ, centerX, centerZ, 4 - clusterType);
                        }
                    }
                    break;
                }
                case 3: {
                    int y2 = comboCave ? (MysteriumPatchesFixesCave.rand.nextInt(4) * 2 + mazeType) : MysteriumPatchesFixesCave.rand.nextInt(8);
                    final int z2 = centerX + (4 + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantX(quadrant);
                    final int i = comboCave ? Math.max(3, Math.round((float)y)) : (MysteriumPatchesFixesCave.rand.nextInt(50) + 3);
                    final int z1 = centerZ + (4 + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantZ(quadrant);
                    final float height = (i == 3) ? 2.625f : 1.625f;
                    for (int d = 0; d < 4; ++d) {
                        y2 += 2;
                        int length = Math.max(0, MysteriumPatchesFixesCave.rand.nextInt(8) + WGConfig.additionalCavesInClusterLength);
                        if (WGConfig.hardLimitsEnabled) {
                            length = Math.min(length, WGConfig.maxCavesInClusterLength);
                            length = Math.max(length, WGConfig.minCavesInClusterLength);
                        }
                        if ((y2 & 0x1) == 0x0) {
                            length += 24;
                        }
                        else {
                            length += 17;
                        }
                        generateMazeCaveSegment(z2, i, z1, y2, length, height);
                        int dirSwitch = MysteriumPatchesFixesCave.rand.nextInt(2) * 4 + 2;
                        for (int maxOffset = length * 3 / 4, offset = length / 5 + MysteriumPatchesFixesCave.rand.nextInt(length / 4); offset < maxOffset; offset += length / 6 + MysteriumPatchesFixesCave.rand.nextInt(length / 4) + 1) {
                            dirSwitch += 4;
                            int x2 = getOffsetX(z2, y2, offset);
                            int z3 = getOffsetZ(z1, y2, offset);
                            int direction2 = y2 + dirSwitch;
                            int length2 = length / 3 + MysteriumPatchesFixesCave.rand.nextInt(length / 3);
                            if ((direction2 & 0x1) == 0x1) {
                                length2 -= offset / 4;
                            }
                            generateMazeCaveSegment(x2, i, z3, direction2, length2, height);
                            if (linkCave && d == 0) {
                                linkCave = false;
                                x2 = getOffsetX(x2, direction2, length2);
                                z3 = getOffsetZ(z3, direction2, length2);
                                generateHorizontalLinkCave(x2, i, z3, centerX, centerZ, 2 - clusterType);
                            }
                            if (offset > length / 2) {
                                offset += MysteriumPatchesFixesCave.rand.nextInt(length / 6) + 2;
                                x2 = getOffsetX(z2, y2, offset);
                                z3 = getOffsetZ(z1, y2, offset);
                                direction2 += 4;
                                length2 = length / 3 + MysteriumPatchesFixesCave.rand.nextInt(length / 3);
                                if ((direction2 & 0x1) == 0x1) {
                                    length2 -= offset / 4;
                                }
                                generateMazeCaveSegment(x2, i, z3, direction2, length2, height);
                            }
                        }
                    }
                    ++mazeType;
                    break;
                }
            }
            ++quadrant;
            y += yInc;
            if (caveIndex == mazeIndex1) {
                caveType = 3;
                int x = centerX + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantX(quadrant);
                int y2 = MysteriumPatchesFixesCave.rand.nextInt(16) + 16;
                int z2 = centerZ + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantZ(quadrant);
                generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y2, 100, z2, MysteriumPatchesFixesCave.rand.nextFloat(), centerX, centerZ, 24);
                generateHorizontalLinkCave(x, y2, z2, centerX, centerZ, 0);
                generateCircularRoom(x, y2, z2, MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 4.0f + 4.0f);
                ++quadrant;
                x = centerX + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantX(quadrant);
                y2 = MysteriumPatchesFixesCave.rand.nextInt(16) + 32;
                z2 = centerZ + (centerOffset + MysteriumPatchesFixesCave.rand.nextInt(5)) * getQuadrantZ(quadrant);
                generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y2, 100, z2, MysteriumPatchesFixesCave.rand.nextFloat(), centerX, centerZ, 24);
                ++quadrant;
                if (mazeIndex1 > 0 && mazeIndex2 > 0) {
                    for (int i = MysteriumPatchesFixesCave.rand.nextInt(3) + 4; i > 0; --i) {
                        x = centerX + (centerOffset + 8 + MysteriumPatchesFixesCave.rand.nextInt(16)) * getQuadrantX(quadrant);
                        z2 = centerZ + (centerOffset + 8 + MysteriumPatchesFixesCave.rand.nextInt(16)) * getQuadrantZ(quadrant);
                        generateDirectionalCave(x, 3, z2, centerX, centerZ, 0);
                        ++quadrant;
                    }
                }
            }
            else if (caveIndex == mazeIndex2) {
                caveType = 3;
            }
            else {
                caveType = (caveType + 1) % 3;
            }
        }
    }

    private static int validCaveClusterLocation(final int chunkX, final int chunkZ, final boolean includeCaves, final boolean firstCheck) {
        byte type = 3;
        int count = 0;
        for (int radius2 = -3; radius2 <= 3; ++radius2) {
            for (int x = -3; x <= 3; ++x) {
                final int z = radius2 * radius2 + x * x;
                if (z <= 13) {
                    int x2z2 = chunkX + radius2;
                    int chunkOffZ = chunkZ + x;
                    if (includeCaves && z != 0) {
                        MysteriumPatchesFixesCave.caveRNG.setSeed((x2z2 * 341873128712L + chunkOffZ * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
                        if (MysteriumPatchesFixesCave.caveRNG.nextInt(15) == 0) {
                            final int chunkModX = MysteriumPatchesFixesCave.caveRNG.nextInt(MysteriumPatchesFixesCave.caveRNG.nextInt(MysteriumPatchesFixesCave.caveRNG.nextInt(40) + 1) + 1);
                            if (chunkModX > 0) {
                                if (!firstCheck || z <= 5) {
                                    return 0;
                                }
                                count += chunkModX;
                                if (type == 3) {
                                    type = 2;
                                }
                                if (type == 2 && count > 10) {
                                    type = 1;
                                }
                            }
                        }
                    }
                    MysteriumPatchesFixesCave.caveRNG.setSeed((x2z2 * 341873128712L + chunkOffZ * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
                    if (MysteriumPatchesFixesCave.caveRNG.nextInt(20) == 15) {
                        final int chunkModX = x2z2 + MysteriumPatchesFixesCave.caveOffsetX + 4;
                        final int chunkModZ = chunkOffZ + MysteriumPatchesFixesCave.caveOffsetZ + 4;
                        boolean ravine = false;
                        if ((chunkModX & 0x7) == 0x0 && (chunkModZ & 0x7) == 0x0 && (chunkModX & 0x8) != (chunkModZ & 0x8)) {
                            ravine = true;
                        }
                        else if (MysteriumPatchesFixesCave.caveRNG.nextInt(25) < 19 && chunkModX % 3 == 0 && chunkModZ % 3 == 0 && (chunkModX / 3 & 0x1) == (chunkModZ / 3 & 0x1)) {
                            ravine = true;
                        }
                        if (ravine || MysteriumPatchesFixesCave.caveRNG.nextInt(30) < 11) {
                            if (!firstCheck || z <= 5) {
                                return 0;
                            }
                            count += 6;
                            if (type == 3) {
                                type = 2;
                            }
                            if (type == 2 && count > 10) {
                                type = 1;
                            }
                        }
                    }
                    x2z2 += MysteriumPatchesFixesCave.mineshaftOffsetX;
                    chunkOffZ += MysteriumPatchesFixesCave.mineshaftOffsetZ;
                    if ((x2z2 / 7 & 0x1) != (chunkOffZ / 7 & 0x1)) {
                        final int chunkModX = x2z2 % 7;
                        final int chunkModZ = chunkOffZ % 7;
                        if (chunkModX <= 2 && chunkModZ <= 2) {
                            MysteriumPatchesFixesCave.caveRNG.setSeed((x2z2 / 7 * 341873128712L + chunkOffZ / 7 * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
                            if (chunkModX == MysteriumPatchesFixesCave.caveRNG.nextInt(3) && chunkModZ == MysteriumPatchesFixesCave.caveRNG.nextInt(3)) {
                                if (!firstCheck || z <= 5) {
                                    return 0;
                                }
                                count += 6;
                                if (type == 3) {
                                    type = 2;
                                }
                                if (type == 2 && count > 10) {
                                    type = 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!includeCaves) {
            return 1;
        }
        if (type > 1 && firstCheck) {
            final int radius2 = type * type + 1;
            for (int x = -type; x <= type; ++x) {
                for (int z = -type; z <= type; ++z) {
                    final int x2z2 = x * x + z * z;
                    if (x2z2 > 0 && x2z2 <= radius2) {
                        MysteriumPatchesFixesCave.caveRNG.setSeed(((chunkX + x) * 341873128712L + (chunkZ + z) * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
                        if (MysteriumPatchesFixesCave.caveRNG.nextInt(15) == 0 && MysteriumPatchesFixesCave.caveRNG.nextInt(MysteriumPatchesFixesCave.caveRNG.nextInt(MysteriumPatchesFixesCave.caveRNG.nextInt(40) + 1) + 1) == 0 && validCaveClusterLocation(chunkX + x, chunkZ + z, true, false) == 3) {
                            return (type == 2) ? 4 : 1;
                        }
                    }
                }
            }
        }
        return type;
    }

    private static void generateCaveSystem(final int size, int centerX, int centerZ, final float widthMultiplier, final float curviness, final boolean largerLargeCaves, final int largeCaveChance, final boolean largerCircularRooms, final int circularRoomChance) {
        byte spread = 16;
        if (curviness >= 0.15f) {
            spread = 32;
            centerX -= 8;
            centerZ -= 8;
        }
        for (int i = 0; i < size; ++i) {
            final double x = centerX + MysteriumPatchesFixesCave.rand.nextInt(spread);
            double y = MysteriumPatchesFixesCave.rand.nextInt(MysteriumPatchesFixesCave.rand.nextInt(120) + 8) - 7;
            final double z = centerZ + MysteriumPatchesFixesCave.rand.nextInt(spread);
            int caves = 1;
            if (MysteriumPatchesFixesCave.rand.nextInt(circularRoomChance) == 0) {
                final int startDirection = MysteriumPatchesFixesCave.rand.nextInt(4);
                caves += startDirection;
                float j = MysteriumPatchesFixesCave.rand.nextFloat() * 6.0f + 1.0f;
                if (largerCircularRooms && MysteriumPatchesFixesCave.rand.nextInt(16 / circularRoomChance) == 0) {
                    j = j * (MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() + 1.0f) + 3.0f;
                    if (j > 8.5f) {
                        caves += 2;
                        if (y < 4.5) {
                            y += 12.0;
                        }
                        else if (y > 62.5) {
                            y -= 47.0;
                            if (y > 62.5) {
                                y -= 20.0;
                            }
                        }
                    }
                }
                if (widthMultiplier < 1.0f) {
                    if (j > 8.5f) {
                        j *= widthMultiplier;
                    }
                }
                else {
                    j *= widthMultiplier;
                    if (startDirection == 0 && j > 10.0f && j < 17.0f) {
                        float width = (j - 10.0f) / 7.0f + WGConfig.widthAdditionNormal;
                        width = Math.max((j - 10.0f) / 10.0f, width);
                        if (WGConfig.hardLimitsEnabled) {
                            width = Math.min(width, (float)WGConfig.widthMaxNormal);
                            width = Math.max(width, (float)WGConfig.widthMinNormal);
                        }
                        j *= MysteriumPatchesFixesCave.rand.nextFloat() * (1.0f - width) + 1.0f + width;
                    }
                    if (j > 15.5f) {
                        if (y < 4.5) {
                            y += 12.0;
                        }
                        else if (y > 62.5) {
                            y -= 47.0;
                            if (y > 62.5) {
                                y -= 20.0;
                            }
                        }
                        if (y < 9.5) {
                            y += j / 8.0f + 0.5f;
                        }
                        else if (y > 52.5) {
                            y -= j / 4.0f + 0.5f;
                        }
                    }
                }
                generateCircularRoom(x, y, z, j);
            }
            final float var23 = MysteriumPatchesFixesCave.rand.nextFloat() * 6.2831855f;
            for (int var24 = 0; var24 < caves; ++var24) {
                float width = MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f + MysteriumPatchesFixesCave.rand.nextFloat() + WGConfig.widthAdditionNormal;
                width = Math.max(MysteriumPatchesFixesCave.rand.nextFloat() * 1.5f, width);
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, (float)WGConfig.widthMaxNormal);
                    width = Math.max(width, (float)WGConfig.widthMinNormal);
                }
                if (MysteriumPatchesFixesCave.rand.nextInt(largeCaveChance) == 0) {
                    width *= MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 4.0f + 1.0f;
                    if (largerLargeCaves) {
                        if (widthMultiplier < 1.0f) {
                            if (width > 7.5f) {
                                width *= widthMultiplier;
                            }
                        }
                        else if (width < 7.5f) {
                            width *= widthMultiplier;
                        }
                    }
                    else if (width > 8.5f) {
                        width = 8.5f;
                    }
                }
                else {
                    width *= widthMultiplier;
                }
                float direction = var23;
                if (var24 > 0) {
                    direction = var23 + 6.2831855f * var24 / caves + (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) * 6.2831855f / caves;
                }
                generateCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, 0, curviness);
            }
        }
    }

    private static void generateColossalCaveSystem(int centerX, int centerZ) {
        if (MysteriumPatchesFixesCave.rand.nextInt(100) + 1 <= WGConfig.caveColossalReductionPercentage) {
            return;
        }
        centerX *= 16;
        centerZ *= 16;
        final int caveType = MysteriumPatchesFixesCave.rand.nextInt(5);
        int caveCounter = MysteriumPatchesFixesCave.rand.nextInt(200);
        if (caveType < 4) {
            final int z = MysteriumPatchesFixesCave.rand.nextInt(2);
            for (int x = 0; x < 8; ++x) {
                int x2z2 = centerX;
                int offset = centerZ;
                switch (caveType) {
                    case 0: {
                        offset = centerZ + (x * 16 - 56);
                        break;
                    }
                    case 1: {
                        x2z2 = centerX + (x * 16 - 56);
                        break;
                    }
                    case 2: {
                        x2z2 = centerX + (x * 11 - 38);
                        offset = centerZ + (x * 12 - 42);
                        break;
                    }
                    case 3: {
                        x2z2 = centerX + (38 - x * 11);
                        offset = centerZ + (x * 12 - 42);
                        break;
                    }
                }
                final int size = (x + z & 0x1) + 12;
                if (caveType == 1) {
                    caveCounter = generateCCCaveSystem(size, x2z2, offset - 8, caveCounter);
                    caveCounter = generateCCCaveSystem(25 - size, x2z2, offset + 8, caveCounter);
                }
                else {
                    caveCounter = generateCCCaveSystem(size, x2z2 - 8, offset, caveCounter);
                    caveCounter = generateCCCaveSystem(25 - size, x2z2 + 8, offset, caveCounter);
                }
            }
        }
        else {
            for (int z = -2; z <= 2; ++z) {
                for (int x = -2; x <= 2; ++x) {
                    final int x2z2 = x * x + z * z;
                    if (x2z2 > 0 && x2z2 <= 5) {
                        final int offset = (x != 0 && z != 0) ? 16 : 20;
                        caveCounter = generateCCCaveSystem(10, centerX + x * offset, centerZ + z * offset, caveCounter);
                    }
                }
            }
        }
        centerX += 8;
        centerZ += 8;
        for (int z = -32; z <= 32; z += 64) {
            for (int x = -32; x <= 32; x += 64) {
                generateHorizontalLinkCave(centerX + x, MysteriumPatchesFixesCave.rand.nextInt(15) + 15, centerZ + z, centerX, centerZ, 4);
            }
        }
    }

    private static int generateCCCaveSystem(final int size, final int centerX, final int centerZ, int caveCounter) {
        for (int i = 0; i < size; ++i) {
            final double x = centerX + MysteriumPatchesFixesCave.rand.nextInt(16);
            final double z = centerZ + MysteriumPatchesFixesCave.rand.nextInt(16);
            final int index12 = caveCounter % 12;
            double y = -7.0;
            if (index12 < 9) {
                int width = Math.max(caveCounter % 3, caveCounter % 3 + WGConfig.widthAdditionColossal);
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, WGConfig.widthMaxColossal);
                    width = Math.max(width, WGConfig.widthMinColossal);
                }
                if (width < 2) {
                    y += width * 10 + MysteriumPatchesFixesCave.rand.nextInt(10);
                }
                else {
                    y += index12 * 3 + 14 + MysteriumPatchesFixesCave.rand.nextInt(9);
                }
            }
            else if (index12 == 9) {
                y += 47 + MysteriumPatchesFixesCave.rand.nextInt(11);
            }
            else if (index12 == 10) {
                y += 58 + MysteriumPatchesFixesCave.rand.nextInt(13);
            }
            else {
                y += 71 + MysteriumPatchesFixesCave.rand.nextInt(20);
            }
            if (caveCounter % 7 == 0) {
                generateCircularRoom(x, y, z, MysteriumPatchesFixesCave.rand.nextFloat() * 5.0f + 2.0f);
            }
            float var14 = MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f + MysteriumPatchesFixesCave.rand.nextFloat();
            if (caveCounter % 19 == 0) {
                if (var14 < 1.5f) {
                    var14 += (var14 + 3.0f) / 3.0f;
                }
                ++var14;
            }
            generateCCCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, var14, MysteriumPatchesFixesCave.rand.nextFloat() * 6.2831855f, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, caveCounter % 5 == 0, centerX + 8, centerZ + 8);
            ++caveCounter;
        }
        return caveCounter;
    }

    private static void generateCCCave(long seed, double x, double y, double z, float width, float directionXZ, float directionY, int pos, boolean isVerticalCave, final double centerX, final double centerZ) {
        MysteriumPatchesFixesCave.caveRNG.setSeed(seed);
        float var23 = 0.0f;
        float var24 = 0.0f;
        final int branchPoint = (pos == 0) ? 49 : -999;
        while (pos < 98) {
            if (pos == branchPoint) {
                seed = MysteriumPatchesFixesCave.caveRNG.nextLong();
                width = MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.5f + 0.5f;
                isVerticalCave = (MysteriumPatchesFixesCave.caveRNG.nextInt(6) == 0);
                directionY /= 3.0f;
                generateCCCave(MysteriumPatchesFixesCave.caveRNG.nextLong(), x, y, z, MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.5f + 0.5f, directionXZ - 1.5707964f, directionY, pos, MysteriumPatchesFixesCave.caveRNG.nextInt(6) == 0, centerX, centerZ);
                generateCCCave(seed, x, y, z, width, directionXZ + 1.5707964f, directionY, pos, isVerticalCave, centerX, centerZ);
                return;
            }
            double radiusW = 1.5f + sine(pos * 0.0320571f) * width;
            final double var25 = x - MysteriumPatchesFixesCave.chunkCenterX;
            final double var26 = z - MysteriumPatchesFixesCave.chunkCenterZ;
            final double var27 = 116 - pos + radiusW;
            if (var25 * var25 + var26 * var26 > var27 * var27) {
                return;
            }
            if (MysteriumPatchesFixesCave.caveRNG.nextInt(4) == 0) {
                radiusW = radiusW / 5.0 + 0.75;
            }
            final float var28 = cosine(directionY);
            x += cosine(directionXZ) * var28;
            y += sine(directionY);
            z += sine(directionXZ) * var28;
            if (isVerticalCave) {
                directionY *= 0.92f;
            }
            else {
                directionY *= 0.7f;
            }
            final float devX = (float)(x - centerX);
            final float devZ = (float)(z - centerZ);
            if (devX * devX + devZ * devZ > 576.0f) {
                if (devZ >= 0.0f) {
                    if (devX >= 0.0f) {
                        directionXZ = (directionXZ * 31.0f - 2.35619f) / 32.0f;
                    }
                    else {
                        directionXZ = (directionXZ * 31.0f - 0.7853982f) / 32.0f;
                    }
                }
                else if (devX >= 0.0f) {
                    directionXZ = (directionXZ * 31.0f + 2.35619f) / 32.0f;
                }
                else {
                    directionXZ = (directionXZ * 31.0f + 0.7853982f) / 32.0f;
                }
            }
            directionY += var24 * 0.1f;
            directionXZ += var23 * 0.1f;
            var24 *= 0.9f;
            var23 *= 0.75f;
            var24 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 2.0f;
            var23 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 4.0f;
            final double radiusW_2 = radiusW + 9.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - radiusW_2 && x <= MysteriumPatchesFixesCave.chunkCenterX + radiusW_2 && z >= MysteriumPatchesFixesCave.chunkCenterZ - radiusW_2 && z <= MysteriumPatchesFixesCave.chunkCenterZ + radiusW_2) {
                final double noiseMultiplier = 0.275 / Math.max(radiusW - 1.0, 0.916666);
                generateCaveSegment(x, y, z, radiusW, radiusW, noiseMultiplier, 1);
            }
            ++pos;
        }
    }

    private static void generateSpecialCaveSystems(final int chunkX, final int chunkZ, final int type) {
        final int centerX = chunkX * 16 + 8;
        final int centerZ = chunkZ * 16 + 8;
        if (type == 2) {
            if ((chunkX + MysteriumPatchesFixesCave.caveOffsetX - 15 & 0x20) == (chunkZ + MysteriumPatchesFixesCave.caveOffsetZ - 15 & 0x20)) {
                if (MysteriumPatchesFixesCave.rand.nextInt(100) + 1 <= WGConfig.caveCircularRoomReductionPercentage) {
                    return;
                }
                generateCircularRoomCaveSystem(centerX, centerZ);
            }
            else {
                if (MysteriumPatchesFixesCave.rand.nextInt(100) + 1 <= WGConfig.caveRavineCaveReductionPercentage) {
                    return;
                }
                generateRavineCaveSystem(centerX, centerZ);
            }
        }
        else if (type == 6) {
            if (MysteriumPatchesFixesCave.rand.nextInt(100) + 1 <= WGConfig.caveVerticalReductionPercentage) {
                return;
            }
            generateVerticalCaveSystem(centerX, centerZ);
        }
        else {
            if (MysteriumPatchesFixesCave.rand.nextInt(100) + 1 <= WGConfig.caveMazeReductionPercentage) {
                return;
            }
            generateMazeCaveSystem(centerX, centerZ);
        }
    }

    private static void generateCircularRoomCaveSystem(final int centerX, final int centerZ) {
        final int caveSize = MysteriumPatchesFixesCave.rand.nextInt(15) + 35;
        final double yInc = 39.0 / caveSize;
        double y = 0.0;
        final int offset = MysteriumPatchesFixesCave.rand.nextInt(4);
        final int centerCave = MysteriumPatchesFixesCave.rand.nextInt(2);
        for (int i = 0; i < caveSize; ++i) {
            int x2z2;
            int x;
            int z;
            do {
                x = MysteriumPatchesFixesCave.rand.nextInt(33);
                z = MysteriumPatchesFixesCave.rand.nextInt(33);
                x2z2 = x * x + z * z;
            } while (x2z2 < 101 || x2z2 > 1025);
            x *= getQuadrantX(i + offset);
            z *= getQuadrantZ(i + offset);
            x += centerX;
            y += (i / (double)(caveSize - 1) + 1.0) * yInc;
            z += centerZ;
            generateCircularRoom(x, y, z, MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 9.0f + 3.0f);
            generateDirectionalCave(x, (int)(y + 0.5), z, centerX, centerZ, 16);
            if (i < 2) {
                x = centerX + (MysteriumPatchesFixesCave.rand.nextInt(9) + 8) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                final int y2 = MysteriumPatchesFixesCave.rand.nextInt(8) + i * 16 + 16;
                z = centerZ + (MysteriumPatchesFixesCave.rand.nextInt(9) + 8) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                generateVerticalCave(x, y2, 100, z);
                generateCircularRoom(x, y2, z, MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 9.0f + 3.0f);
                generateDirectionalCave(x, y2, z, centerX, centerZ, 999);
                generateHorizontalLinkCave(x, y2, z, centerX, centerZ, 0);
            }
            if ((i & 0x7) == centerCave) {
                x = centerX + (MysteriumPatchesFixesCave.rand.nextInt(6) + 3) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                double var16 = y + (MysteriumPatchesFixesCave.rand.nextInt(9) - 4);
                if (var16 < 0.0) {
                    var16 += 4.0;
                }
                z = centerZ + (MysteriumPatchesFixesCave.rand.nextInt(6) + 3) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                generateCircularRoom(x, y, z, MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 9.0f + 3.0f);
                if (i == centerCave) {
                    generateVerticalCave(x, 3, 32, z);
                }
            }
        }
    }

    private static void generateRavineCaveSystem(final int centerX, final int centerZ) {
        final int caveSize = MysteriumPatchesFixesCave.rand.nextInt(10) + 30;
        final double yInc = 39.0 / caveSize;
        double y = 0.0;
        final int offset = MysteriumPatchesFixesCave.rand.nextInt(4);
        final int vertCave1 = caveSize / 4 + MysteriumPatchesFixesCave.rand.nextInt(3);
        final int vertCave2 = caveSize / 3 + MysteriumPatchesFixesCave.rand.nextInt(3);
        final int centerCave = MysteriumPatchesFixesCave.rand.nextInt(3);
        for (int i = 0; i < caveSize; ++i) {
            int x2z2;
            int x;
            int z;
            do {
                x = MysteriumPatchesFixesCave.rand.nextInt(33);
                z = MysteriumPatchesFixesCave.rand.nextInt(33);
                x2z2 = x * x + z * z;
            } while (x2z2 < 145 || x2z2 > 1025);
            x *= getQuadrantX(i + offset);
            z *= getQuadrantZ(i + offset);
            x += centerX;
            y += (i / (double)(caveSize - 1) + 1.0) * yInc;
            z += centerZ;
            generateRavineCave(x, y, z, 2.0f);
            if (i == vertCave1 || i == vertCave2) {
                generateVerticalCave(x, (int)(y + 0.5), 100, z);
                generateHorizontalLinkCave(x, (int)(y + 0.5), z, centerX, centerZ, 0);
            }
            if (i % 7 == centerCave) {
                x = centerX + (MysteriumPatchesFixesCave.rand.nextInt(6) + 3) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                double y2 = y + (MysteriumPatchesFixesCave.rand.nextInt(5) - 2);
                if (y2 < 0.0) {
                    y2 += 4.0;
                }
                z = centerZ + (MysteriumPatchesFixesCave.rand.nextInt(6) + 3) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                final int length = MysteriumPatchesFixesCave.rand.nextInt(17) + 26;
                int segmentLength = length + MysteriumPatchesFixesCave.rand.nextInt(2) * 8;
                final float width = MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() + 1.0f;
                final float height = MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f + 2.0f;
                final float direction = MysteriumPatchesFixesCave.rand.nextFloat() * 0.7853982f + i * 0.112199f;
                final float directionY = (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f;
                final float slope = (MysteriumPatchesFixesCave.rand.nextFloat() * 0.75f + 0.25f) * 0.25f * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                generateRavineCaveSegment(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction, directionY, slope, segmentLength, height);
                segmentLength = length + MysteriumPatchesFixesCave.rand.nextInt(2) * 8;
                generateRavineCaveSegment(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction + 3.1415927f, -directionY, -slope, segmentLength, height);
            }
        }
    }

    private static void generateVerticalCaveSystem(final int centerX, final int centerZ) {
        final int caveSize = MysteriumPatchesFixesCave.rand.nextInt(15) + 45;
        final double yInc = 39.0 / caveSize;
        double y = 0.0;
        final int offset = MysteriumPatchesFixesCave.rand.nextInt(4);
        final int horizCave = MysteriumPatchesFixesCave.rand.nextInt(3);
        final int deepHorizCave = 6 + MysteriumPatchesFixesCave.rand.nextInt(3);
        final int vertCave1 = caveSize / 3 + (MysteriumPatchesFixesCave.rand.nextInt(5) - 2) * 2;
        final int vertCave2 = caveSize / 3 + (MysteriumPatchesFixesCave.rand.nextInt(5) - 2) * 2 + MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1;
        int largeCaveChance = (caveSize - 29) / 5;
        largeCaveChance += MysteriumPatchesFixesCave.rand.nextInt(8 - largeCaveChance) + 5;
        final int largeCaveOffset = MysteriumPatchesFixesCave.rand.nextInt(largeCaveChance);
        for (int i = 0; i < caveSize; ++i) {
            int x2z2;
            int x;
            int z;
            do {
                x = MysteriumPatchesFixesCave.rand.nextInt(33);
                z = MysteriumPatchesFixesCave.rand.nextInt(33);
                x2z2 = x * x + z * z;
            } while (x2z2 < 101 || x2z2 > 1025);
            x *= getQuadrantX(i + offset);
            z *= getQuadrantZ(i + offset);
            y += (i / (double)(caveSize - 1) + 1.0) * yInc;
            int y2 = (int)y;
            if (i < deepHorizCave) {
                generateDirectionalCave(centerX - x - ((x >= 0) ? 8 : -8), 3, centerZ - z - ((z >= 0) ? 8 : -8), centerX, centerZ, 0);
            }
            if (i % 3 == horizCave) {
                generateDirectionalCave(centerX + x + ((x >= 0) ? 8 : -8), y2, centerZ + z + ((z >= 0) ? 8 : -8), centerX, centerZ, 0);
            }
            if (i == vertCave1 || i == vertCave2) {
                generateHorizontalLinkCave(centerX - x, y2, centerZ - z, centerX, centerZ, 2);
            }
            y2 /= 3;
            final int minY = y2 - MysteriumPatchesFixesCave.rand.nextInt(5);
            final int maxY = y2 + 32 + MysteriumPatchesFixesCave.rand.nextInt(33 - y2);
            float width = Math.max(0.5f, MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f + WGConfig.caveVerticalReductionPercentage);
            if (i % largeCaveChance == largeCaveOffset) {
                width = Math.min(8.0f, Math.max(width * 3.0f, width * (MysteriumPatchesFixesCave.rand.nextFloat() * 3.0f + 1.0f) + 2.0f) + WGConfig.caveVerticalReductionPercentage);
            }
            x += centerX;
            z += centerZ;
            if ((i + offset & 0x4) == 0x0) {
                generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, minY, maxY, z, width, centerX, centerZ, 40);
            }
            else {
                generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, maxY - minY, 0, z, width, centerX, centerZ, 40);
            }
            if (i == vertCave1 || i == vertCave2) {
                x = centerX + (MysteriumPatchesFixesCave.rand.nextInt(6) + 3) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                z = centerZ + (MysteriumPatchesFixesCave.rand.nextInt(6) + 3) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
                if (i == vertCave1) {
                    generateVerticalCave(x, minY, 100, z);
                }
                else {
                    generateVerticalCave(x, 100, 0, z);
                }
            }
        }
    }

    private static void generateMazeCaveSystem(final int centerX, final int centerZ) {
        final boolean direction = false;
        int oldDirection = 0;
        boolean change = false;
        final int yInc = 7 + MysteriumPatchesFixesCave.rand.nextInt(2);
        final byte minY = 3;
        final byte maxY = 59;
        int oldQuadrant;
        int quadrant = oldQuadrant = MysteriumPatchesFixesCave.rand.nextInt(4);
        float height = 2.625f;
        int horizCave1 = MysteriumPatchesFixesCave.rand.nextInt(4);
        int horizCave2;
        do {
            horizCave2 = MysteriumPatchesFixesCave.rand.nextInt(4);
        } while (horizCave1 == horizCave2);
        horizCave1 = minY + (horizCave1 + 1) * yInc + MysteriumPatchesFixesCave.rand.nextInt(4);
        horizCave2 = minY + (horizCave2 + 1) * yInc + MysteriumPatchesFixesCave.rand.nextInt(4);
        int vertCave1 = maxY + MysteriumPatchesFixesCave.rand.nextInt(4);
        int vertCave2;
        do {
            vertCave2 = maxY + MysteriumPatchesFixesCave.rand.nextInt(4);
        } while (vertCave1 == vertCave2);
        int caveCount = MysteriumPatchesFixesCave.rand.nextInt(2);
        for (int caveY = minY; caveY <= maxY; caveY += yInc) {
            final int caveX = centerX + (MysteriumPatchesFixesCave.rand.nextInt(7) + 4) * getQuadrantX(quadrant);
            final int caveZ = centerZ + (MysteriumPatchesFixesCave.rand.nextInt(7) + 4) * getQuadrantZ(quadrant);
            int var31 = MysteriumPatchesFixesCave.rand.nextInt(2);
            if (change && var31 == oldDirection) {
                var31 = 1 - var31;
            }
            change = (var31 == oldDirection);
            oldDirection = var31;
            var31 += MysteriumPatchesFixesCave.rand.nextInt(4) * 2;
            for (int d = 0; d < 4; ++d) {
                var31 += 2;
                int length;
                if ((var31 & 0x1) == 0x0) {
                    length = 28 + MysteriumPatchesFixesCave.rand.nextInt(20);
                }
                else {
                    length = 20 + MysteriumPatchesFixesCave.rand.nextInt(20);
                }
                generateMazeCaveSegment(caveX, caveY, caveZ, var31, length, height);
                int dirSwitch = MysteriumPatchesFixesCave.rand.nextInt(2) * 4 + 2;
                for (int maxOffset = length * 3 / 4, offset = length / 5 + MysteriumPatchesFixesCave.rand.nextInt(length / 4); offset < maxOffset; offset += length / 6 + MysteriumPatchesFixesCave.rand.nextInt(length / 4) + 1) {
                    dirSwitch += 4;
                    int x = getOffsetX(caveX, var31, offset);
                    int z = getOffsetZ(caveZ, var31, offset);
                    int direction2 = var31 + dirSwitch;
                    int length2 = length / 3 + MysteriumPatchesFixesCave.rand.nextInt(length / 3);
                    if ((direction2 & 0x1) == 0x1) {
                        length2 -= offset / 4;
                    }
                    generateMazeCaveSegment(x, caveY, z, direction2, length2, height);
                    final int index = caveY + (direction2 / 2 & 0x3);
                    if (index != horizCave1 && index != horizCave2) {
                        if (index == vertCave1 || index == vertCave2) {
                            final int offset2 = length2 / 4 + MysteriumPatchesFixesCave.rand.nextInt(length2 / 2 + 1) + 1;
                            x = getOffsetX(x, direction2, offset2);
                            z = getOffsetZ(z, direction2, offset2);
                            if (index == vertCave1) {
                                vertCave1 = -999;
                                generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, caveY, minY + yInc * 2, z, 0.0f, centerX, centerZ, 32);
                            }
                            else {
                                vertCave2 = -999;
                                generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, caveY, 100, z, 0.0f, 0.0, 0.0, 0);
                            }
                        }
                    }
                    else {
                        if (index == horizCave1) {
                            horizCave1 = -999;
                        }
                        else {
                            horizCave2 = -999;
                        }
                        x = getOffsetX(x, direction2, length2);
                        z = getOffsetZ(z, direction2, length2);
                        generateHorizontalLinkCave(x, caveY, z, centerX, centerZ, 0);
                    }
                    if (offset > length / 2) {
                        offset += MysteriumPatchesFixesCave.rand.nextInt(length / 6) + 2;
                        x = getOffsetX(caveX, var31, offset);
                        z = getOffsetZ(caveZ, var31, offset);
                        direction2 += 4;
                        length2 = length / 3 + MysteriumPatchesFixesCave.rand.nextInt(length / 3);
                        if ((direction2 & 0x1) == 0x1) {
                            length2 -= offset / 4;
                        }
                        generateMazeCaveSegment(x, caveY, z, direction2, length2, height);
                    }
                }
            }
            int length;
            if (caveY == maxY) {
                length = 100;
            }
            else if (caveY == minY) {
                length = maxY - yInc * 2 + 1;
            }
            else {
                length = Math.min(maxY, caveY + yInc) + 1;
            }
            generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), caveX, caveY, length, caveZ, 0.0f, caveX, caveZ, 8);
            do {
                quadrant = MysteriumPatchesFixesCave.rand.nextInt(4);
            } while (quadrant == oldQuadrant);
            oldQuadrant = quadrant;
            height = 1.625f;
            ++caveCount;
        }
    }

    private static void generateRegionalCaves(final int chunkX, final int chunkZ) {
        if (MysteriumPatchesFixesCave.rand.nextInt(100) + 1 <= WGConfig.caveRegionalReductionPercentage) {
            return;
        }
        final Random r = new Random();
        r.setSeed(MysteriumPatchesFixesCave.worldObj.func_72905_C());
        if (isGiantCaveRegion(chunkX, chunkZ) || r.nextInt(100) < WGConfig.giantCaveChance) {
            final int chunkOffX = isEdgeOfGiantCaveRegion(chunkX, chunkZ);
            if (chunkOffX > 0 || (chunkX & 0x1) == (chunkZ & 0x1)) {
                int chunkOffZ;
                int div1;
                for (chunkOffZ = chunkX, div1 = chunkZ; validRegionalCaveLocation(chunkOffZ - 1, div1, 4096); --chunkOffZ) {}
                while (validRegionalCaveLocation(chunkOffZ, div1 - 1, 4096)) {
                    --div1;
                }
                int div2 = 0;
                float startY = 0.0f;
                int verticalCave = 0;
                for (int x = div1; x < div1 + 12; ++x) {
                    for (int direction = chunkOffZ; direction < chunkOffZ + 12; ++direction) {
                        if ((direction & 0x1) == (x & 0x1) || isEdgeOfGiantCaveRegion(direction, x) > 0) {
                            MysteriumPatchesFixesCave.caveRNG.setSeed((direction * 341873128712L + x * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
                            div2 += MysteriumPatchesFixesCave.caveRNG.nextInt(65);
                            startY += MysteriumPatchesFixesCave.caveRNG.nextFloat() * MysteriumPatchesFixesCave.caveRNG.nextFloat();
                            ++verticalCave;
                        }
                    }
                }
                div2 = Math.round(32.0f - div2 / (float)verticalCave) + MysteriumPatchesFixesCave.rand.nextInt(65) + 112;
                startY = 0.25f / (startY / verticalCave) * MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * ((chunkOffX > 0) ? 12.0f : 11.3333f) + ((chunkOffX > 0) ? 3.0f : 3.66667f);
                int x = MysteriumPatchesFixesCave.rand.nextInt(div2 / 4) + div2 / 2;
                float var25;
                if (chunkOffX > 0) {
                    var25 = (chunkOffX - 1) * 0.7853982f;
                }
                else {
                    var25 = MysteriumPatchesFixesCave.rand.nextFloat() * 6.2831855f;
                }
                chunkOffZ = chunkX * 16 + 8;
                div1 = chunkZ * 16 + 8;
                final int y = MysteriumPatchesFixesCave.rand.nextInt(11) + 25 * ((chunkOffX > 0) ? (chunkX + chunkZ & 0x1) : (chunkZ & 0x1)) + 10;
                generateLargeCave2(MysteriumPatchesFixesCave.rand.nextLong(), chunkOffZ, y, div1, startY, var25, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, div2, x, 0.1f, true, false);
                if (chunkOffX > 0 && (chunkOffX & 0x1) == 0x0) {
                    div2 = 64 + MysteriumPatchesFixesCave.rand.nextInt(16);
                    generateHorizontalCave(MysteriumPatchesFixesCave.rand.nextLong(), chunkOffZ, y, div1, MysteriumPatchesFixesCave.rand.nextFloat() + 1.0f, var25 + 3.1415927f, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, div2, div2 + 32 + MysteriumPatchesFixesCave.rand.nextInt(8), 1);
                }
                if (MysteriumPatchesFixesCave.rand.nextInt((chunkOffX > 0) ? 12 : 6) == 0) {
                    generateVerticalCave(chunkOffZ, y, 100, div1);
                }
            }
        }
        else {
            final int chunkOffX = chunkX + MysteriumPatchesFixesCave.caveOffsetX;
            final int chunkOffZ = chunkZ + MysteriumPatchesFixesCave.caveOffsetZ;
            byte var26 = 2;
            byte var27 = 3;
            if ((chunkOffX & 0x40) == (chunkOffZ & 0x40)) {
                var26 = 3;
                var27 = 2;
            }
            MysteriumPatchesFixesCave.caveRNG.setSeed((chunkOffX / var26 * 341873128712L + chunkOffZ / var27 * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
            if (chunkOffX % var26 == MysteriumPatchesFixesCave.caveRNG.nextInt(var26) && chunkOffZ % var27 == MysteriumPatchesFixesCave.caveRNG.nextInt(var27)) {
                final int var28 = 10 + (chunkOffX / var27 + chunkOffZ / var26) % 3 * 20;
                final boolean var29 = MysteriumPatchesFixesCave.rand.nextBoolean();
                final double var30 = chunkX * 16 + MysteriumPatchesFixesCave.rand.nextInt(16);
                final double var31 = var28;
                final double z = chunkZ * 16 + MysteriumPatchesFixesCave.rand.nextInt(16);
                float direction2 = MysteriumPatchesFixesCave.rand.nextFloat() * 6.2831855f;
                int segments = MysteriumPatchesFixesCave.rand.nextInt(3) + 2;
                float width = Math.max(1.0f, (!var29 && segments == 2) ? 1.0f : (MysteriumPatchesFixesCave.rand.nextInt(2) + 1 + (float)WGConfig.widthAdditionRegional));
                width = MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * width + 0.5f;
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, (float)WGConfig.widthMaxRegional);
                    width = Math.max(width, (float)WGConfig.widthMinRegional);
                }
                for (int i = 0; i < segments; ++i) {
                    float segmentDirection = direction2;
                    direction2 += 6.2831855f / segments;
                    if (segments > 2) {
                        segmentDirection += (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) * 2.094395f / segments;
                    }
                    if (i > 0 && (var29 || segments > 2)) {
                        width = Math.max(0.6f, MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * (MysteriumPatchesFixesCave.rand.nextInt(2) + 1) + 0.5f + WGConfig.widthAdditionRegional);
                        if (WGConfig.hardLimitsEnabled) {
                            width = Math.min(width, (float)WGConfig.widthMaxRegional);
                            width = Math.max(width, (float)WGConfig.widthMinRegional);
                        }
                    }
                    generateHorizontalCave(MysteriumPatchesFixesCave.rand.nextLong(), var30, var31, z, width, segmentDirection, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, 112 + MysteriumPatchesFixesCave.rand.nextInt(65), 2);
                }
                if (var29) {
                    generateVerticalCave(var30, var28, 100, z);
                    ++segments;
                }
                if (segments > 2) {
                    generateCircularRoom(var30, var31, z, (MysteriumPatchesFixesCave.rand.nextFloat() + 0.5f) * segments + 1.0f);
                }
                if (!var29 && validCaveClusterLocation(chunkX, chunkZ, false, true) > 0) {
                    generateCaveCluster(chunkX, chunkZ, 1);
                }
            }
        }
    }

    private static void generateCave(long seed, double x, double y, double z, float width, float directionXZ, float directionY, int pos, int length, final float curviness) {
        MysteriumPatchesFixesCave.caveRNG.setSeed(seed);
        float var23 = 0.0f;
        float var24 = 0.0f;
        int branchPoint = -999;
        if (pos <= 0) {
            length = 112 - MysteriumPatchesFixesCave.caveRNG.nextInt(28);
            if (width >= 1.0f) {
                branchPoint = MysteriumPatchesFixesCave.caveRNG.nextInt(length / 2) + length / 4;
            }
        }
        final boolean isVerticalCave = MysteriumPatchesFixesCave.caveRNG.nextInt(6) == 0;
        while (pos < length) {
            if (pos == branchPoint) {
                seed = MysteriumPatchesFixesCave.caveRNG.nextLong();
                width = MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.5f + 0.5f + WGConfig.widthAdditionBranchPoint;
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, (float)WGConfig.widthMaxBranchPoint);
                    width = Math.max(width, (float)WGConfig.widthMinBranchPoint);
                }
                directionY /= 3.0f;
                generateCave(MysteriumPatchesFixesCave.caveRNG.nextLong(), x, y, z, MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.5f + 0.5f, directionXZ - 1.5707964f, directionY, pos, length, curviness);
                generateCave(seed, x, y, z, width, directionXZ + 1.5707964f, directionY, pos, length, curviness);
                return;
            }
            double radiusW = 1.5f + sine(pos * 3.1415927f / length) * width;
            final double var25 = x - MysteriumPatchesFixesCave.chunkCenterX;
            final double var26 = z - MysteriumPatchesFixesCave.chunkCenterZ;
            final double var27 = length - pos + 18 + radiusW;
            if (var25 * var25 + var26 * var26 > var27 * var27) {
                return;
            }
            if (MysteriumPatchesFixesCave.caveRNG.nextInt(4) == 0) {
                radiusW = radiusW / 5.0 + 0.75;
            }
            final float var28 = cosine(directionY);
            x += cosine(directionXZ) * var28;
            y += sine(directionY);
            z += sine(directionXZ) * var28;
            if (isVerticalCave) {
                directionY *= 0.92f;
            }
            else {
                directionY *= 0.7f;
            }
            directionY += var24 * 0.1f;
            directionXZ += var23 * curviness;
            var24 *= 0.9f;
            var23 *= 0.75f;
            var24 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 2.0f;
            var23 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 4.0f;
            final double radiusW_2 = radiusW + 9.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - radiusW_2 && x <= MysteriumPatchesFixesCave.chunkCenterX + radiusW_2 && z >= MysteriumPatchesFixesCave.chunkCenterZ - radiusW_2 && z <= MysteriumPatchesFixesCave.chunkCenterZ + radiusW_2) {
                final double noiseMultiplier = 0.275 / Math.max(radiusW - 1.0, 0.916666);
                generateCaveSegment(x, y, z, radiusW, radiusW, noiseMultiplier, 1);
            }
            ++pos;
        }
    }

    private static void generateCircularRoom(double x, double y, double z, float width) {
        final long seed = MysteriumPatchesFixesCave.rand.nextLong();
        ++width;
        width = Math.max(1.0f, width + WGConfig.widthAdditionCircular);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, (float)WGConfig.widthMaxCircular);
            width = Math.max(width, (float)WGConfig.widthMinCircular);
        }
        final double var35 = x - MysteriumPatchesFixesCave.chunkCenterX;
        final double var36 = z - MysteriumPatchesFixesCave.chunkCenterZ;
        final double var37 = width + 18.0f;
        if (var35 * var35 + var36 * var36 <= var37 * var37) {
            MysteriumPatchesFixesCave.caveRNG.setSeed(seed);
            x += MysteriumPatchesFixesCave.caveRNG.nextFloat() - 0.5f;
            y += MysteriumPatchesFixesCave.caveRNG.nextFloat() - 0.5f;
            z += MysteriumPatchesFixesCave.caveRNG.nextFloat() - 0.5f;
            final double radiusW_2 = width + 9.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - radiusW_2 && x <= MysteriumPatchesFixesCave.chunkCenterX + radiusW_2 && z >= MysteriumPatchesFixesCave.chunkCenterZ - radiusW_2 && z <= MysteriumPatchesFixesCave.chunkCenterZ + radiusW_2) {
                final double noiseMultiplier = 0.33 / Math.max(width - 1.0f, 1.1);
                generateCaveSegment(x, y, z, width, width / 2.0f, noiseMultiplier, 1);
            }
        }
    }

    private static void generateSingleCave(int x, final int y, int z, final float curviness) {
        x += MysteriumPatchesFixesCave.rand.nextInt(16);
        z += MysteriumPatchesFixesCave.rand.nextInt(16);
        final float width = MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 5.0f + 0.5f;
        generateCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, MysteriumPatchesFixesCave.rand.nextFloat() * 6.2831855f, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, 0, curviness);
    }

    private static int generateLargeCave(final int chunkX, final int chunkZ, final int type) {
        int length;
        float width;
        if (type == 0) {
            length = 224 + MysteriumPatchesFixesCave.largeCaveRNG.nextInt(113);
            width = MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * 8.0f + MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * 6.0f + 10.0f + WGConfig.widthAdditionColossal;
            if (WGConfig.hardLimitsEnabled) {
                width = Math.min(width, (float)WGConfig.widthMaxColossal);
                width = Math.max(width, (float)WGConfig.widthMinColossal);
            }
            if (MysteriumPatchesFixesCave.largeCaveRNG.nextBoolean()) {
                width *= length / 224.0f;
            }
        }
        else {
            length = Math.min(112 + MysteriumPatchesFixesCave.largeCaveRNG.nextInt(MysteriumPatchesFixesCave.largeCaveRNG.nextInt(336) + 1), 336);
            MysteriumPatchesFixesCave.caveRNG.setSeed(((chunkX + MysteriumPatchesFixesCave.caveOffsetX + 12) / 16 * 341873128712L + (chunkZ + MysteriumPatchesFixesCave.caveOffsetZ + 12) / 16 * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
            width = Math.max(MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * MysteriumPatchesFixesCave.largeCaveRNG.nextFloat(), MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() + WGConfig.widthAdditionColossal);
            if (MysteriumPatchesFixesCave.caveRNG.nextBoolean()) {
                width = width * 8.0f + 2.0f;
            }
            else {
                width = width * 2.66667f + 2.66667f;
            }
            if (MysteriumPatchesFixesCave.largeCaveRNG.nextBoolean()) {
                final float x = MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * length / 96.0f + (672 - length) / 672.0f;
                if (x > 1.0f) {
                    width *= x;
                }
            }
            else {
                final float x = MysteriumPatchesFixesCave.largeCaveRNG.nextFloat();
                width *= x * x * 3.0f + 1.0f;
            }
        }
        final double x2 = chunkX * 16 + 8;
        double y = MysteriumPatchesFixesCave.largeCaveRNG.nextInt(16) + 15;
        final double z = chunkZ * 16 + 8;
        if (y < 20.5) {
            y += (width + 0.5f) / 4.0f;
        }
        int branchPoint = MysteriumPatchesFixesCave.largeCaveRNG.nextInt(length / 4) + length / 2;
        final float direction = MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * 6.2831855f;
        final float curviness = length / 3360.0f + 0.05f;
        if (type == 0) {
            int ret = length - branchPoint;
            if (WGConfig.hardLimitsEnabled) {
                width = Math.min(width, (float)WGConfig.widthMaxColossal);
                width = Math.max(width, (float)WGConfig.widthMinColossal);
            }
            generateLargeCave2(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), x2, y, z, width, direction, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) / 4.0f, ret, length, 0, curviness, false, true);
            length += ret;
            ret = length / 2;
            branchPoint = ret * 3 / 2 + MysteriumPatchesFixesCave.largeCaveRNG.nextInt(ret / 4);
            generateLargeCave2(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), x2, y, z, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * width + width * 2.0f) / 4.0f, direction + 1.5707964f, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) / 4.0f, ret, length, branchPoint, curviness, true, true);
            generateLargeCave2(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), x2, y, z, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() * width + width * 2.0f) / 4.0f, direction - 1.5707964f, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) / 4.0f, ret, length, branchPoint, curviness, true, true);
        }
        else {
            generateLargeCave2(MysteriumPatchesFixesCave.largeCaveRNG.nextLong(), x2, y, z, width, direction, (MysteriumPatchesFixesCave.largeCaveRNG.nextFloat() - 0.5f) / 4.0f, 0, length, branchPoint, curviness, false, true);
        }
        int ret = (int)(y + 0.5);
        if (type == 1) {
            ret += (int)(direction * 1024.0f) * 256;
        }
        return ret;
    }

    private static void generateLargeCave2(long seed, double x, double y, double z, float width, float directionXZ, float directionY, int pos, final int length, final int branchPoint, final float curviness, final boolean giantCaveBranch, final boolean vertVar) {
        MysteriumPatchesFixesCave.caveRNG.setSeed(seed);
        float var23 = 0.0f;
        float var24 = 0.0f;
        final float minRadius = 1.75f + width / 53.3333f;
        final boolean isVerticalCave = vertVar && MysteriumPatchesFixesCave.caveRNG.nextInt(6) == 0 && width < 20.0f;
        while (pos < length) {
            if (pos == branchPoint) {
                seed = MysteriumPatchesFixesCave.caveRNG.nextLong();
                if (giantCaveBranch) {
                    width *= 1.5f;
                }
                width = (MysteriumPatchesFixesCave.caveRNG.nextFloat() * width + width) / 3.0f;
                directionY /= 3.0f;
                generateLargeCave2(MysteriumPatchesFixesCave.caveRNG.nextLong(), x, y, z, (MysteriumPatchesFixesCave.caveRNG.nextFloat() * width + width) / 3.0f, directionXZ - 1.5707964f, directionY, pos, length, 0, curviness, false, true);
                generateLargeCave2(seed, x, y, z, width, directionXZ + 1.5707964f, directionY, pos, length, 0, curviness, false, true);
                return;
            }
            double radiusW = sine(pos * 3.1415927f / length) * width;
            final double var25 = x - MysteriumPatchesFixesCave.chunkCenterX;
            final double var26 = z - MysteriumPatchesFixesCave.chunkCenterZ;
            final double var27 = length - pos + 18 + radiusW;
            if (var25 * var25 + var26 * var26 > var27 * var27) {
                return;
            }
            final double ratio = 1.0f - (float)radiusW / 100.0f;
            radiusW += minRadius;
            double radiusH = radiusW * ratio;
            if (MysteriumPatchesFixesCave.caveRNG.nextInt(4) == 0) {
                radiusW = radiusW / 5.0 + 0.75;
                radiusH = radiusH / 5.0 + 0.75;
            }
            final float var28 = cosine(directionY);
            x += cosine(directionXZ) * var28;
            y += sine(directionY);
            z += sine(directionXZ) * var28;
            if (isVerticalCave) {
                directionY *= 0.92f;
            }
            else {
                directionY *= 0.7f;
            }
            if (!vertVar) {
                if (y > 45.0) {
                    var24 = -0.5f;
                }
                else if (y < 4.0) {
                    var24 = 0.5f;
                }
            }
            directionY += var24 * 0.1f;
            directionXZ += var23 * curviness;
            var24 *= 0.9f;
            var23 *= 0.75f;
            var24 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 2.0f;
            var23 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 4.0f;
            final double radiusW_2 = radiusW + 9.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - radiusW_2 && x <= MysteriumPatchesFixesCave.chunkCenterX + radiusW_2 && z >= MysteriumPatchesFixesCave.chunkCenterZ - radiusW_2 && z <= MysteriumPatchesFixesCave.chunkCenterZ + radiusW_2) {
                final double noiseMultiplier = 0.275 / Math.max(radiusW - 1.0, 0.916666) + 0.0033735;
                generateCaveSegment(x, y, z, radiusW, radiusH, noiseMultiplier, 1);
            }
            ++pos;
        }
    }

    private static void generateHorizontalCave(long seed, double x, double y, double z, float width, float directionXZ, float directionY, int pos, final int length, final int caveType) {
        MysteriumPatchesFixesCave.caveRNG.setSeed(seed);
        float var23 = 0.0f;
        float var24 = 0.0f;
        int branchPoint = -999;
        final float startDir = directionXZ;
        final double startY = y;
        byte branchFlag = -1;
        final boolean flag = caveType < 1 || caveType == 2;
        float curviness;
        if (caveType < 2) {
            curviness = 0.1f;
        }
        else {
            curviness = ((caveType != 3 && MysteriumPatchesFixesCave.caveRNG.nextInt(4) != 0) ? 0.025f : 0.05f);
        }
        if (caveType == 1) {
            if (pos == 0) {
                branchPoint = Math.min(length * 2 / 3 + MysteriumPatchesFixesCave.caveRNG.nextInt(length / 11), 120);
            }
            else {
                branchPoint = pos;
                pos = 0;
                branchFlag = 0;
            }
        }
        final boolean isVerticalCave = flag && MysteriumPatchesFixesCave.caveRNG.nextInt(6) == 0;
        while (pos < length) {
            if (pos == branchPoint) {
                seed = MysteriumPatchesFixesCave.caveRNG.nextLong();
                final float var25 = Math.min(width * 0.75f, MysteriumPatchesFixesCave.caveRNG.nextFloat() * width * 0.75f + width * 0.25f);
                width = Math.min(width * 0.75f, MysteriumPatchesFixesCave.caveRNG.nextFloat() * width * 0.75f + width * 0.25f);
                directionY /= 3.0f;
                generateHorizontalCave(MysteriumPatchesFixesCave.caveRNG.nextLong(), x, y, z, width, directionXZ - 1.5707964f, directionY, pos, length, branchFlag);
                generateHorizontalCave(seed, x, y, z, var25, directionXZ + 1.5707964f, directionY, pos, length, branchFlag);
                return;
            }
            final double var26 = x - MysteriumPatchesFixesCave.chunkCenterX;
            final double var27 = z - MysteriumPatchesFixesCave.chunkCenterZ;
            final double var28 = length - pos + 18 + (double)width;
            if (var26 * var26 + var27 * var27 > var28 * var28) {
                return;
            }
            double radiusW = 1.25;
            final float var29 = cosine(directionY);
            x += cosine(directionXZ) * var29;
            y += sine(directionY);
            z += sine(directionXZ) * var29;
            if (isVerticalCave) {
                directionY *= 0.92f;
            }
            else {
                directionY *= 0.7f;
            }
            if (caveType < 2) {
                radiusW += sine(pos * 3.1415927f / length) * width;
                if (caveType >= 0) {
                    float radiusW_2 = directionXZ - startDir;
                    if (radiusW_2 > 0.7853982f) {
                        var23 = -0.5f;
                    }
                    else if (radiusW_2 < -0.7853982f) {
                        var23 = 0.5f;
                    }
                    radiusW_2 = (float)(y - startY);
                    if (radiusW_2 > 5.0f) {
                        var24 = -0.5f;
                    }
                    else if (radiusW_2 < -5.0f) {
                        var24 = 0.5f;
                    }
                }
            }
            else {
                if (pos < length - 3) {
                    radiusW += 0.25;
                }
                if (pos < length - 6) {
                    radiusW += width * MysteriumPatchesFixesCave.caveRNG.nextFloat();
                }
                if (caveType == 2) {
                    final float radiusW_2 = (float)(y - startY);
                    if (radiusW_2 > 5.0f) {
                        var24 = -0.5f;
                    }
                    else if (radiusW_2 < -5.0f) {
                        var24 = 0.5f;
                    }
                }
                else if (caveType == 3) {
                    final float radiusW_2 = directionXZ - startDir;
                    if (radiusW_2 > 0.7853982f) {
                        var23 = -0.5f;
                    }
                    else if (radiusW_2 < -0.7853982f) {
                        var23 = 0.5f;
                    }
                }
            }
            directionY += var24 * 0.1f;
            var24 *= 0.9f;
            var24 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 2.0f;
            if (MysteriumPatchesFixesCave.caveRNG.nextInt(4) == 0) {
                radiusW = 1.25;
            }
            directionXZ += var23 * curviness;
            var23 *= 0.75f;
            var23 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 4.0f;
            final double var30 = radiusW + 9.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - var30 && x <= MysteriumPatchesFixesCave.chunkCenterX + var30 && z >= MysteriumPatchesFixesCave.chunkCenterZ - var30 && z <= MysteriumPatchesFixesCave.chunkCenterZ + var30) {
                final double noiseMultiplier = (radiusW < 1.916666) ? 0.15 : (0.275 / (radiusW - 1.0));
                generateCaveSegment(x, y, z, radiusW, radiusW, noiseMultiplier, 1);
            }
            ++pos;
        }
    }

    private static void generateDirectionalCave(final int x, final int y, final int z, int cx, int cz, final int offset) {
        float direction = (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) * 0.7853982f;
        boolean length = false;
        cx = x - cx;
        cz = z - cz;
        if (cx > offset) {
            if (cz > offset) {
                direction -= 2.35619f;
            }
            else if (cz < -offset) {
                direction += 2.35619f;
            }
            else {
                direction += 3.1415927f;
            }
        }
        else if (cx < -offset) {
            if (cz > offset) {
                direction -= 0.7853982f;
            }
            else if (cz < -offset) {
                direction += 0.7853982f;
            }
        }
        else if (cz > offset) {
            --direction;
        }
        else if (cz < -offset) {
            ++direction;
        }
        else {
            direction *= 8.0f;
            length = true;
        }
        int var9;
        if (!length) {
            var9 = MysteriumPatchesFixesCave.rand.nextInt(16) + 8 + Math.round((float)Math.sqrt(cx * cx + cz * cz));
        }
        else {
            var9 = MysteriumPatchesFixesCave.rand.nextInt(8) + 24;
        }
        float width = Math.max(0.01f, MysteriumPatchesFixesCave.rand.nextFloat() * 0.5f + WGConfig.widthAdditionDirectional / 10.0f);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, (float)WGConfig.widthMaxDirectional);
            width = Math.max(width, (float)WGConfig.widthMinDirectional);
        }
        generateHorizontalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, var9, 0);
    }

    private static void generateHorizontalLinkCave(final int x, final int y, final int z, int cx, int cz, final int type) {
        float direction = (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) * 0.7853982f;
        cx = x - cx;
        cz = z - cz;
        if (cx == 0 && cz == 0) {
            direction *= 8.0f;
        }
        else if (cx >= 0) {
            if (cz >= 0) {
                direction += 0.7853982f;
            }
            else {
                direction -= 0.7853982f;
            }
        }
        else if (cz >= 0) {
            direction += 2.35619f;
        }
        else {
            direction -= 2.35619f;
        }
        float width = MysteriumPatchesFixesCave.rand.nextFloat() * 0.75f + 0.25f;
        width += WGConfig.widthAdditionNormal;
        width = Math.max(0.275f, width);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, (float)WGConfig.widthMaxNormal);
            width = Math.max(width, (float)WGConfig.widthMinNormal);
        }
        byte caveType;
        int length;
        if ((type & 0x1) == 0x0) {
            caveType = 1;
            length = ((type == 4) ? 144 : 128) + MysteriumPatchesFixesCave.rand.nextInt(32);
        }
        else {
            caveType = 0;
            length = 80 + MysteriumPatchesFixesCave.rand.nextInt(16);
        }
        generateHorizontalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, length, caveType);
        if (type >= 2) {
            length = 20 + MysteriumPatchesFixesCave.rand.nextInt(6);
            generateHorizontalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, Math.min(width * 0.75f, MysteriumPatchesFixesCave.rand.nextFloat() * width * 0.75f + width * 0.25f), direction - 2.0944f, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, length, -1);
            generateHorizontalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, Math.min(width * 0.75f, MysteriumPatchesFixesCave.rand.nextFloat() * width * 0.75f + width * 0.25f), direction + 2.0944f, (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f, 0, length, -1);
        }
    }

    private static void generateVerticalCave(final double x, final int y1, final int y2, final double z) {
        generateVerticalCave(MysteriumPatchesFixesCave.rand.nextLong(), x, y1, y2, z, -1.0f, 0.0, 0.0, 0);
    }

    private static void generateVerticalCave(final long seed, double x, int y1, int y2, double z, float width, final double centerX, final double centerZ, int maxDeviation) {
        MysteriumPatchesFixesCave.caveRNG.setSeed(seed);
        float var23 = 0.0f;
        width += Math.max(0.0f, (float)WGConfig.widthAdditionVertical);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, (float)WGConfig.widthMaxVertical);
            width = Math.max(width, (float)WGConfig.widthMinVertical);
        }
        float directionXZ = MysteriumPatchesFixesCave.caveRNG.nextFloat() * 6.2831855f;
        boolean descending = false;
        maxDeviation *= maxDeviation;
        float horizVar = 1.0f;
        if (width != 0.0f) {
            horizVar = MysteriumPatchesFixesCave.caveRNG.nextFloat();
            horizVar = 1.0f - horizVar * horizVar * 0.333333f;
        }
        if (y1 > y2) {
            final int length = y1;
            y1 = y2;
            y2 = length;
            descending = true;
        }
        final int length = y2 - y1;
        for (int i = 0; i < length; ++i) {
            final double var24 = x - MysteriumPatchesFixesCave.chunkCenterX;
            final double var25 = z - MysteriumPatchesFixesCave.chunkCenterZ;
            final double var26 = length - i + 18 + (double)width;
            if (var24 * var24 + var25 * var25 > var26 * var26) {
                return;
            }
            double radiusW;
            if (width >= 0.0f) {
                radiusW = 1.5f + sine(i * 1.5707964f / length) * width;
            }
            else {
                radiusW = 1.5f + (MysteriumPatchesFixesCave.caveRNG.nextFloat() + MysteriumPatchesFixesCave.caveRNG.nextFloat()) * 0.5f;
            }
            x += cosine(directionXZ) * horizVar;
            z += sine(directionXZ) * horizVar;
            if (maxDeviation == 1) {
                final float radiusW_2 = (float)(x - centerX);
                final float devZ = (float)(z - centerZ);
                if (radiusW_2 > 1.0f) {
                    --x;
                }
                if (radiusW_2 < -1.0f) {
                    ++x;
                }
                if (devZ > 1.0f) {
                    --z;
                }
                if (devZ < -1.0f) {
                    ++z;
                }
            }
            else if (maxDeviation > 0) {
                final float radiusW_2 = (float)(x - centerX);
                final float devZ = (float)(z - centerZ);
                if (radiusW_2 * radiusW_2 + devZ * devZ > maxDeviation) {
                    if (devZ >= 0.0f) {
                        if (radiusW_2 >= 0.0f) {
                            directionXZ = (directionXZ * 3.0f - 2.35619f) / 4.0f;
                        }
                        else {
                            directionXZ = (directionXZ * 3.0f - 0.7853982f) / 4.0f;
                        }
                    }
                    else if (radiusW_2 >= 0.0f) {
                        directionXZ = (directionXZ * 3.0f + 2.35619f) / 4.0f;
                    }
                    else {
                        directionXZ = (directionXZ * 3.0f + 0.7853982f) / 4.0f;
                    }
                }
            }
            directionXZ += var23 * 0.15f;
            var23 *= 0.75f;
            var23 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 4.0f;
            final double var27 = radiusW + 9.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - var27 && x <= MysteriumPatchesFixesCave.chunkCenterX + var27 && z >= MysteriumPatchesFixesCave.chunkCenterZ - var27 && z <= MysteriumPatchesFixesCave.chunkCenterZ + var27) {
                final double noiseMultiplier = (radiusW < 1.916666) ? 0.15 : (0.275 / (radiusW - 1.0));
                double radiusH = 1.5;
                double y3;
                if (descending) {
                    y3 = y2 - i - 1;
                }
                else {
                    y3 = y1 + i;
                }
                if (i == length - 1 && width >= 0.5f) {
                    radiusH = width + 1.0f;
                }
                generateCaveSegment(x, y3, z, radiusW, radiusH, noiseMultiplier, 1);
            }
        }
    }

    private static void generateRavineCave(final double x, final double y, final double z, final float heightVariation) {
        int length = MysteriumPatchesFixesCave.rand.nextInt(5) + 16;
        int segmentLength = length + MysteriumPatchesFixesCave.rand.nextInt(4) * 2;
        float width = MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() + 1.0f + Math.max(-1.0f, (float)WGConfig.widthAdditionRavineCave);
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, (float)WGConfig.widthMaxRavineCave);
            width = Math.max(width, (float)WGConfig.widthMinRavineCave);
        }
        float height = MysteriumPatchesFixesCave.rand.nextFloat() * heightVariation + 2.0f;
        float direction = MysteriumPatchesFixesCave.rand.nextFloat() * 3.1415927f;
        float directionY = (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f;
        float slope = (MysteriumPatchesFixesCave.rand.nextFloat() * 0.75f + 0.25f) * 0.25f * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
        generateRavineCaveSegment(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction, directionY, slope, segmentLength, height);
        segmentLength = length + MysteriumPatchesFixesCave.rand.nextInt(4) * 2;
        generateRavineCaveSegment(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction + 3.1415927f, -directionY, -slope, segmentLength, height);
        if (MysteriumPatchesFixesCave.rand.nextBoolean()) {
            length = MysteriumPatchesFixesCave.rand.nextInt(5) + 16;
            segmentLength = length + MysteriumPatchesFixesCave.rand.nextInt(4) * 2;
            width = MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() + 1.0f;
            height = MysteriumPatchesFixesCave.rand.nextFloat() * heightVariation + 2.0f;
            direction += (MysteriumPatchesFixesCave.rand.nextFloat() * 0.7853982f + 0.393699f) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1);
            directionY = (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f;
            slope = (MysteriumPatchesFixesCave.rand.nextFloat() * 0.75f + 0.25f) * 0.25f * (MysteriumPatchesFixesCave.rand.nextInt(2) * 2 - 1) * (MysteriumPatchesFixesCave.rand.nextInt(2) * 0.75f + 0.25f);
            generateRavineCaveSegment(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction, directionY, slope, segmentLength, height);
            if (MysteriumPatchesFixesCave.rand.nextBoolean()) {
                segmentLength = length + MysteriumPatchesFixesCave.rand.nextInt(4) * 2;
                generateRavineCaveSegment(MysteriumPatchesFixesCave.rand.nextLong(), x, y, z, width, direction + 3.1415927f, -directionY, -slope, segmentLength, height);
            }
        }
    }

    private static void generateRavineCaveSegment(final long seed, double x, double y, double z, float width, float directionXZ, float directionY, final float slope, final int length, final float heightRatio) {
        MysteriumPatchesFixesCave.caveRNG.setSeed(seed);
        float var24 = 0.0f;
        float var25 = 0.0f;
        final float startDir = directionXZ;
        final int end = (width >= 1.666f) ? 3 : ((width >= 1.333f) ? 2 : 1);
        width /= 2.0f;
        for (int pos = 0; pos < length; ++pos) {
            final double var26 = x - MysteriumPatchesFixesCave.chunkCenterX;
            final double var27 = z - MysteriumPatchesFixesCave.chunkCenterZ;
            final double var28 = length - pos + 18;
            if (var26 * var26 + var27 * var27 > var28 * var28) {
                return;
            }
            double radiusW = width;
            if (pos < length - end) {
                radiusW += MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.5f;
            }
            if (pos < length - end * 2) {
                radiusW += width;
            }
            double radiusH = radiusW * heightRatio;
            radiusW *= MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.25f + 0.75f;
            radiusH *= MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.25f + 0.75f;
            final float var29 = cosine(directionY);
            x += cosine(directionXZ) * var29;
            y += sine(directionY) + slope;
            z += sine(directionXZ) * var29;
            final float dev = directionXZ - startDir;
            if (dev > 0.392699f) {
                var24 = -0.5f;
            }
            else if (dev < -0.392699f) {
                var24 = 0.5f;
            }
            directionY *= 0.7f;
            directionY += var25 * 0.1f;
            directionXZ += var24 * 0.1f;
            var25 *= 0.5f;
            var24 *= 0.75f;
            var25 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 2.0f;
            var24 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 4.0f;
            final double radiusW_2 = radiusW + 9.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - radiusW_2 && x <= MysteriumPatchesFixesCave.chunkCenterX + radiusW_2 && z >= MysteriumPatchesFixesCave.chunkCenterZ - radiusW_2 && z <= MysteriumPatchesFixesCave.chunkCenterZ + radiusW_2) {
                int var30 = MathHelper.func_76128_c(x - radiusW) - MysteriumPatchesFixesCave.chunkX_16 - 1;
                int var31 = MathHelper.func_76128_c(x + radiusW) - MysteriumPatchesFixesCave.chunkX_16 + 1;
                int var32 = (int)(y - radiusH) - 1;
                int var33 = (int)(y + radiusH) + 1;
                int var34 = MathHelper.func_76128_c(z - radiusW) - MysteriumPatchesFixesCave.chunkZ_16 - 1;
                int var35 = MathHelper.func_76128_c(z + radiusW) - MysteriumPatchesFixesCave.chunkZ_16 + 1;
                if (var30 < 0) {
                    var30 = 0;
                }
                if (var31 > 16) {
                    var31 = 16;
                }
                if (var32 < 0) {
                    var32 = 0;
                }
                if (var33 > 200) {
                    var33 = 200;
                }
                if (var34 < 0) {
                    var34 = 0;
                }
                if (var35 > 16) {
                    var35 = 16;
                }
                for (int var36 = var30; var36 < var31; ++var36) {
                    double var37 = (var36 + MysteriumPatchesFixesCave.chunkX_16 + 0.5 - x) / radiusW;
                    var37 *= var37;
                    for (int var38 = var34; var38 < var35; ++var38) {
                        double var39 = (var38 + MysteriumPatchesFixesCave.chunkZ_16 + 0.5 - z) / radiusW;
                        var39 = var39 * var39 + var37;
                        if (var39 < 1.0) {
                            int var40 = var36 << 12 | var38 << 8 | var33;
                            final int biome = MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(var36 + MysteriumPatchesFixesCave.chunkX_16, var38 + MysteriumPatchesFixesCave.chunkZ_16).field_76756_M;
                            for (int var41 = var33 - 1; var41 >= var32; --var41) {
                                final double var42 = (var41 + 0.5 - y) / radiusH;
                                if (var42 > -0.7 && var39 + var42 * var42 / 6.0 + (MysteriumPatchesFixesCave.noiseGen.nextInt(3) - 1) * 0.3 < 1.0) {
                                    replaceBlock(var40, var36, var38, biome);
                                }
                                --var40;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void generateMazeCaveSegment(int x, final int y, int z, final int direction, final int length, final float height) {
        float width = Math.max(1.45f, ((direction & 0x1) == 0x1) ? 1.55f : (1.45f + WGConfig.widthAdditionMaze));
        if (WGConfig.hardLimitsEnabled) {
            width = Math.min(width, (float)WGConfig.widthMaxMaze);
            width = Math.max(width, (float)WGConfig.widthMinMaze);
        }
        for (int pos = 0; pos < length; ++pos) {
            final double var34 = x - MysteriumPatchesFixesCave.chunkCenterX;
            final double var35 = z - MysteriumPatchesFixesCave.chunkCenterZ;
            final double var36 = length - pos + 18;
            if (var34 * var34 + var35 * var35 > var36 * var36) {
                return;
            }
            x = getOffsetX(x, direction, 1);
            z = getOffsetZ(z, direction, 1);
            double radiusW = width + 10.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - radiusW && x <= MysteriumPatchesFixesCave.chunkCenterX + radiusW && z >= MysteriumPatchesFixesCave.chunkCenterZ - radiusW && z <= MysteriumPatchesFixesCave.chunkCenterZ + radiusW) {
                radiusW = MysteriumPatchesFixesCave.noiseGen.nextFloat() * 0.5f + width;
                final double radiusH = MysteriumPatchesFixesCave.noiseGen.nextFloat() * 0.5f + height;
                double var37 = MysteriumPatchesFixesCave.noiseGen.nextFloat() - 0.5f + x;
                var37 = MysteriumPatchesFixesCave.noiseGen.nextFloat() - 0.5f + y;
                var37 = MysteriumPatchesFixesCave.noiseGen.nextFloat() - 0.5f + z;
                generateCaveSegment(x, y, z, radiusW, radiusH, width / 7.25f, 0);
            }
        }
    }

    private static void generateRavines(final int chunkX, final int chunkZ, final boolean flag, final int genCaves) {
        if (MysteriumPatchesFixesCave.rand.nextInt(20) == 15) {
            if (genCaves == 3 && isGiantCaveRegion(chunkX, chunkZ)) {
                return;
            }
            byte bigRavine = -1;
            final boolean notNearOrigin = true;
            if (notNearOrigin) {
                final int x = chunkX + MysteriumPatchesFixesCave.caveOffsetX + 4;
                final int offsetZ = chunkZ + MysteriumPatchesFixesCave.caveOffsetZ + 4;
                if ((x & 0x7) == 0x0 && (offsetZ & 0x7) == 0x0 && (x & 0x8) != (offsetZ & 0x8)) {
                    bigRavine = 2;
                }
                else if (MysteriumPatchesFixesCave.rand.nextInt(25) < 19 && x % 3 == 0 && offsetZ % 3 == 0 && (x / 3 & 0x1) == (offsetZ / 3 & 0x1)) {
                    bigRavine = 1;
                }
            }
            if (bigRavine > 0 && genCaves == 5) {
                bigRavine = 0;
            }
            if (bigRavine >= 0 || (flag && (MysteriumPatchesFixesCave.rand.nextInt(30) < 11 || (!notNearOrigin && MysteriumPatchesFixesCave.rand.nextInt(20) == 0)))) {
                final double var24 = chunkX * 16 + 8;
                double y = MysteriumPatchesFixesCave.rand.nextInt(MysteriumPatchesFixesCave.rand.nextInt(50) + 8) + 13;
                final double z = chunkZ * 16 + 8;
                final float directionXZ = MysteriumPatchesFixesCave.rand.nextFloat() * 3.1415927f;
                final float directionY = (MysteriumPatchesFixesCave.rand.nextFloat() - 0.5f) / 4.0f;
                float width = Math.max(MysteriumPatchesFixesCave.rand.nextFloat() * 3.0f, MysteriumPatchesFixesCave.rand.nextFloat() * 4.0f + MysteriumPatchesFixesCave.rand.nextFloat() * 2.0f + WGConfig.widthAdditionRavineCave);
                if (WGConfig.hardLimitsEnabled) {
                    width = Math.min(width, (float)WGConfig.widthMaxRavineCave);
                    width = Math.max(width, (float)WGConfig.widthMinRavineCave);
                }
                if (MysteriumPatchesFixesCave.rand.nextInt(4) == 0) {
                    width += MysteriumPatchesFixesCave.rand.nextFloat() * ((bigRavine != 1 && width < 2.0f) ? 0.0f : 2.0f);
                }
                double heightRatio = 3.0;
                int length = 112 - MysteriumPatchesFixesCave.rand.nextInt(15) * 2;
                float curviness = 0.05f;
                if (MysteriumPatchesFixesCave.rand.nextInt(3) == 0) {
                    if (MysteriumPatchesFixesCave.rand.nextInt(3) == 0) {
                        curviness = 0.1f;
                    }
                    else {
                        curviness = 0.075f;
                    }
                }
                if (bigRavine <= 0) {
                    int data = MysteriumPatchesFixesCave.biomeList[(MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(MysteriumPatchesFixesCave.chunkX_16 + 8, MysteriumPatchesFixesCave.chunkZ_16 + 8).field_76756_M > 255) ? 20 : MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(MysteriumPatchesFixesCave.chunkX_16 + 8, MysteriumPatchesFixesCave.chunkZ_16 + 8).field_76756_M] >> 2 & 0x3;
                    if ((MysteriumPatchesFixesCave.rand.nextBoolean() && data == 1) || data == 2) {
                        data = MysteriumPatchesFixesCave.rand.nextInt(2) + 1;
                        if (y < 31.5) {
                            y += data * 8;
                        }
                        heightRatio += data;
                        if (width > 6 - data) {
                            width /= 2.0f;
                        }
                    }
                }
                else {
                    length += MysteriumPatchesFixesCave.rand.nextInt(64) * 2;
                    if (width < 2.0f) {
                        ++width;
                        if (width < 2.0f) {
                            ++width;
                        }
                    }
                    width *= MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * 1.5f + 1.0f;
                    if (bigRavine == 2) {
                        length += 80 + MysteriumPatchesFixesCave.rand.nextInt(40) * 2;
                        width += MysteriumPatchesFixesCave.rand.nextFloat() * (length / 56) + 3.0f;
                    }
                    if (length > 336) {
                        length = 336;
                    }
                    if (width > 18.0f) {
                        width = 18.0f;
                    }
                    if (y < 23.5) {
                        y += width / 1.5f;
                    }
                    else if (y > 52.5) {
                        y -= width * 1.5f;
                    }
                    else if (y > 42.5) {
                        y -= width / 1.5f;
                    }
                    curviness = (curviness + length / 8960.0f + 0.0125f) / 1.5f;
                }
                float var25 = 1.0f;
                float ravineDataMultiplier = 1.1f - (width - 2.0f) * 0.07f;
                if (ravineDataMultiplier < 0.6f) {
                    ravineDataMultiplier = 0.6f - (0.6f - ravineDataMultiplier) * 0.290322f;
                }
                int skipCount = 999;
                for (int i = 0; i < 128; ++i) {
                    if (++skipCount >= 2 && (skipCount >= 5 || MysteriumPatchesFixesCave.rand.nextInt(3) == 0)) {
                        skipCount = 0;
                        var25 = (1.0f + MysteriumPatchesFixesCave.rand.nextFloat() * MysteriumPatchesFixesCave.rand.nextFloat() * ravineDataMultiplier) * (0.95f + MysteriumPatchesFixesCave.rand.nextInt(2) * 0.1f);
                        var25 *= var25;
                    }
                    MysteriumPatchesFixesCave.ravineData[i] = var25;
                }
                length /= 2;
                generateRavineHalf(MysteriumPatchesFixesCave.rand.nextLong(), var24, y, z, width, directionXZ, directionY, heightRatio, length, curviness, bigRavine > 0);
                generateRavineHalf(MysteriumPatchesFixesCave.rand.nextLong(), var24, y, z, width, directionXZ + 3.1415927f, -directionY, heightRatio, length, curviness, bigRavine > 0);
            }
        }
    }

    private static void generateRavineHalf(final long seed, double x, double y, double z, final float width, float directionXZ, float directionY, final double heightRatio, final int length, final float curviness, final boolean bigRavine) {
        MysteriumPatchesFixesCave.caveRNG.setSeed(seed);
        float var24 = 0.0f;
        float var25 = 0.0f;
        final float startDir = directionXZ;
        for (int pos = 0; pos < length; ++pos) {
            double radiusW = 1.5f + cosine(pos * 1.5707964f / length) * width;
            final double var26 = x - MysteriumPatchesFixesCave.chunkCenterX;
            final double var27 = z - MysteriumPatchesFixesCave.chunkCenterZ;
            final double var28 = length - pos + 18 + radiusW;
            if (var26 * var26 + var27 * var27 > var28 * var28) {
                return;
            }
            double radiusH = radiusW * heightRatio;
            if (bigRavine) {
                if (width > 5.5f) {
                    radiusH *= MysteriumPatchesFixesCave.ravineHeightLookup[(int)(radiusW * 10.025642) - 15];
                }
                else {
                    radiusH *= (MysteriumPatchesFixesCave.ravineHeightLookup[(int)(radiusW * 10.025642) - 15] + Math.max(1.875f - (float)radiusW / 4.0f, 1.0f) + 0.25f) / 2.25f;
                }
            }
            else if (width > 2.0f) {
                radiusH *= Math.max(1.875f - (float)radiusW / 4.0f, 1.0f);
            }
            radiusW *= MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.25f + 0.75f;
            radiusH *= MysteriumPatchesFixesCave.caveRNG.nextFloat() * 0.25f + 0.75f;
            if (MysteriumPatchesFixesCave.caveRNG.nextInt(4) == 0) {
                radiusW = radiusW / 5.0 + 0.5;
                radiusH = radiusH / 4.0 + 1.5;
            }
            final float var29 = cosine(directionY);
            x += cosine(directionXZ) * var29;
            y += sine(directionY);
            z += sine(directionXZ) * var29;
            final float dev = directionXZ - startDir;
            if (dev > 0.7853982f) {
                var24 = -0.5f;
            }
            else if (dev < -0.7853982f) {
                var24 = 0.5f;
            }
            directionY *= 0.7f;
            directionY += var25 * 0.05f;
            directionXZ += var24 * curviness;
            var25 *= 0.8f;
            var24 *= 0.5f;
            var25 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 2.0f;
            var24 += (MysteriumPatchesFixesCave.caveRNG.nextFloat() - MysteriumPatchesFixesCave.caveRNG.nextFloat()) * MysteriumPatchesFixesCave.caveRNG.nextFloat() * 4.0f;
            final double radiusW_2 = radiusW + 9.0;
            if (x >= MysteriumPatchesFixesCave.chunkCenterX - radiusW_2 && x <= MysteriumPatchesFixesCave.chunkCenterX + radiusW_2 && z >= MysteriumPatchesFixesCave.chunkCenterZ - radiusW_2 && z <= MysteriumPatchesFixesCave.chunkCenterZ + radiusW_2) {
                int var30 = MathHelper.func_76128_c(x - radiusW) - MysteriumPatchesFixesCave.chunkX_16 - 1;
                int var31 = MathHelper.func_76128_c(x + radiusW) - MysteriumPatchesFixesCave.chunkX_16 + 1;
                int var32 = (int)(y - radiusH) - 1;
                int var33 = (int)(y + radiusH) + 1;
                int var34 = MathHelper.func_76128_c(z - radiusW) - MysteriumPatchesFixesCave.chunkZ_16 - 1;
                int var35 = MathHelper.func_76128_c(z + radiusW) - MysteriumPatchesFixesCave.chunkZ_16 + 1;
                if (var30 < 0) {
                    var30 = 0;
                }
                if (var31 > 16) {
                    var31 = 16;
                }
                if (var32 < 0) {
                    var32 = 0;
                }
                if (var33 > 120) {
                    var33 = 120;
                }
                if (var34 < 0) {
                    var34 = 0;
                }
                if (var35 > 16) {
                    var35 = 16;
                }
                final double noiseMultiplier = 0.33333333 / Math.max(radiusW - 0.5, 2.5);
                for (int var36 = var30; var36 < var31; ++var36) {
                    double var37 = (var36 + MysteriumPatchesFixesCave.chunkX_16 + 0.5 - x) / radiusW;
                    var37 *= var37;
                    for (int var38 = var34; var38 < var35; ++var38) {
                        double var39 = (var38 + MysteriumPatchesFixesCave.chunkZ_16 + 0.5 - z) / radiusW;
                        var39 = var39 * var39 + var37;
                        if (var39 < 1.0) {
                            int var40 = var36 << 12 | var38 << 8 | var33;
                            final int biome = MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(var36 + MysteriumPatchesFixesCave.chunkX_16, var38 + MysteriumPatchesFixesCave.chunkZ_16).field_76756_M;
                            for (int var41 = var33 - 1; var41 >= var32; --var41) {
                                final double var42 = (var41 + 0.5 - y) / radiusH;
                                if (var39 * MysteriumPatchesFixesCave.ravineData[var41] + var42 * var42 / 6.0 + (MysteriumPatchesFixesCave.noiseGen.nextInt(3) - 1) * noiseMultiplier < 1.0) {
                                    replaceBlock(var40, var36, var38, biome);
                                }
                                --var40;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void generateCaveSegment(final double x, final double y, final double z, final double radiusW, final double radiusH, final double noiseMultiplier, final int noiseOffset) {
        int var55 = MathHelper.func_76128_c(x - radiusW) - MysteriumPatchesFixesCave.chunkX_16 - 1;
        int var56 = MathHelper.func_76128_c(x + radiusW) - MysteriumPatchesFixesCave.chunkX_16 + 1;
        int var57 = (int)(y - radiusH) - 1;
        int var58 = (int)(y + radiusH) + 1;
        int var59 = MathHelper.func_76128_c(z - radiusW) - MysteriumPatchesFixesCave.chunkZ_16 - 1;
        int var60 = MathHelper.func_76128_c(z + radiusW) - MysteriumPatchesFixesCave.chunkZ_16 + 1;
        if (var55 < 0) {
            var55 = 0;
        }
        if (var56 > 16) {
            var56 = 16;
        }
        if (var57 < 0) {
            var57 = 0;
        }
        if (var58 > 200) {
            var58 = 200;
        }
        if (var59 < 0) {
            var59 = 0;
        }
        if (var60 > 16) {
            var60 = 16;
        }
        for (int var61 = var55; var61 < var56; ++var61) {
            double var62 = (var61 + MysteriumPatchesFixesCave.chunkX_16 + 0.5 - x) / radiusW;
            var62 *= var62;
            for (int var63 = var59; var63 < var60; ++var63) {
                double var64 = (var63 + MysteriumPatchesFixesCave.chunkZ_16 + 0.5 - z) / radiusW;
                var64 = var64 * var64 + var62;
                if (var64 < 1.0) {
                    int var65 = var61 << 12 | var63 << 8 | var58;
                    final int biome = MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(var61 + MysteriumPatchesFixesCave.chunkX_16, var63 + MysteriumPatchesFixesCave.chunkZ_16).field_76756_M;
                    for (int var66 = var58 - 1; var66 >= var57; --var66) {
                        final double var67 = (var66 + 0.5 - y) / radiusH;
                        if (var67 > -0.7 && var67 * var67 + var64 + (MysteriumPatchesFixesCave.noiseGen.nextInt(3) - noiseOffset) * noiseMultiplier < 1.0) {
                            replaceBlock(var65, var61, var63, biome);
                        }
                        --var65;
                    }
                }
            }
        }
    }

    private static void replaceBlock(final int index, final int x, final int z, final int biome) {
        Block data = MysteriumPatchesFixesCave.chunkData[index];
        if (data != null || data != Blocks.field_150350_a) {
            final int y = index & 0xFF;
            if (y >= 25 && y <= 62) {
                final int minX = Math.max(x - 1, 0);
                final int maxX = Math.min(x + 1, 15);
                final int minZ = Math.max(z - 1, 0);
                final int maxZ = Math.min(z + 1, 15);
                for (int x2 = minX; x2 <= maxX; ++x2) {
                    for (int z2 = minZ; z2 <= maxZ; ++z2) {
                        final int xyz = x2 << 12 | z2 << 8 | y;
                        if (MysteriumPatchesFixesCave.chunkData[xyz] == Blocks.field_150355_j) {
                            return;
                        }
                        if (MysteriumPatchesFixesCave.chunkData[xyz + 1] == Blocks.field_150355_j) {
                            return;
                        }
                    }
                }
                for (int x2 = minX; x2 <= maxX; ++x2) {
                    int z2 = z - 2;
                    if (z2 >= 0 && MysteriumPatchesFixesCave.chunkData[x2 << 12 | z2 << 8 | y] == Blocks.field_150355_j) {
                        return;
                    }
                    z2 = z + 2;
                    if (z2 <= 15 && MysteriumPatchesFixesCave.chunkData[x2 << 12 | z2 << 8 | y] == Blocks.field_150355_j) {
                        return;
                    }
                }
                for (int z2 = minZ; z2 <= maxZ; ++z2) {
                    int x2 = x - 2;
                    if (x2 >= 0 && MysteriumPatchesFixesCave.chunkData[x2 << 12 | z2 << 8 | y] == Blocks.field_150355_j) {
                        return;
                    }
                    x2 = x + 2;
                    if (x2 <= 15 && MysteriumPatchesFixesCave.chunkData[x2 << 12 | z2 << 8 | y] == Blocks.field_150355_j) {
                        return;
                    }
                }
                if (MysteriumPatchesFixesCave.chunkData[x << 12 | z << 8 | y + 2] == Blocks.field_150355_j) {
                    return;
                }
            }
            if (y >= 60 && y <= 64 && (MysteriumPatchesFixesCave.biomeList[(biome > 255) ? 20 : biome] & 0x20) != 0x0) {
                return;
            }
            BiomeGenBase bm = null;
            try {
                bm = MysteriumPatchesFixesCave.worldObj.getBiomeGenForCoordsBody(x + MysteriumPatchesFixesCave.chunkX_16, z + MysteriumPatchesFixesCave.chunkZ_16);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            boolean flag1 = false;
            for (final String str : WGConfig.secondYLevelList) {
                if (bm.field_76791_y.equalsIgnoreCase(str)) {
                    flag1 = true;
                    break;
                }
            }
            if (bm != null && y < MysteriumPatchesFixesCave.oceanAvg && WGConfig.secondYLevel && flag1) {
                MysteriumPatchesFixesCave.chunkData[index] = Blocks.field_150355_j;
                if (CavesDecorator.shouldGenerateStone(MysteriumPatchesFixesCave.worldObj, x + MysteriumPatchesFixesCave.chunkX_16, z + MysteriumPatchesFixesCave.chunkZ_16)) {
                    MysteriumPatchesFixesCave.chunkData[index] = ((MysteriumPatchesFixesCave.newRand.nextFloat() > 0.5f) ? Blocks.field_150348_b : Blocks.field_150347_e);
                }
            }
            else if (y < WGConfig.floodLevel) {
                MysteriumPatchesFixesCave.chunkData[index] = ((WGConfig.floodMech == 1) ? Blocks.field_150353_l : Blocks.field_150355_j);
            }
            else {
                if (y > 56 && (data == Blocks.field_150349_c || data == Blocks.field_150391_bh) && MysteriumPatchesFixesCave.chunkData[index - 1] == Blocks.field_150346_d) {
                    MysteriumPatchesFixesCave.chunkData[index - 1] = data;
                }
                MysteriumPatchesFixesCave.chunkData[index] = null;
                if (y > 25) {
                    data = MysteriumPatchesFixesCave.chunkData[index + 1];
                    if (data == Blocks.field_150354_m) {
                        if ((biome < 37 || biome > 39) && (biome < 165 || biome > 167)) {
                            MysteriumPatchesFixesCave.chunkData[index + 1] = Blocks.field_150322_A;
                        }
                        else {
                            MysteriumPatchesFixesCave.chunkData[index + 1] = Blocks.field_150406_ce;
                        }
                    }
                    else if (data == Blocks.field_150351_n) {
                        MysteriumPatchesFixesCave.chunkData[index + 1] = Blocks.field_150348_b;
                    }
                }
            }
        }
    }

    private static void initializeCaveData(final int chunkX, final int chunkZ) {
        final boolean flag = Math.abs(chunkX) < 82 && Math.abs(chunkZ) < 82;
        int distance = 6724;
        for (int z = -18; z <= 18; ++z) {
            final int zIndex = (z + 18) * 37 + 18;
            final int cz = chunkZ + z;
            final int z2 = z * z;
            for (int x = -18; x <= 18; ++x) {
                final int x2z2 = x * x + z2;
                if (x2z2 <= 329) {
                    final int cx = chunkX + x;
                    if (flag) {
                        distance = cx * cx + cz * cz;
                    }
                    byte data = 0;
                    if (validColossalCaveLocation(cx, cz, distance)) {
                        data = -1;
                    }
                    else if (validStrongholdLocation(cx, cz, distance)) {
                        data = 3;
                    }
                    else if (x2z2 <= 287) {
                        if (validRegionalCaveLocation(cx, cz, distance)) {
                            data = 2;
                        }
                        else if (x2z2 <= 262) {
                            final int d = validSpecialCaveLocation(cx, cz, distance);
                            if (d > 0) {
                                data = (byte)d;
                            }
                        }
                    }
                    MysteriumPatchesFixesCave.caveDataArray[zIndex + x] = data;
                }
            }
        }
    }

    private static int validCaveLocation(final int cx, final int cz) {
        byte flag = 1;
        for (int z = -6; z <= 6; ++z) {
            final int zIndex = (cz + z + 18) * 37 + cx + 18;
            final int z2 = z * z;
            for (int x = -6; x <= 6; ++x) {
                final int x2z2 = x * x + z2;
                if (x2z2 <= 37) {
                    final byte data = MysteriumPatchesFixesCave.caveDataArray[zIndex + x];
                    if (data != 0) {
                        if (data == -1) {
                            if (x2z2 == 0) {
                                return -1;
                            }
                            return 0;
                        }
                        else {
                            if (data == 1 && x2z2 <= 17) {
                                if (x2z2 == 0) {
                                    return 2;
                                }
                                if (x2z2 <= 5) {
                                    return 0;
                                }
                                flag = 5;
                            }
                            if (data == 4 && x2z2 <= 17) {
                                if (x2z2 == 0) {
                                    return 6;
                                }
                                if (x2z2 <= 5) {
                                    return 0;
                                }
                                flag = 5;
                            }
                            if (data == 5 && x2z2 <= 17) {
                                if (x2z2 == 0) {
                                    return 7;
                                }
                                if (x2z2 <= 5) {
                                    return 0;
                                }
                                flag = 5;
                            }
                            if (data == 2 && x2z2 <= 24) {
                                if (x2z2 == 0) {
                                    return 3;
                                }
                                if (flag == 1) {
                                    flag = 4;
                                }
                            }
                            if (data == 3) {
                                flag = 5;
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }

    public static boolean validColossalCaveLocation(int chunkX, int chunkZ, final int distance) {
        chunkX += MysteriumPatchesFixesCave.caveOffsetX;
        chunkZ += MysteriumPatchesFixesCave.caveOffsetZ;
        if ((chunkX & 0x40) == (chunkZ & 0x40)) {
            return false;
        }
        MysteriumPatchesFixesCave.caveRNG.setSeed((chunkX / 64 * 341873128712L + chunkZ / 64 * 132897987541L) * MysteriumPatchesFixesCave.colossalCaveSeedMultiplier);
        return (chunkX & 0x3F) == MysteriumPatchesFixesCave.caveRNG.nextInt(32) && (chunkZ & 0x3F) == MysteriumPatchesFixesCave.caveRNG.nextInt(32);
    }

    public static int validSpecialCaveLocation(final int chunkX, final int chunkZ, int distance) {
        int offsetX = chunkX + MysteriumPatchesFixesCave.caveOffsetX + 1;
        int offsetZ = chunkZ + MysteriumPatchesFixesCave.caveOffsetZ + 1;
        if ((offsetX & 0x7) <= 2 && (offsetZ & 0x7) <= 2) {
            final int d = validSpecialCaveLocation2(offsetX, offsetZ);
            if (d != 0) {
                return d;
            }
            offsetX -= 16;
            offsetZ -= 16;
            MysteriumPatchesFixesCave.caveRNG.setSeed((offsetX / 32 * 341873128712L + offsetZ / 32 * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
            if ((offsetX & 0x1F) == MysteriumPatchesFixesCave.caveRNG.nextInt(4) * 8 + MysteriumPatchesFixesCave.caveRNG.nextInt(3) && (offsetZ & 0x1F) == MysteriumPatchesFixesCave.caveRNG.nextInt(4) * 8 + MysteriumPatchesFixesCave.caveRNG.nextInt(3)) {
                final boolean flag = distance < 5041;
                for (int z = -7; z <= 7; ++z) {
                    final int cz = chunkZ + z;
                    for (int x = -7; x <= 7; ++x) {
                        final int x2z2 = x * x + z * z;
                        if (x2z2 <= 50) {
                            final int cx = chunkX + x;
                            if (flag) {
                                distance = cx * cx + cz * cz;
                            }
                            if (validColossalCaveLocation(cx, cz, distance)) {
                                return 0;
                            }
                            if (x2z2 <= 37) {
                                if (validStrongholdLocation(cx, cz, distance)) {
                                    return 0;
                                }
                                if (x2z2 <= 24) {
                                    if (validRegionalCaveLocation(cx, cz, distance)) {
                                        return 0;
                                    }
                                    if (x2z2 > 0 && x2z2 <= 17 && validSpecialCaveLocation2(cx + MysteriumPatchesFixesCave.caveOffsetX + 1, cz + MysteriumPatchesFixesCave.caveOffsetZ + 1) != 0) {
                                        return 0;
                                    }
                                }
                            }
                        }
                    }
                }
                return 1;
            }
        }
        return 0;
    }

    private static int validSpecialCaveLocation2(final int offsetX, final int offsetZ) {
        MysteriumPatchesFixesCave.caveRNG.setSeed((offsetX / 64 * 341873128712L + offsetZ / 64 * 132897987541L) * MysteriumPatchesFixesCave.regionalCaveSeedMultiplier);
        int x;
        int z;
        if (MysteriumPatchesFixesCave.caveRNG.nextBoolean()) {
            x = MysteriumPatchesFixesCave.caveRNG.nextInt(4) * 8 + MysteriumPatchesFixesCave.caveRNG.nextInt(3);
            z = MysteriumPatchesFixesCave.caveRNG.nextInt(3) * 8 + MysteriumPatchesFixesCave.caveRNG.nextInt(3) + 40;
        }
        else {
            x = MysteriumPatchesFixesCave.caveRNG.nextInt(3) * 8 + MysteriumPatchesFixesCave.caveRNG.nextInt(3) + 40;
            z = MysteriumPatchesFixesCave.caveRNG.nextInt(4) * 8 + MysteriumPatchesFixesCave.caveRNG.nextInt(3);
        }
        return ((offsetX & 0x3F) == x && (offsetZ & 0x3F) == z) ? 4 : (((offsetX & 0x3F) == MysteriumPatchesFixesCave.caveRNG.nextInt(3) * 8 + MysteriumPatchesFixesCave.caveRNG.nextInt(3) + 40 && (offsetZ & 0x3F) == MysteriumPatchesFixesCave.caveRNG.nextInt(3) * 8 + MysteriumPatchesFixesCave.caveRNG.nextInt(3) + 40) ? 5 : 0);
    }

    public static boolean validRegionalCaveLocation(int chunkX, int chunkZ, final int distance) {
        chunkX += MysteriumPatchesFixesCave.caveOffsetX;
        chunkZ += MysteriumPatchesFixesCave.caveOffsetZ;
        MysteriumPatchesFixesCave.caveRNG.setSeed((chunkX / 64 * 341873128712L + chunkZ / 64 * 132897987541L) * MysteriumPatchesFixesCave.regionalCaveSeedMultiplier);
        chunkX &= 0x3F;
        chunkZ &= 0x3F;
        int offsetX;
        int offsetZ;
        if (MysteriumPatchesFixesCave.caveRNG.nextBoolean()) {
            offsetX = MysteriumPatchesFixesCave.caveRNG.nextInt(9) + 38;
            offsetZ = MysteriumPatchesFixesCave.caveRNG.nextInt(21);
        }
        else {
            offsetX = MysteriumPatchesFixesCave.caveRNG.nextInt(21);
            offsetZ = MysteriumPatchesFixesCave.caveRNG.nextInt(9) + 38;
        }
        return chunkX >= offsetX && chunkX <= offsetX + 11 && chunkZ >= offsetZ && chunkZ <= offsetZ + 11;
    }

    public static boolean isGiantCaveRegion(int chunkX, int chunkZ) {
        chunkX = (chunkX + MysteriumPatchesFixesCave.caveOffsetX) / 64;
        chunkZ = (chunkZ + MysteriumPatchesFixesCave.caveOffsetZ) / 64;
        MysteriumPatchesFixesCave.caveRNG.setSeed((chunkX / 2 * 341873128712L + chunkZ / 2 * 132897987541L) * MysteriumPatchesFixesCave.regionalCaveSeedMultiplier);
        return (chunkX & 0x1) == MysteriumPatchesFixesCave.caveRNG.nextInt(2) && (chunkZ & 0x1) == MysteriumPatchesFixesCave.caveRNG.nextInt(2);
    }

    private static int isEdgeOfGiantCaveRegion(int chunkX, int chunkZ) {
        chunkX += MysteriumPatchesFixesCave.caveOffsetX;
        chunkZ += MysteriumPatchesFixesCave.caveOffsetZ;
        MysteriumPatchesFixesCave.caveRNG.setSeed((chunkX / 64 * 341873128712L + chunkZ / 64 * 132897987541L) * MysteriumPatchesFixesCave.regionalCaveSeedMultiplier);
        chunkX &= 0x3F;
        chunkZ &= 0x3F;
        int offsetX;
        int offsetZ;
        if (MysteriumPatchesFixesCave.caveRNG.nextBoolean()) {
            offsetX = MysteriumPatchesFixesCave.caveRNG.nextInt(9) + 38;
            offsetZ = MysteriumPatchesFixesCave.caveRNG.nextInt(21);
        }
        else {
            offsetX = MysteriumPatchesFixesCave.caveRNG.nextInt(21);
            offsetZ = MysteriumPatchesFixesCave.caveRNG.nextInt(9) + 38;
        }
        return (chunkX == offsetX) ? ((chunkZ == offsetZ) ? 6 : ((chunkZ == offsetZ + 11) ? 4 : 5)) : ((chunkX == offsetX + 11) ? ((chunkZ == offsetZ) ? 8 : ((chunkZ == offsetZ + 11) ? 2 : 1)) : ((chunkZ == offsetZ) ? 7 : ((chunkZ == offsetZ + 11) ? 3 : 0)));
    }

    private static int getQuadrantX(final int i) {
        return ((i + 1 & 0x3) < 2) ? -1 : 1;
    }

    private static int getQuadrantZ(final int i) {
        return ((i & 0x3) < 2) ? -1 : 1;
    }

    private static int getOffsetX(final int x, final int direction, final int offset) {
        switch (direction & 0x7) {
            case 0:
            case 1:
            case 7: {
                return x + offset;
            }
            default: {
                return x;
            }
            case 3:
            case 4:
            case 5: {
                return x - offset;
            }
        }
    }

    private static int getOffsetZ(final int z, final int direction, final int offset) {
        switch (direction & 0x7) {
            case 1:
            case 2:
            case 3: {
                return z + offset;
            }
            default: {
                return z;
            }
            case 5:
            case 6:
            case 7: {
                return z - offset;
            }
        }
    }

    private static float sine(final float f) {
        return MysteriumPatchesFixesCave.SINE_TABLE[(int)(f * 162.97466f) & 0x3FF];
    }

    private static float cosine(final float f) {
        return MysteriumPatchesFixesCave.SINE_TABLE[(int)(f * 162.97466f) + 256 & 0x3FF];
    }

    private static boolean validStrongholdLocation(int chunkX, int chunkZ, final int distance) {
        chunkX += MysteriumPatchesFixesCave.caveOffsetX;
        chunkZ += MysteriumPatchesFixesCave.caveOffsetZ;
        if ((chunkX & 0x40) != (chunkZ & 0x40)) {
            return false;
        }
        MysteriumPatchesFixesCave.rand.setSeed((chunkX / 64 * 341873128712L + chunkZ / 64 * 132897987541L) * MysteriumPatchesFixesCave.seedMultiplier);
        return (chunkX & 0x3F) == MysteriumPatchesFixesCave.rand.nextInt(32) && (chunkZ & 0x3F) == MysteriumPatchesFixesCave.rand.nextInt(32) && distance >= 1600;
    }

    static {
        rand = new Random();
        noiseGen = new Random();
        caveRNG = new Random();
        newRand = new Random();
        largeCaveRNG = new Random();
        caveDataArray = new byte[1369];
        ravineData = new float[128];
        ravineHeightLookup = new float[181];
        biomeList = new byte[256];
        SINE_TABLE = new float[1024];
        MysteriumPatchesFixesCave.strongholdGenerator = new MapGenStronghold();
        MysteriumPatchesFixesCave.villageGenerator = new MapGenVillage();
        MysteriumPatchesFixesCave.mineshaftGenerator = new MapGenMineshaft();
        MysteriumPatchesFixesCave.scatteredFeatureGenerator = new MapGenScatteredFeature();
        MysteriumPatchesFixesCave.oceanAvg = -1;
        for (int i = 0; i < 1024; ++i) {
            MysteriumPatchesFixesCave.SINE_TABLE[i] = (float)Math.sin(i * 3.141592653589793 * 2.0 / 1024.0);
        }
        MysteriumPatchesFixesCave.biomeList[3] = 1;
        MysteriumPatchesFixesCave.biomeList[20] = 1;
        MysteriumPatchesFixesCave.biomeList[34] = 1;
        MysteriumPatchesFixesCave.biomeList[131] = 1;
        MysteriumPatchesFixesCave.biomeList[162] = 1;
        MysteriumPatchesFixesCave.biomeList[163] = 1;
        MysteriumPatchesFixesCave.biomeList[164] = 1;
        MysteriumPatchesFixesCave.biomeList[36] = 2;
        MysteriumPatchesFixesCave.biomeList[37] = 2;
        MysteriumPatchesFixesCave.biomeList[38] = 2;
        MysteriumPatchesFixesCave.biomeList[39] = 2;
        MysteriumPatchesFixesCave.biomeList[165] = 2;
        MysteriumPatchesFixesCave.biomeList[166] = 2;
        MysteriumPatchesFixesCave.biomeList[167] = 2;
        MysteriumPatchesFixesCave.biomeList[37] += 4;
        MysteriumPatchesFixesCave.biomeList[38] += 4;
        MysteriumPatchesFixesCave.biomeList[39] += 4;
        MysteriumPatchesFixesCave.biomeList[165] += 4;
        MysteriumPatchesFixesCave.biomeList[166] += 4;
        MysteriumPatchesFixesCave.biomeList[167] += 4;
        MysteriumPatchesFixesCave.biomeList[36] += 4;
        MysteriumPatchesFixesCave.biomeList[163] += 8;
        MysteriumPatchesFixesCave.biomeList[164] += 8;
        MysteriumPatchesFixesCave.biomeList[3] += 16;
        MysteriumPatchesFixesCave.biomeList[20] += 16;
        MysteriumPatchesFixesCave.biomeList[34] += 16;
        MysteriumPatchesFixesCave.biomeList[131] += 16;
        MysteriumPatchesFixesCave.biomeList[162] += 16;
        MysteriumPatchesFixesCave.biomeList[0] += 32;
        MysteriumPatchesFixesCave.biomeList[7] += 32;
        MysteriumPatchesFixesCave.biomeList[10] += 32;
        MysteriumPatchesFixesCave.biomeList[11] += 32;
        MysteriumPatchesFixesCave.biomeList[16] += 32;
        MysteriumPatchesFixesCave.biomeList[26] += 32;
        for (int i = 15; i <= 195; ++i) {
            final float radiusW = i / 10.0f;
            float heightMultiplier;
            if (i < 35) {
                heightMultiplier = 2.42858f - radiusW / 3.5f;
            }
            else if (i < 70) {
                heightMultiplier = 1.85716f - radiusW / 8.1665f;
            }
            else if (i < 110) {
                heightMultiplier = 1.2f - radiusW / 25.0f;
            }
            else {
                heightMultiplier = 0.96706f - radiusW / 53.125f;
            }
            MysteriumPatchesFixesCave.ravineHeightLookup[i - 15] = heightMultiplier;
        }
    }
}
